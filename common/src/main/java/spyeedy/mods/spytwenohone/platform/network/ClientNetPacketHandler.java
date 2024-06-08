package spyeedy.mods.spytwenohone.platform.network;

import net.minecraft.client.Minecraft;

public interface ClientNetPacketHandler<T extends NetworkPacket> {
	void handle(T msg, Minecraft client);
}
