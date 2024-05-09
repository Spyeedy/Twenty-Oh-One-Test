package spyeedy.mods.spytwenohone.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class SmeltineryContainer extends AbstractContainerMenu {

	private final Inventory playerInv;

	public SmeltineryContainer(int containerId, Inventory playerInv) {
		super(SpyTooContainers.SMELTINERY.get(), containerId);
		this.playerInv = playerInv;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
}
