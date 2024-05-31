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

		// Fluid input slot
		this.addSlot(new Slot(container, SmeltineryBlockEntity.SLOT_FLUID_INPUT, 8, 10) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() == Items.WATER_BUCKET;
			}

			@Override
			public int getMaxStackSize() {
				return 1;
			}
		});

		// Fluid output slot
		this.addSlot(new Slot(container, SmeltineryBlockEntity.SLOT_FLUID_OUTPUT, 8, 85) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() == Items.BUCKET;
			}

			@Override
			public int getMaxStackSize() {
				return 1;
			}
		});

		// Fuel Slot
		this.addSlot(new Slot(container, SmeltineryBlockEntity.SLOT_FUEL, 62, 94) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return SmeltineryBlockEntity.isFuel(stack);
			}
		});

		// Metal Slot
		this.addSlot(new Slot(container, SmeltineryBlockEntity.SLOT_METAL, 80, 49));

		// Materials Slot
		for (int y = 0; y < 4; y++) {
			this.addSlot(new Slot(container, SmeltineryBlockEntity.SLOT_MATERIAL_START + y, 35, 22 + y * 18));
		}

		// Result Slot
		this.addSlot(new Slot(container, SmeltineryBlockEntity.SLOT_RESULT, 144, 49) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
		});

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 118 + y * 18));
			}
		}

		for(int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(playerInv, x, 8 + x * 18, 176));
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

	public int getEnergy() {
		return this.containerData.get(SmeltineryBlockEntity.DataValues.ENERGY.ordinal());
	}

	public int getProgress() {
		return this.containerData.get(SmeltineryBlockEntity.DataValues.PROGRESS.ordinal());
	}

	public int getMaxProgress() {
		return this.containerData.get(SmeltineryBlockEntity.DataValues.MAX_PROGRESS.ordinal());
	}

	public int getFluidAmount() {
		return this.containerData.get(SmeltineryBlockEntity.DataValues.FLUID_AMOUNT.ordinal());
	}
}
