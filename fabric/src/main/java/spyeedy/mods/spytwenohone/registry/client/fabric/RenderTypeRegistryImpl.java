package spyeedy.mods.spytwenohone.registry.client.fabric;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

public class RenderTypeRegistryImpl {

	public static void registerBlock(RegistrySupplier<Block> block, RenderType renderType) {
		BlockRenderLayerMap.INSTANCE.putBlock(block.get(), renderType);
	}
}
