package spyeedy.mods.spytwenohone.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import spyeedy.mods.spytwenohone.SpyTwentyOhOneClient;
import spyeedy.mods.spytwenohone.registry.client.RenderTypeRegistry;

public final class SpyTwentyOhOneFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		SpyTwentyOhOneClient.init();

		RenderTypeRegistry.getBlocks().forEach(renderTypeBlock -> BlockRenderLayerMap.INSTANCE.putBlock(renderTypeBlock.getSecond().get(), renderTypeBlock.getFirst()));
	}
}
