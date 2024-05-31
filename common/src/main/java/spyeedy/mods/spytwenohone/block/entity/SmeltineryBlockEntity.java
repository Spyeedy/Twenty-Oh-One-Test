package spyeedy.mods.spytwenohone.block.entity;

import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import spyeedy.mods.spytwenohone.block.SmeltineryBlock;
import spyeedy.mods.spytwenohone.container.SmeltineryContainer;
import spyeedy.mods.spytwenohone.recipe.SmeltineryRecipe;
import spyeedy.mods.spytwenohone.recipe.SpyTooRecipes;

import java.util.HashMap;
import java.util.Map;

public class SmeltineryBlockEntity extends BlockEntity implements Container, MenuProvider {
	public static final int SLOTS = 9;

	public static final int SLOT_FLUID_INPUT = 0;
	public static final int SLOT_FLUID_OUTPUT = 1;
	public static final int SLOT_FUEL = 2;
	public static final int SLOT_METAL = 3;
	public static final int SLOT_MATERIAL_START = 4;
	public static final int SLOT_RESULT = SLOTS - 1;
	public static final int MAX_MATERIALS = SLOT_RESULT - SLOT_MATERIAL_START;
	public static final int DATA_COUNT = 6;

	public static final int MAX_LIT = 8000;
	public static final int MAX_FLUID = 5000;
	public static final int FLUID_PER_BUCKET = 1000;
	public static final int MAX_ENERGY = 10000;
	public static final HashMap<Item, Integer> ENERGY_SOURCES_MAP = new HashMap<>();

	private NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
	private float experience;
	private int energy;
	private int genEnergyProgress;
	private int maxGenEnergyProgress;
	private int progress;
	private int maxProgress;
	private int fluidAmount;
	protected final ContainerData dataAccess;
	private final RecipeManager.CachedCheck<SmeltineryBlockEntity, SmeltineryRecipe> quickCheck;

	static {
		addEnergySource(ENERGY_SOURCES_MAP, Items.LAVA_BUCKET, 1000);
		addEnergySource(ENERGY_SOURCES_MAP, Items.COAL, 800);
		addEnergySource(ENERGY_SOURCES_MAP, Items.CHARCOAL, 800);
	}

	public SmeltineryBlockEntity(BlockPos pos, BlockState blockState) {
		super(SpyTooBlockEntities.SMELTINERY.get(), pos, blockState);

		fluidAmount = energy = progress = 0;

		quickCheck = RecipeManager.createCheck(SpyTooRecipes.SMELTINERY_RECIPE.get());
		dataAccess = new ContainerData() {
			@Override
			public int get(int index) {
				return switch (index) {
					case 0 -> progress;
					case 1 -> maxProgress;
					case 2 -> energy;
					case 3 -> genEnergyProgress;
					case 4 -> maxGenEnergyProgress;
					case 5 -> fluidAmount;
					default -> 0;
				};
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0:
						progress = value;
						break;
					case 1:
						maxProgress = value;
						break;
					case 2:
						energy = value;
						break;
					case 3:
						genEnergyProgress = value;
						break;
					case 4:
						maxGenEnergyProgress = value;
						break;
					case 5:
						fluidAmount = value;
						break;
				}
			}

			@Override
			public int getCount() {
				return DATA_COUNT;
			}
		};
	}

	@Override
	public int getContainerSize() {
		return SLOTS;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : inventory) {
			if (!stack.isEmpty()) return false;
		}
		return true;
	}

	public boolean isRecipeSlotsEmpty() {
		for (int i = SLOT_METAL; i < SLOT_RESULT; i++) {
			if (!getItem(i).isEmpty()) return false;
		}
		return true;
	}

	@Override
	public ItemStack getItem(int slot) {
		return inventory.get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack result = ContainerHelper.removeItem(inventory, slot, amount);
		setChanged();
		return result;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(inventory, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		this.inventory.set(slot, stack);
		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}

		boolean shouldBlockUpdate = false;

		switch (slot) {
			case SLOT_FLUID_INPUT:
				if (stack.getItem() == Items.WATER_BUCKET && fluidAmount < MAX_FLUID) {
					shouldBlockUpdate = true;
					this.inventory.set(slot, new ItemStack(Items.BUCKET));
					addFluid(FLUID_PER_BUCKET, false);
				}
				if (!getItem(SLOT_FLUID_OUTPUT).isEmpty() && getItem(SLOT_FLUID_OUTPUT).getItem() == Items.BUCKET && fluidAmount >= FLUID_PER_BUCKET) {
					shouldBlockUpdate = true;
					this.inventory.set(SLOT_FLUID_OUTPUT, new ItemStack(Items.WATER_BUCKET));
					removeFluid(FLUID_PER_BUCKET, false);
				}
				break;
			case SLOT_FLUID_OUTPUT:
				if (stack.getItem() == Items.BUCKET && fluidAmount >= FLUID_PER_BUCKET) {
					shouldBlockUpdate = true;
					this.inventory.set(slot, new ItemStack(Items.WATER_BUCKET));
					removeFluid(FLUID_PER_BUCKET, false);
				}
				if (!getItem(SLOT_FLUID_INPUT).isEmpty() && getItem(SLOT_FLUID_INPUT).getItem() == Items.WATER_BUCKET && fluidAmount < MAX_FLUID) {
					shouldBlockUpdate = true;
					this.inventory.set(SLOT_FLUID_INPUT, new ItemStack(Items.BUCKET));
					addFluid(FLUID_PER_BUCKET, false);
				}
				break;
			default:
		}

		this.setChanged();
		if (shouldBlockUpdate)
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
	}

	@Override
	public boolean stillValid(Player player) {
		return !player.isSpectator();
	}

	@Override
	public void clearContent() {
		this.inventory.clear();
		this.setChanged();
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("block.spytoo.smeltinery");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return new SmeltineryContainer(i, inventory, this, dataAccess);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.experience = tag.getFloat("Experience");
		this.progress = tag.getShort("Progress");
		this.energy = tag.getShort("Energy");
		this.genEnergyProgress = tag.getShort("GenEnergyProgress");
		this.fluidAmount = tag.getShort("FluidAmt");
		inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, inventory);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putFloat("Experience", this.experience);
		tag.putShort("Progress", (short) this.progress);
		tag.putShort("Energy", (short) this.energy);
		tag.putShort("GenEnergyProgress", (short) this.genEnergyProgress);
		saveDataForUpdateTag(tag);
		ContainerHelper.saveAllItems(tag, inventory);
	}

	private void saveDataForUpdateTag(CompoundTag tag) {
		tag.putShort("FluidAmt", (short) this.fluidAmount);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag compoundTag = new CompoundTag();
		saveDataForUpdateTag(compoundTag);
		return compoundTag;
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public boolean hasProgress() {
		return this.progress > 0;
	}

	/**
	 * @param amount Amount of fluid to add to the block
	 * @return True if addition was successful, false if otherwise
	 */
	public boolean addFluid(int amount, boolean shouldContainerUpdate) {
		if (this.fluidAmount >= MAX_FLUID) return false;

		this.fluidAmount = Math.min(fluidAmount + amount, MAX_FLUID);

		if (shouldContainerUpdate) {
			this.setChanged();

			if (level != null)
				level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
		}

		return true;
	}

	/**
	 * @param amount Amount of fluid to remove from the block
	 * @return True if removal was successful, false if otherwise
	 */
	public boolean removeFluid(int amount, boolean shouldContainerUpdate) {
		if (this.fluidAmount <= 0) return false;

		this.fluidAmount = Math.max(0, fluidAmount - amount);

		if (shouldContainerUpdate) {
			this.setChanged();

			if (level != null)
				level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
		}

		return true;
	}

	public int getFluidAmount() {
		return fluidAmount;
	}

	public int getEnergy() {
		return energy;
	}

	public int getProgress() {
		return progress;
	}

	public int getMaxProgress() {
		return maxProgress;
	}

	@Nullable
	public SmeltineryRecipe getRecipe() {
		return isRecipeSlotsEmpty() ? null : quickCheck.getRecipeFor(this, level).orElse(null);
	}

	/**
	 * Consume progress if it's supposed to "idle". Idle conditions is left to the caller to decide
	 */
	public void idleConsumption() {
		progress = Mth.clamp(progress - 2, 0, progress);
		if (progress == 0) {
			maxProgress = 0;
		}
	}

	public static boolean isFuel(ItemStack stack) {
		return ENERGY_SOURCES_MAP.containsKey(stack.getItem());
	}

	public static int getGeneratedEnergy(ItemStack stack) {
		return ENERGY_SOURCES_MAP.getOrDefault(stack.getItem(), 0);
	}

	public static boolean canCook(RegistryAccess registryAccess, @Nullable SmeltineryRecipe recipe, SmeltineryBlockEntity blockEntity) {
		if (recipe != null && !blockEntity.isRecipeSlotsEmpty() && blockEntity.energy >= recipe.getEnergyRequired()) {
			ItemStack resultStack = recipe.getResultItem(registryAccess);
			if (resultStack.isEmpty())
				return false;

			ItemStack outputItemStack = blockEntity.getItem(SLOT_RESULT);
			if (outputItemStack.isEmpty())
				return true;
			else if (!ItemStack.isSameItem(resultStack, outputItemStack))
				return false;
			else {
				int finalCount = resultStack.getCount() + outputItemStack.getCount();
				return finalCount < blockEntity.getMaxStackSize() && finalCount < outputItemStack.getMaxStackSize();
			}
		}
		return false;
	}

	public static boolean consumeItems(RegistryAccess registryAccess, @Nullable SmeltineryRecipe recipe, SmeltineryBlockEntity blockEntity) {
		if (canCook(registryAccess, recipe, blockEntity)) {
			ItemStack resultStack = recipe.getResultItem(registryAccess);
			ItemStack outputStack = blockEntity.getItem(SLOT_RESULT);

			if (outputStack.isEmpty()) {
				blockEntity.inventory.set(SLOT_RESULT, resultStack.copy());
			} else {
				outputStack.grow(resultStack.getCount());
			}

			if (blockEntity.level != null) {
				recipe.consumeInputs(blockEntity, blockEntity.level);
			}
			return true;
		}
		return false;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, SmeltineryBlockEntity blockEntity) {
		boolean shouldUpdate = false;
		boolean shouldClientUpdate = false;

		// Energy Generation
		{
			ItemStack fuelStack = blockEntity.inventory.get(SLOT_FUEL);

			if (blockEntity.energy < MAX_ENERGY && !fuelStack.isEmpty() && isFuel(fuelStack)) {
				blockEntity.energy += getGeneratedEnergy(fuelStack);
				if (blockEntity.energy > MAX_ENERGY) blockEntity.energy = MAX_ENERGY;

				shouldUpdate = true;

				if (fuelStack.is(Items.LAVA_BUCKET)) {
					blockEntity.inventory.set(SLOT_FUEL, new ItemStack(Items.BUCKET));
				} else {
					fuelStack.shrink(1);
				}
			}
		}

		// Cooking
		{
			SmeltineryRecipe recipe = null;

			if (!blockEntity.isRecipeSlotsEmpty()) {
				recipe = blockEntity.quickCheck.getRecipeFor(blockEntity, level).orElse(null);
			}

			if (recipe != null) {
//				SpyTwentyOhOne.LOGGER.info("Smeltinery has a recipe!");
				blockEntity.maxProgress = recipe.getProcessTime();

				if (canCook(level.registryAccess(), recipe, blockEntity)) {
					++blockEntity.progress;
					if (blockEntity.progress >= recipe.getProcessTime()) {
						shouldUpdate = true;
						shouldClientUpdate = true;
						blockEntity.experience += recipe.getExperience();

						blockEntity.energy -= recipe.getEnergyRequired();
						blockEntity.removeFluid(recipe.getFluidConsumption(), false);

						blockEntity.progress = 0;
						consumeItems(level.registryAccess(), recipe, blockEntity);
					}
				} else {
					blockEntity.idleConsumption();
				}
			} else {
				blockEntity.progress = 0;
				blockEntity.maxProgress = 0;
			}
		}

		// if the old lit state doesn't match current lit state, we set the blockstate!
		if (getLitBlockState(level, pos) != blockEntity.hasProgress()) {
			shouldUpdate = true;
			state = state.setValue(SmeltineryBlock.LIT, blockEntity.hasProgress());
			level.setBlock(pos, state, 3);
		}

		if (shouldUpdate)
			setChanged(level, pos, state);
		if (shouldClientUpdate)
			level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
	}

	private static boolean isNonFlammable(Item item) {
		return item.builtInRegistryHolder().is(ItemTags.NON_FLAMMABLE_WOOD);
	}

	public static void addEnergySource(Map<Item, Integer> map, TagKey<Item> itemTag, int energy) {
		for (Holder<Item> itemHolder : BuiltInRegistries.ITEM.getTagOrEmpty(itemTag)) {
			if (!isNonFlammable(itemHolder.value())) {
				map.put(itemHolder.value(), energy);
			}
		}
	}

	private static void addEnergySource(Map<Item, Integer> map, ItemLike item, int energy) {
		Item item2 = item.asItem();
		if (isNonFlammable(item2)) {
			if (SharedConstants.IS_RUNNING_IN_IDE) {
				throw Util.pauseInIde(new IllegalStateException("A developer tried to explicitly make fire resistant item " + item2.getName(null).getString() + " a furnace fuel. That will not work!"));
			}
		} else {
			map.put(item2, energy);
		}
	}

	public static boolean getLitBlockState(Level level, BlockPos pos) {
		return level.getBlockState(pos).getValue(SmeltineryBlock.LIT);
	}

	public enum DataValues {
		PROGRESS, MAX_PROGRESS, ENERGY, GEN_ENERGY_PROGRESS, MAX_GEN_ENERGY_PROGRESS, FLUID_AMOUNT
	}
}
