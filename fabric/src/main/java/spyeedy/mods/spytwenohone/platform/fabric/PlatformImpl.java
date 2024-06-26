package spyeedy.mods.spytwenohone.platform.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class PlatformImpl {
	public static boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	public static boolean isServer() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
	}

	public static boolean isModLoaded(String id) {
		return FabricLoader.getInstance().isModLoaded(id);
	}
}
