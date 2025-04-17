package spyeedy.mods.spytwenohone.storage.item.forge;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import spyeedy.mods.spytwenohone.storage.item.ItemHandler;

public class ItemHandlerImpl implements IItemHandler, ItemHandler {

	@Override
	public int getSlots() {
		return 0;
	}

	@Override
	public @NotNull ItemStack getStackInSlot(int i) {
		return null;
	}

	@Override
	public @NotNull ItemStack insertItem(int i, @NotNull ItemStack arg, boolean bl) {
		return null;
	}

	@Override
	public @NotNull ItemStack extractItem(int i, int j, boolean bl) {
		return null;
	}

	@Override
	public int getSlotLimit(int i) {
		return 0;
	}

	@Override
	public boolean isItemValid(int i, @NotNull ItemStack arg) {
		return false;
	}
}
