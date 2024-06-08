package spyeedy.mods.spytwenohone.platform.fabric;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import spyeedy.mods.spytwenohone.platform.NetworkManager;
import spyeedy.mods.spytwenohone.platform.network.ClientNetPacketHandler;
import spyeedy.mods.spytwenohone.platform.network.DecodeBytes;
import spyeedy.mods.spytwenohone.platform.network.NetworkPacket;
import spyeedy.mods.spytwenohone.platform.network.ServerNetPacketHandler;

public class NetworkManagerImpl extends NetworkManager {
	public static NetworkManager create(String modid) {
		return new NetworkManagerImpl(modid);
	}

	protected NetworkManagerImpl(String modid) {
		super(modid);
	}

	@Override
	public <M extends NetworkPacket> void registerClientBound(String msgId, Class<M> msgClz, DecodeBytes<M> decoder, ClientNetPacketHandler<M> packetHandler) {
		ClientPlayNetworking.registerGlobalReceiver(new ResourceLocation(modid, msgId), (client, handler, buf, responseSender) -> {
			var msg = decoder.decode(buf);

			client.execute(() -> packetHandler.handle(msg, client));
		});
	}

	@Override
	public <M extends NetworkPacket> void registerServerBound(String msgId, Class<M> msgClz, DecodeBytes<M> decoder, ServerNetPacketHandler<M> packetHandler) {
		ServerPlayNetworking.registerGlobalReceiver(new ResourceLocation(modid, msgId), (server, player, handler, buf, responseSender) -> {
			var msg = decoder.decode(buf);

			server.execute(() -> packetHandler.handle(msg, player));
		});
	}

	@Override
	public void sendToServer(NetworkPacket packet) {
		var packetBytes = PacketByteBufs.create();
		packet.encode(packetBytes);

		ClientPlayNetworking.send(new ResourceLocation(modid, packet.getId()), PacketByteBufs.empty());
	}

	@Override
	public void sendToClient(ServerPlayer player, NetworkPacket packet) {
		var packetBytes = PacketByteBufs.create();
		packet.encode(packetBytes);

		ServerPlayNetworking.send(player, new ResourceLocation(modid, packet.getId()), packetBytes);
	}
}
