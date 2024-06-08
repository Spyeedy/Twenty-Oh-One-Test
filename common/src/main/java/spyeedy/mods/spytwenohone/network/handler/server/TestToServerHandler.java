package spyeedy.mods.spytwenohone.network.handler.server;

import net.minecraft.server.level.ServerPlayer;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.network.message.client.TestToServerMessage;
import spyeedy.mods.spytwenohone.platform.network.ServerNetPacketHandler;

public class TestToServerHandler implements ServerNetPacketHandler<TestToServerMessage> {
	@Override
	public void handle(TestToServerMessage msg, ServerPlayer sender) {
		SpyTwentyOhOne.LOGGER.info("Server received");
	}
}
