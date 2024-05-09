package spyeedy.mods.spytwenohone.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import spyeedy.mods.spytwenohone.container.SmeltineryContainer;

public class SmeltineryBlockEntity extends BlockEntity implements Container, MenuProvider {

	private NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);

	public SmeltineryBlockEntity(BlockPos pos, BlockState blockState) {
		super(SpyTooBlockEntities.SMELTINERY.get(), pos, blockState);
	}

	@Override
	public int getContainerSize() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public ItemStack getItem(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {

	}

	@Override
	public boolean stillValid(Player player) {
		return !player.isSpectator();
	}

	@Override
	public void clearContent() {

	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("block.spytoo.smeltinery");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return new SmeltineryContainer(i, inventory);
	}
}
