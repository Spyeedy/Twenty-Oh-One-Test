package spyeedy.mods.spytwenohone.client.gui.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.block.entity.SmeltineryBlockEntity;
import spyeedy.mods.spytwenohone.container.SmeltineryContainer;
import spyeedy.mods.spytwenohone.util.RenderUtils;

public class SmeltineryInvScreen extends AbstractContainerScreen<SmeltineryContainer> {

	static final ResourceLocation GUI_TEX = new ResourceLocation(SpyTwentyOhOne.MOD_ID, "textures/gui/smeltinery.png");
	static int PROGRESS_LENGTH = 4; // how much do the bars span: outline + core
	static int PROGRESS_MAT_V_LENGTH = 11;
	static int PROGRESS_MAT_H_LENGTH = 41;
	static int PROGRESS_METAL_V_LENGTH = 34;
	static int PROGRESS_CONVERGE_LENGTH = 4;
	static int PROGRESS_COMBINED_H_LENGTH = 60;
	static int PROGRESS_COMBINED_V_LENGTH = 34;

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

		if (mouseX >= (leftPos + 114) && mouseX < (leftPos + 160) && mouseY >= (topPos + 98) && mouseY < (topPos + 106))
		guiGraphics.renderTooltip(this.font, Component.translatable("gui.spytoo.smeltinery.energy", this.menu.getEnergy(), SmeltineryBlockEntity.MAX_ENERGY), mouseX, mouseY);
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
			guiGraphics.blit(GUI_TEX, i + 80, j + 49, imageWidth, 16, 16, 16);
		}

		// Energy
		if (this.menu.getEnergy() > 0) {
			int energyBar = (int) ((menu.getEnergy() / (float) SmeltineryBlockEntity.MAX_ENERGY) * 46);
			guiGraphics.blit(GUI_TEX, i + 114, j + 98, imageWidth, 83, energyBar, 8);
		}

		// Fluid
		if (this.menu.getFluidAmount() > 0) {
			int maxHeight = 49;
			int fluidHeight = (int)(this.menu.getFluidAmount() / (float) SmeltineryBlockEntity.MAX_FLUID * maxHeight);

			RenderUtils.renderGuiFluid(guiGraphics, this.minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(RenderUtils.FLUID_STILL), i + 8, j + 31 + maxHeight - fluidHeight, 0, 16, fluidHeight, RenderUtils.COLOR_WATER_FLUID);
		}

		// Fluid markings
		guiGraphics.blit(GUI_TEX, i + 7, j + 30, imageWidth, 32, 18, 51);

		// Progress
		{
			int prog_int = this.menu.getProgress();
			float progress = Mth.clamp(prog_int / (float)this.menu.getMaxProgress(), 0.0f, 1.0f);

			float mat_metal_percent = 0.4f;
			float converge_percent = 0.1f;
			float remainder_percent = 1.0f - mat_metal_percent - converge_percent;

			if (prog_int > 0) {
				int blitX = i + 41;
				int blitXOffset = 0;

				// Draw Materials & Metal progress
				{
					float progress_mat_metal = Mth.clamp(progress / mat_metal_percent, 0.0f, 1.0f); // make progress in range of 0.0f to 1.0f with respect to mat_metal_percent
					float progress_mat = progress_mat_metal * (PROGRESS_MAT_H_LENGTH + PROGRESS_MAT_V_LENGTH); // convert the progress into length with respect to materials' total length
					float progress_metal = progress_mat_metal * PROGRESS_METAL_V_LENGTH; // convert the progress into length with respect to metal length

					// material vertical
					int mat_v = Mth.clamp((int)progress_mat, 0, PROGRESS_MAT_V_LENGTH);
					guiGraphics.blit(GUI_TEX, blitX, j + 10 + PROGRESS_MAT_V_LENGTH - mat_v, blitXOffset, imageHeight + PROGRESS_MAT_V_LENGTH - mat_v, PROGRESS_LENGTH, mat_v);

					// material horizontal
					blitX += PROGRESS_LENGTH;
					blitXOffset += PROGRESS_LENGTH;
					int mat_h = Mth.clamp((int)progress_mat - PROGRESS_MAT_V_LENGTH, 0, PROGRESS_MAT_H_LENGTH);
					guiGraphics.blit(GUI_TEX, blitX, j + 10, blitXOffset, imageHeight, mat_h, PROGRESS_LENGTH);

					// vertical metal
					blitX += PROGRESS_MAT_H_LENGTH;
					blitXOffset += PROGRESS_MAT_H_LENGTH;
					int metal_v = Mth.clamp((int)progress_metal, 0, PROGRESS_METAL_V_LENGTH);
					guiGraphics.blit(GUI_TEX, blitX, j + 14 + PROGRESS_METAL_V_LENGTH - metal_v, blitXOffset, imageHeight + PROGRESS_LENGTH + PROGRESS_METAL_V_LENGTH - metal_v, PROGRESS_LENGTH, metal_v);
				}
				// Draw Converge progress, it's a horizontal bar
				if (progress >= mat_metal_percent) {
					float progress_converge = Mth.clamp((progress - mat_metal_percent) / converge_percent, 0.0f, 1.0f); // make progress in range of 0.0f to 1.0f with respect to converge_percent
					int converge_unit = (int)(progress_converge * PROGRESS_CONVERGE_LENGTH);
					guiGraphics.blit(GUI_TEX, blitX, j + 10, blitXOffset, imageHeight, converge_unit, PROGRESS_LENGTH);
				}
				// Draw remainder progress
				if (progress >= (mat_metal_percent + converge_percent)) {
					float progress_combined_decimal = Mth.clamp((progress - mat_metal_percent - converge_percent)/remainder_percent, 0.0f, 1.0f); // make progress in range of 0.0f to 1.0f with respect to remainder_percent
					float progress_combined = progress_combined_decimal * (PROGRESS_COMBINED_H_LENGTH + PROGRESS_COMBINED_V_LENGTH); // convert the progress into length with respect to combined's total length

					// combined horizontal
					int comb_h = Mth.clamp((int)progress_combined, 0, PROGRESS_COMBINED_H_LENGTH);
					blitX += PROGRESS_CONVERGE_LENGTH;
					blitXOffset += PROGRESS_CONVERGE_LENGTH;
					guiGraphics.blit(GUI_TEX, blitX, j + 10, blitXOffset, imageHeight, comb_h, PROGRESS_LENGTH);

					// combined vertical
					int comb_v = Mth.clamp((int)progress_combined - PROGRESS_COMBINED_H_LENGTH, 0, PROGRESS_COMBINED_V_LENGTH);
					blitX += PROGRESS_COMBINED_H_LENGTH;
					blitXOffset += PROGRESS_COMBINED_H_LENGTH;
					guiGraphics.blit(GUI_TEX, blitX, j + 10, blitXOffset, imageHeight, PROGRESS_LENGTH, comb_v); // combined vertical progress
				}
			}
		}
	}
}
