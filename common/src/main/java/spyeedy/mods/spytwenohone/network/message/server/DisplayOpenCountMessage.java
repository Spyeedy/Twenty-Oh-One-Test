package spyeedy.mods.spytwenohone.network.message.server;

import net.minecraft.network.FriendlyByteBuf;
import spyeedy.mods.spytwenohone.platform.network.NetworkPacket;

public class DisplayOpenCountMessage implements NetworkPacket {
	public static final String ID = "test_s2c";

	private int openCount;

	public DisplayOpenCountMessage(int openCount) {
		this.openCount = openCount;
	}

	@Override
	public String getId() {
		return ID;
	}

	public int getOpenCount() {
		return openCount;
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(openCount);
	}

	public static DisplayOpenCountMessage decode(FriendlyByteBuf buffer) {
		return new DisplayOpenCountMessage(buffer.readInt());
	}
}
