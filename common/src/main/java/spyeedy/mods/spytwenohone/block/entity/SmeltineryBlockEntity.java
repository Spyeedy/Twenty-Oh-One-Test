package spyeedy.mods.spytwenohone.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.block.SmeltineryBlock;
import spyeedy.mods.spytwenohone.container.SmeltineryContainer;
import spyeedy.mods.spytwenohone.recipe.SmeltineryRecipe;
import spyeedy.mods.spytwenohone.recipe.SpyTooRecipes;

public class SmeltineryBlockEntity extends BlockEntity implements Container, MenuProvider {
	public static final int SLOTS = 8;

	public static final int SLOT_FLUID = 0;
	public static final int SLOT_FUEL = 1;
	public static final int SLOT_METAL = 2;
	public static final int SLOT_MATERIAL_START = 3;
	public static final int SLOT_RESULT = SLOTS - 1;
	public static final int MAX_MATERIALS = SLOT_RESULT - SLOT_MATERIAL_START;
	public static final int DATA_COUNT = 2;

	private static final int MAX_LIT = 100;

	private NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
	private int litTime;
	private int progress;
	protected final ContainerData dataAccess;
	private final RecipeManager.CachedCheck<SmeltineryBlockEntity, SmeltineryRecipe> quickCheck;

	public SmeltineryBlockEntity(BlockPos pos, BlockState blockState) {
		super(SpyTooBlockEntities.SMELTINERY.get(), pos, blockState);

		litTime = progress = 0;

		quickCheck = RecipeManager.createCheck(SpyTooRecipes.SMELTINERY_RECIPE.get());
		dataAccess = new ContainerData() {
			@Override
			public int get(int index) {
				return switch (index) {
					case 0 -> progress;
					case 1 -> litTime;
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
						litTime = value;
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

		this.setChanged();
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
		this.progress = tag.getShort("Progress");
		this.litTime = tag.getShort("LitTime");
		inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, inventory);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putShort("Progress", (short) this.progress);
		tag.putShort("LitTime", (short) this.litTime);
		ContainerHelper.saveAllItems(tag, inventory);
	}

	public static boolean isFuel(ItemStack stack) {
		return stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL;
	}

	public boolean isLit() {
		return this.litTime > 0;
	}

	public static boolean canCook(RegistryAccess registryAccess, @Nullable SmeltineryRecipe recipe, SmeltineryBlockEntity blockEntity) {
		if (recipe != null && !blockEntity.isRecipeSlotsEmpty()) {
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
		boolean oldIsLit = blockEntity.isLit();
		boolean shouldUpdate = false;
		if (oldIsLit) {
			--blockEntity.litTime;
		}

		SpyTwentyOhOne.LOGGER.info("Smeltinery ticking");

		ItemStack fuelStack = blockEntity.inventory.get(SLOT_FUEL);
		// if it's lit or if not lit must have fuel & the recipe items
		if (blockEntity.isLit() || !fuelStack.isEmpty() && !blockEntity.isRecipeSlotsEmpty()) {
//			SpyTwentyOhOne.LOGGER.info("Smeltinery may cook");

			SmeltineryRecipe recipe = null;
			if (!blockEntity.isRecipeSlotsEmpty()) {
				recipe = blockEntity.quickCheck.getRecipeFor(blockEntity, level).orElse(null);
			}

			if (recipe != null)
				SpyTwentyOhOne.LOGGER.info("Smeltinery has a recipe!");

			if (canCook(level.registryAccess(), recipe, blockEntity)) {
				if (!blockEntity.isLit()) { // if not lit AND can cook
					shouldUpdate = true;
					blockEntity.litTime = MAX_LIT;
					fuelStack.shrink(1);
					blockEntity.inventory.set(SLOT_FUEL, fuelStack);
				} else { // if is lit AND can cook
					++blockEntity.progress;
					if (blockEntity.progress >= recipe.getProcessTime()) {
						shouldUpdate = true;
						blockEntity.progress = 0;
						consumeItems(level.registryAccess(), recipe, blockEntity);
					}
				}
			} else {
				blockEntity.progress = 0;
			}
		} else if (!blockEntity.isLit() && blockEntity.progress > 0)
			blockEntity.progress = Mth.clamp(blockEntity.progress - 2, 0, blockEntity.progress);

		// if the old lit state doesn't match current lit state, we set the blockstate!
		if (oldIsLit != blockEntity.isLit()) {
			shouldUpdate = true;
			state = state.setValue(SmeltineryBlock.LIT, blockEntity.isLit());
			level.setBlock(pos, state, 3);
		}

		if (shouldUpdate)
			setChanged(level, pos, state);
	}
}
