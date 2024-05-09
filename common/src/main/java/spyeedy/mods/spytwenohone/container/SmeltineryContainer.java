package spyeedy.mods.spytwenohone.container;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import spyeedy.mods.spytwenohone.block.entity.SmeltineryBlockEntity;

public class SmeltineryContainer extends AbstractContainerMenu {

	private Level level;
	private final Inventory playerInv;
	private final Container container;

	// Client-side constructor. Client gets an empty inventory and the container menu will update the client with the actual data
	public SmeltineryContainer(int containerId, Inventory playerInv) {
		this(containerId, playerInv, new SimpleContainer(SmeltineryBlockEntity.SLOTS));
	}

	// Server-side constructor, used by the block entity
	public SmeltineryContainer(int containerId, Inventory playerInv, Container container) {
		super(SpyTooContainers.SMELTINERY.get(), containerId);

		this.level = playerInv.player.level();
		this.playerInv = playerInv;
		this.container = container;

		int blockSlotIdx = 0;

		// Water slot
		this.addSlot(new Slot(container, blockSlotIdx++, 8, 60) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() == Items.WATER_BUCKET;
			}
		});

		// Fuel Slot
		this.addSlot(new Slot(container, blockSlotIdx++, 84, 60) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL;
			}
		});

		// Metal Slot
		this.addSlot(new Slot(container, blockSlotIdx++, 84, 19));

		// Materials Slot
		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 2; x++) {
				this.addSlot(new Slot(container, blockSlotIdx++, 35 + x * 18, 10 + y * 18));
			}
		}

		// Result Slot
		this.addSlot(new Slot(container, blockSlotIdx, 144, 19) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
		});

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		for(int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(playerInv, x, 8 + x * 18, 142));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack remainingStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack movingStack = slot.getItem();
			remainingStack = movingStack;

			if (!movingStack.isEmpty()) {
				if (index < this.container.getContainerSize()) { // move from block's container into player's inventory
					if (!this.moveItemStackTo(movingStack, this.container.getContainerSize(), this.container.getContainerSize() + 36, true)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(movingStack, 0, this.container.getContainerSize(), false)) { // move from player's inventory into block's container
					return ItemStack.EMPTY;
				}
			}

			slot.onTake(player, movingStack);
		}

		return remainingStack;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}
}
