package spyeedy.mods.spytwenohone.registry.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = SpyTwentyOhOne.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReloadListenerRegistryImpl {

	public static List<PreparableReloadListener> SERVER_LISTENERS = new ArrayList<>();
	public static List<PreparableReloadListener> CLIENT_LISTENERS = new ArrayList<>();

	public static void register(PackType packType, ResourceLocation id, PreparableReloadListener listener) {
		switch (packType) {
			case CLIENT_RESOURCES -> CLIENT_LISTENERS.add(listener);
			case SERVER_DATA -> SERVER_LISTENERS.add(listener);
		}
	}

	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent e) {
		SERVER_LISTENERS.forEach(e::addListener);
	}

	@Mod.EventBusSubscriber(modid = SpyTwentyOhOne.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class Client {
		@SubscribeEvent
		public static void addReloadListeners(RegisterClientReloadListenersEvent e) {
			CLIENT_LISTENERS.forEach(e::registerReloadListener);
		}
	}
}
