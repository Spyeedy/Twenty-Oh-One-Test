package spyeedy.mods.spytwenohone.forge;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.SpyTwentyOhOneClient;
import spyeedy.mods.spytwenohone.registry.client.RenderTypeRegistry;
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

	@Mod.EventBusSubscriber(modid = SpyTwentyOhOne.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ModClientEvents {
		@SubscribeEvent
		public static void clientSetup(FMLClientSetupEvent event) {
			RenderTypeRegistry.getBlocks().forEach(renderTypeBlock -> ItemBlockRenderTypes.setRenderLayer(renderTypeBlock.getSecond().get(), renderTypeBlock.getFirst()));
		}
	}
}
