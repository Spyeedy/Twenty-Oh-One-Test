package spyeedy.mods.spytwenohone.network.handler.client;

import net.minecraft.client.Minecraft;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.network.message.server.TestToClientMessage;
import spyeedy.mods.spytwenohone.platform.network.ClientNetPacketHandler;

public class TestToClientHandler implements ClientNetPacketHandler<TestToClientMessage> {
	@Override
	public void handle(TestToClientMessage msg, Minecraft client) {
		SpyTwentyOhOne.LOGGER.info("Water level is {}", msg.getWaterLevel());
	}
}
