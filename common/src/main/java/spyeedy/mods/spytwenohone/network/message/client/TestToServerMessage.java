package spyeedy.mods.spytwenohone.network.message.client;

import net.minecraft.network.FriendlyByteBuf;
import spyeedy.mods.spytwenohone.platform.network.NetworkPacket;

public class TestToServerMessage implements NetworkPacket {
	public static String ID = "test_c2s";

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {}
}
