package spyeedy.mods.spytwenohone.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ServerPlayer;
import spyeedy.mods.spytwenohone.platform.network.ClientNetPacketHandler;
import spyeedy.mods.spytwenohone.platform.network.DecodeBytes;
import spyeedy.mods.spytwenohone.platform.network.NetworkPacket;
import spyeedy.mods.spytwenohone.platform.network.ServerNetPacketHandler;

public abstract class NetworkManager {
	/*
	 * Register client and server packets separately
	 */

	/*
	 * Maintain two maps of server-bound and client-bound packets
	 * A packet's encoder & decoders
	 */

	protected final String modid;

	/**
	 * @param modid The network channel that the mod's messages will communicate on. Typically, this would be your mod id.
	 */
	protected NetworkManager(String modid) {
		this.modid = modid;
	}

	public abstract <M extends NetworkPacket> void registerClientBound(String msgId, Class<M> msgClz, DecodeBytes<M> decoder, ClientNetPacketHandler<M> packetHandler);

	public abstract <M extends NetworkPacket> void registerServerBound(String msgId, Class<M> msgClz, DecodeBytes<M> decoder, ServerNetPacketHandler<M> packetHandler);

	public abstract void sendToServer(NetworkPacket packet);

	public abstract void sendToClient(ServerPlayer player, NetworkPacket packet);

	@ExpectPlatform
	public static NetworkManager create(String modid) {
		throw new AssertionError();
	}
}
