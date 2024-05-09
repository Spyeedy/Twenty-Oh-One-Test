package spyeedy.mods.spytwenohone.forge;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.SpyTwentyOhOneClient;

@Mod.EventBusSubscriber(modid = SpyTwentyOhOne.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class SpyTooClientEventHandler {
	@SubscribeEvent
	public static void onKeyInput(InputEvent.Key e) {
		SpyTwentyOhOneClient.keyPress(Minecraft.getInstance(), e.getKey(), e.getScanCode(), e.getAction(), e.getModifiers());
	}
}
