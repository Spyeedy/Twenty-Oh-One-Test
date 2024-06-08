package spyeedy.mods.spytwenohone.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import spyeedy.mods.spytwenohone.SpyTwentyOhOneClient;
import spyeedy.mods.spytwenohone.network.SpyTooNetwork;

public final class SpyTwentyOhOneFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		SpyTwentyOhOneClient.init();

		SpyTwentyOhOneClient.registerRenderTypes();
		SpyTwentyOhOneClient.registerMenuScreens();
		SpyTwentyOhOneClient.registerEntityRenderers();

		SpyTooNetwork.registerClientPackets();
	}
}
