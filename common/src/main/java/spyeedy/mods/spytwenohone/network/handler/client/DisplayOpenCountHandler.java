package spyeedy.mods.spytwenohone.network.handler.client;

import net.minecraft.client.Minecraft;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.network.message.server.DisplayOpenCountMessage;
import spyeedy.mods.spytwenohone.platform.network.ClientNetPacketHandler;

public class DisplayOpenCountHandler implements ClientNetPacketHandler<DisplayOpenCountMessage> {
	@Override
	public void handle(DisplayOpenCountMessage msg, Minecraft client) {
		SpyTwentyOhOne.LOGGER.info("Open count is {}", msg.getOpenCount());
	}
}
