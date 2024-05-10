package spyeedy.mods.spytwenohone.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import spyeedy.mods.spytwenohone.block.SmeltineryBlock;
import spyeedy.mods.spytwenohone.block.entity.SmeltineryBlockEntity;
import spyeedy.mods.spytwenohone.util.RenderUtils;

public class SmeltineryBlockEntityRenderer implements BlockEntityRenderer<SmeltineryBlockEntity> {

	private final BlockEntityRendererProvider.Context context;
	/*private static float FLUID_WIDTH = 12.0f / 16.0f;
	private static float FLUID_HEIGHT = 10.0f / 16.0f;*/

	private TextureAtlasSprite waterTexture;

	public SmeltineryBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
		this.waterTexture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(RenderUtils.FLUID_STILL);
	}

	@Override
	public void render(SmeltineryBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		Direction facing = blockEntity.getBlockState().getValue(SmeltineryBlock.FACING);
		float rotation = 0.0f; // rotation goes in CCW
		switch (facing) {
			case SOUTH -> rotation = 180.0f;
			case WEST -> rotation = 90.0f;
			case EAST -> rotation = 270.0f;
		}

		poseStack.pushPose();
		poseStack.translate(0.5, 0.0, 0.5);
		poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

		// Note that for pos coordinates, they can be multiplied against 16 to get integer value in terms of coordinate placement in the block model json.
		// That's because 0.0f to 1.0f spans a whole block. One block can be segmented into 16 sections, or 16 pixels in Minecraft context

		// Render fluid
		{
			// Fluid render setup
			int red = RenderUtils.COLOR_WATER_FLUID >> 16 & 255;
			int green = RenderUtils.COLOR_WATER_FLUID >> 8 & 255;
			int blue = RenderUtils.COLOR_WATER_FLUID & 255;
			int alpha = 255;

			VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucentCull(waterTexture.atlasLocation()));
			float minU = waterTexture.getU0();
			float maxU = waterTexture.getU1();
			float minV = waterTexture.getV0();
			float maxV = waterTexture.getV1();
			float uvWidth = maxU - minU;
			float uvHeight = maxV - minV;

			float fluidWidth = 0.75f; // 12/16
			float fluidHeight = 0.625f * blockEntity.getFluidAmount() / SmeltineryBlockEntity.MAX_FLUID; // 10/16

			float endU = minU + fluidWidth * uvWidth;
			float endV = minV + fluidHeight * uvHeight;

			// Actual fluid rendering, face is drawn CCW starting from bottom-right corner. 2/16 = 0.125
			poseStack.pushPose();
			poseStack.translate(-0.5f, 0.25f, -0.5f);
			var matrix4f = poseStack.last().pose();
			var matrix3f = poseStack.last().normal();
			// South face
			{
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.875f, 0.0f, 0.875f, red, green, blue, alpha, endU, endV, packedOverlay, packedLight);
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.875f, fluidHeight, 0.875f, red, green, blue, alpha, endU, minV, packedOverlay, packedLight);
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.125f, fluidHeight, 0.875f, red, green, blue, alpha, minU, minV, packedOverlay, packedLight);
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.125f, 0.0f, 0.875f, red, green, blue, alpha, minU, endV, packedOverlay, packedLight);
			}
			// West face
			{
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.125f, 0.0f, 0.875f, red, green, blue, alpha, endU, endV, packedOverlay, packedLight);
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.125f, fluidHeight, 0.875f, red, green, blue, alpha, endU, minV, packedOverlay, packedLight);
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.125f, fluidHeight, 0.125f, red, green, blue, alpha, minU, minV, packedOverlay, packedLight);
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.125f, 0.0f, 0.125f, red, green, blue, alpha, minU, endV, packedOverlay, packedLight);
			}
			// East face
			{
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.875f, 0.0f, 0.125f, red, green, blue, alpha, endU, endV, packedOverlay, packedLight);
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.875f, fluidHeight, 0.125f, red, green, blue, alpha, endU, minV, packedOverlay, packedLight);
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.875f, fluidHeight, 0.875f, red, green, blue, alpha, minU, minV, packedOverlay, packedLight);
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.875f, 0.0f, 0.875f, red, green, blue, alpha, minU, endV, packedOverlay, packedLight);
			}
			// Top face
			if (blockEntity.getFluidAmount() < SmeltineryBlockEntity.MAX_FLUID && blockEntity.getFluidAmount() > 0) {
				endU = minU + fluidWidth * uvWidth;
				endV = minV + fluidWidth * uvHeight; // because it's a square and the length is of the fluidWidth, so use the width again

				addVertex(vertexConsumer, matrix4f, matrix3f, 0.875f, fluidHeight, 0.875f, red, green, blue, alpha, endU, endV, packedOverlay, packedLight);
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.875f, fluidHeight, 0.125f, red, green, blue, alpha, endU, minV, packedOverlay, packedLight);
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.125f, fluidHeight, 0.125f, red, green, blue, alpha, minU, minV, packedOverlay, packedLight);
				addVertex(vertexConsumer, matrix4f, matrix3f, 0.125f, fluidHeight, 0.875f, red, green, blue, alpha, minU, endV, packedOverlay, packedLight);
			}
			poseStack.popPose();
		}

		// Render items
		{

		}

		poseStack.popPose();
	}

	private static void addVertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, float posX, float posY, float posZ, int red, int green, int blue, int alpha, float u, float v, int overlay, int lightmap) {
		consumer.vertex(pose, posX, posY, posZ).color(red, green, blue, alpha).uv(u, v).overlayCoords(overlay).uv2(lightmap).normal(normal, 0.0f, 1.0f, 0.0f).endVertex();
	}
}
