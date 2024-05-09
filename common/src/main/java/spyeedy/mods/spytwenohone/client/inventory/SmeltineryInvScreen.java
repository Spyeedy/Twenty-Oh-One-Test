package spyeedy.mods.spytwenohone.client.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import spyeedy.mods.spytwenohone.container.SmeltineryContainer;

public class SmeltineryInvScreen extends AbstractContainerScreen<SmeltineryContainer> {

	public SmeltineryInvScreen(SmeltineryContainer menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
	}

	@Override
	public boolean isPauseScreen() {
		return super.isPauseScreen();
	}
}
