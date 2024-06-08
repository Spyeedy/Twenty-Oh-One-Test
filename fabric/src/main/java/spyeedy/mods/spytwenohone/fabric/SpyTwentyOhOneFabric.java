package spyeedy.mods.spytwenohone.fabric;

import net.fabricmc.api.ModInitializer;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.network.SpyTooNetwork;

public final class SpyTwentyOhOneFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Run our common setup.
		SpyTwentyOhOne.init();
		SpyTooNetwork.registerServerPackets();
	}
}
