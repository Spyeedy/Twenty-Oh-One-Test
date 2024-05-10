package spyeedy.mods.spytwenohone.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RenderUtils {

	// Decimal color was lifted from plains biome json in the minecraft datapack. The decimal was in RGB format, does not include alpha as seen in @net.minecraft.client.renderer.block.LiquidBlockRenderer
	public static final int COLOR_WATER_FLUID = 4159204;
	public static final ResourceLocation FLUID_STILL = new ResourceLocation("block/water_still");
	public static final ResourceLocation FLUID_FLOW = new ResourceLocation("block/water_flow");

	/**
	 * Renders a fluid for your gui
	 * @param guiGraphics
	 * @param atlasSprite You can get the desired fluid like so <pre>minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("block/water_still"))</pre>
	 * @param posX
	 * @param posY
	 * @param zLevel
	 * @param width
	 * @param height
	 * @param rgb RGB color in decimal
	 */
	@Environment(EnvType.CLIENT)
	public static void renderGuiFluid(GuiGraphics guiGraphics, TextureAtlasSprite atlasSprite, int posX, int posY, int zLevel, int width, int height, int rgb) {
		int red = rgb >> 16 & 255;
		int green = rgb >> 8 & 255;
		int blue = rgb & 255;
		int alpha = 255;

		float minU = atlasSprite.getU0();
		float maxU = atlasSprite.getU1();
		float minV = atlasSprite.getV0();
		float maxV = atlasSprite.getV1();

		RenderSystem.setShaderTexture(0, atlasSprite.atlasLocation());
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		RenderSystem.enableBlend();
		Matrix4f matrix4f = guiGraphics.pose().last().pose();
		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();

		// we have to draw each part to ensure they are of maximum 16 by 16 pixels so as to not stretch the texture/pixels
		for (int i = 0; i < width; i += 16) {
			for (int j = 0; j < height; j += 16) {
				int drawWidth = Math.min(width - i, 16); // ensures that the drawn texture is 16 pixels or less
				int drawHeight = Math.min(height - j, 16);

				int drawX = posX + i;
				int drawY = posY + j;

				float endU = minU + (maxU - minU) * (drawWidth / 16.0f); // length of the texture multiplied by drawWidth/16.0f
				float endV = minV + (maxV - minV) * (drawHeight / 16.0f);

				// corners drawn counter-clockwise, starting from top left
				bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
				bufferBuilder.vertex(matrix4f, drawX, drawY + drawHeight, zLevel).color(red, green, blue, alpha).uv(minU, endV).endVertex();
				bufferBuilder.vertex(matrix4f, drawX + drawWidth, drawY + drawHeight, zLevel).color(red, green, blue, alpha).uv(endU, endV).endVertex();
				bufferBuilder.vertex(matrix4f, drawX + drawWidth, drawY, zLevel).color(red, green, blue, alpha).uv(endU, minV).endVertex();
				bufferBuilder.vertex(matrix4f, drawX, drawY, zLevel).color(red, green, blue, alpha).uv(minU, minV).endVertex();
				BufferUploader.drawWithShader(bufferBuilder.end());
			}
		}

		RenderSystem.disableBlend();
	}
}
