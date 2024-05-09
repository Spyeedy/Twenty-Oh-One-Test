package spyeedy.mods.spytwenohone.container;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import spyeedy.mods.spytwenohone.block.entity.SmeltineryBlockEntity;

public class SmeltineryContainer extends AbstractContainerMenu {

	private Level level;
	private final Inventory playerInv;
	private final Container container;
	private final ContainerData containerData;

	// Client-side constructor. Client gets an empty inventory and the container menu will update the client with the actual data
	public SmeltineryContainer(int containerId, Inventory playerInv) {
		this(containerId, playerInv, new SimpleContainer(SmeltineryBlockEntity.SLOTS), new SimpleContainerData(SmeltineryBlockEntity.DATA_COUNT));
	}

	// Server-side constructor, used by the block entity
	public SmeltineryContainer(int containerId, Inventory playerInv, Container container, ContainerData containerData) {
		super(SpyTooContainers.SMELTINERY.get(), containerId);

		this.level = playerInv.player.level();
		this.playerInv = playerInv;
		checkContainerSize(container, SmeltineryBlockEntity.SLOTS);
		checkContainerDataCount(containerData, SmeltineryBlockEntity.DATA_COUNT);
		this.container = container;
		this.containerData = containerData;

		// Water slot
		this.addSlot(new Slot(container, SmeltineryBlockEntity.SLOT_FLUID, 8, 60) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() == Items.WATER_BUCKET;
			}
		});

		// Fuel Slot
		this.addSlot(new Slot(container, SmeltineryBlockEntity.SLOT_FUEL, 84, 60) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return SmeltineryBlockEntity.isFuel(stack);
			}
		});

		// Metal Slot
		this.addSlot(new Slot(container, SmeltineryBlockEntity.SLOT_METAL, 84, 19));

		// Materials Slot
		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 2; x++) {
				this.addSlot(new Slot(container, SmeltineryBlockEntity.SLOT_MATERIAL_START + x + y * 2, 35 + x * 18, 10 + y * 18));
			}
		}

		// Result Slot
		this.addSlot(new Slot(container, SmeltineryBlockEntity.SLOT_RESULT, 144, 19) {
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

		this.addDataSlots(containerData);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack quickMovedStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack rawStack = slot.getItem();
			quickMovedStack = rawStack.copy();

			if (!rawStack.isEmpty()) {
				if (index < this.container.getContainerSize()) { // move from block's container into player's inventory
					if (!this.moveItemStackTo(rawStack, this.container.getContainerSize(), this.container.getContainerSize() + 36, true)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(rawStack, 0, this.container.getContainerSize(), false)) { // move from player's inventory into block's container
					return ItemStack.EMPTY;
				}
			}

			if (rawStack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (rawStack.getCount() == quickMovedStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, rawStack);
		}

		return quickMovedStack;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}

	public int getProgress() {
		int i = this.containerData.get(SmeltineryBlockEntity.DataValues.PROGRESS.ordinal());
		int j = this.containerData.get(SmeltineryBlockEntity.DataValues.MAX_PROGRESS.ordinal());

		return j > 0 && i > 0 ? i * 24 / j : 0;
	}

	public int getLitProgress() {
		return this.containerData.get(SmeltineryBlockEntity.DataValues.LIT_TIME.ordinal()) * 13 / SmeltineryBlockEntity.MAX_LIT;
	}

	public boolean isLit() {
		return this.containerData.get(SmeltineryBlockEntity.DataValues.LIT_TIME.ordinal()) > 0;
	}
}
