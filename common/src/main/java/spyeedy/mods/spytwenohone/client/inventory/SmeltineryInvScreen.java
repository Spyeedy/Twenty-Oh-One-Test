package spyeedy.mods.spytwenohone.client.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.block.entity.SmeltineryBlockEntity;
import spyeedy.mods.spytwenohone.container.SmeltineryContainer;

public class SmeltineryInvScreen extends AbstractContainerScreen<SmeltineryContainer> {

	static final ResourceLocation GUI_TEX = new ResourceLocation(SpyTwentyOhOne.MOD_ID, "textures/gui/smeltinery.png");

	public SmeltineryInvScreen(SmeltineryContainer menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(GUI_TEX, i, j, 0, 0, this.imageWidth, this.imageHeight);

		if (!this.menu.getSlot(SmeltineryBlockEntity.SLOT_METAL).hasItem()) {
			guiGraphics.blit(GUI_TEX, i + 84, j + 19, imageWidth, 31, 16, 16);
		}

		if (this.menu.isLit()) {
			int litProg = menu.getLitProgress();
			guiGraphics.blit(GUI_TEX, i + 84, j + 55 - litProg, imageWidth, 12 - litProg, 14, litProg + 1);
		}

		// Progress
		guiGraphics.blit(GUI_TEX, i + 109, j + 18, imageWidth, 14, menu.getProgress() + 1, 17);
	}
}
