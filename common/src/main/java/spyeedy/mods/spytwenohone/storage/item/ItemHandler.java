package spyeedy.mods.spytwenohone.storage.item;

import net.minecraft.world.item.ItemStack;

public interface ItemHandler {
	int getSlots();

	ItemStack getStackInSlot(int idx);

	ItemStack insertItem(int idx, ItemStack stack, boolean simulate);

	ItemStack extractItem(int idx, int amount, boolean simulate);

	/**
	 * Get the maximum stack size for a specific slot
	 */
	int getSlotLimit(int idx);
}
