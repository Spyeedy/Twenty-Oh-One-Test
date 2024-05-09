package spyeedy.mods.spytwenohone.forge;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.SpyTwentyOhOneClient;
import spyeedy.mods.spytwenohone.util.Platform;

@Mod(SpyTwentyOhOne.MOD_ID)
public class SpyTwentyOhOneForge {
	private static IEventBus MOD_BUS = null;

	public SpyTwentyOhOneForge() {
		MOD_BUS = FMLJavaModLoadingContext.get().getModEventBus();

		// Run our common setup.
		SpyTwentyOhOne.init();

		if (Platform.isClient()) {
			SpyTwentyOhOneClient.init();
		}
	}

	public static IEventBus getModBus() {
		return MOD_BUS;
	}
}
