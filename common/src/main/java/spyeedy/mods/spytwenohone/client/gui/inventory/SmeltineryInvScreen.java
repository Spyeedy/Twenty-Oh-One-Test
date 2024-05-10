package spyeedy.mods.spytwenohone.client.gui.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.block.entity.SmeltineryBlockEntity;
import spyeedy.mods.spytwenohone.container.SmeltineryContainer;
import spyeedy.mods.spytwenohone.util.RenderUtils;

public class SmeltineryInvScreen extends AbstractContainerScreen<SmeltineryContainer> {

	static final ResourceLocation GUI_TEX = new ResourceLocation(SpyTwentyOhOne.MOD_ID, "textures/gui/smeltinery.png");

	public SmeltineryInvScreen(SmeltineryContainer menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		this.imageHeight = 200;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.renderTooltip(guiGraphics, mouseX, mouseY);

		if (mouseX >= (leftPos + 8) && mouseX < (leftPos + 24) && mouseY >= (topPos + 31) && mouseY < (topPos + 80)) {
			guiGraphics.renderTooltip(this.font, Component.translatable("gui.spytoo.smeltinery.fluid_level", this.menu.getFluidAmount(), SmeltineryBlockEntity.MAX_FLUID), mouseX, mouseY);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(GUI_TEX, i, j, 0, 0, this.imageWidth, this.imageHeight);

		// Metal slot icon
		if (!this.menu.getSlot(SmeltineryBlockEntity.SLOT_METAL).hasItem()) {
			guiGraphics.blit(GUI_TEX, i + 80, j + 49, imageWidth, 31, 16, 16);
		}

		// Lit state
		if (this.menu.isLit()) {
			int litProg = menu.getLitProgress();
			guiGraphics.blit(GUI_TEX, i + 80, j + 88 - litProg, imageWidth, 12 - litProg, 14, litProg + 1);
		}

		// Progress
		guiGraphics.blit(GUI_TEX, i + 109, j + 48, imageWidth, 14, menu.getProgress() + 1, 17);

		// Fluid
		if (this.menu.getFluidAmount() > 0) {
			int maxHeight = 49;
			int fluidHeight = (int)(this.menu.getFluidAmount() / (float) SmeltineryBlockEntity.MAX_FLUID * maxHeight);

			RenderUtils.renderGuiFluid(guiGraphics, this.minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(RenderUtils.FLUID_STILL), i + 8, j + 31 + maxHeight - fluidHeight, 0, 16, fluidHeight, RenderUtils.COLOR_WATER_FLUID);
		}

		// Fluid Label
		guiGraphics.blit(GUI_TEX, i + 7, j + 30, imageWidth, 47, 18, 51);
	}
}
