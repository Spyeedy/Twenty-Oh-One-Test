package spyeedy.mods.spytwenohone.platform.network;

import net.minecraft.server.level.ServerPlayer;

public interface ServerNetPacketHandler<T extends NetworkPacket> {
	void handle(T msg, ServerPlayer sender);
}
