package spyeedy.mods.spytwenohone.network.message.server;

import net.minecraft.network.FriendlyByteBuf;
import spyeedy.mods.spytwenohone.platform.network.NetworkPacket;

public class TestToClientMessage implements NetworkPacket {
	public static final String ID = "test_s2c";

	private int waterLevel;

	public TestToClientMessage(int waterLevel) {
		this.waterLevel = waterLevel;
	}

	@Override
	public String getId() {
		return ID;
	}

	public int getWaterLevel() {
		return waterLevel;
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(waterLevel);
	}

	public static TestToClientMessage decode(FriendlyByteBuf buffer) {
		return new TestToClientMessage(buffer.readInt());
	}
}
