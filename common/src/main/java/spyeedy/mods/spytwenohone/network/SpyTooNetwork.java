package spyeedy.mods.spytwenohone.network;

import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.network.handler.server.TestToServerHandler;
import spyeedy.mods.spytwenohone.platform.NetworkManager;
import spyeedy.mods.spytwenohone.network.message.client.TestToServerMessage;
import spyeedy.mods.spytwenohone.network.handler.client.DisplayOpenCountHandler;
import spyeedy.mods.spytwenohone.network.message.server.DisplayOpenCountMessage;


public class SpyTooNetwork {
	public static NetworkManager NETWORK = NetworkManager.create(SpyTwentyOhOne.MOD_ID);

	public static void registerClientPackets() {
		NETWORK.registerClientBound(DisplayOpenCountMessage.ID, DisplayOpenCountMessage.class, DisplayOpenCountMessage::decode, new DisplayOpenCountHandler());
	}

	public static void registerServerPackets() {
		NETWORK.registerServerBound(TestToServerMessage.ID, TestToServerMessage.class, (buf) -> new TestToServerMessage(), new TestToServerHandler());
	}
}
