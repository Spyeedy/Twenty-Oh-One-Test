package spyeedy.mods.spytwenohone.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.container.SmeltineryContainer;

public class SmeltineryBlockEntity extends BlockEntity implements Container, MenuProvider {
	public static final int SLOTS = 8;

	public static final int SLOT_FLUID = 0;
	public static final int SLOT_FUEL = 1;
	public static final int SLOT_METAL = 2;
	public static final int SLOT_MATERIAL_START = 3;
	public static final int SLOT_RESULT = SLOTS - 1;
	public static final int MAX_MATERIALS = SLOT_RESULT - SLOT_MATERIAL_START;

	private NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

	public SmeltineryBlockEntity(BlockPos pos, BlockState blockState) {
		super(SpyTooBlockEntities.SMELTINERY.get(), pos, blockState);
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

		if (!this.level.isClientSide) {
			SpyTwentyOhOne.LOGGER.info("Yoohoo setting item");
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
		return new SmeltineryContainer(i, inventory, this);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, inventory);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		ContainerHelper.saveAllItems(tag, inventory);
	}
}
