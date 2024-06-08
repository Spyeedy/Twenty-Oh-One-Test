package spyeedy.mods.spytwenohone.platform.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class PlatformImpl {
	public static boolean isClient() {
		return FMLEnvironment.dist == Dist.CLIENT;
	}

	public static boolean isServer() {
		return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
	}

	public static boolean isModLoaded(String id) {
		return ModList.get().isLoaded(id);
	}
}
