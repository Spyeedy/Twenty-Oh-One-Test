package spyeedy.mods.spytwenohone.registry.client.forge;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

public class RenderTypeRegistryImpl {

	public static void registerBlock(RegistrySupplier<Block> block, RenderType renderType) {
		ItemBlockRenderTypes.setRenderLayer(block.get(), renderType);
	}
}
