package spyeedy.mods.spytwenohone.platform.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import spyeedy.mods.spytwenohone.platform.NetworkManager;
import spyeedy.mods.spytwenohone.platform.network.*;
import spyeedy.mods.spytwenohone.util.Platform;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class NetworkManagerImpl extends NetworkManager {
	public static NetworkManager create(String modid) {
		return new NetworkManagerImpl(modid);
	}

	private static final String PROTOCOL_VERSION = "1";

	private final SimpleChannel channel;
	private int id = 0;

	protected NetworkManagerImpl(String modid) {
		super(modid);
		channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(modid, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	}

	@Override
	public <M extends NetworkPacket> void registerClientBound(String msgId, Class<M> msgClz, DecodeBytes<M> decoder, ClientNetPacketHandler<M> packetHandler) {
		this.registerPacket(msgClz, decoder, (msg, ctx) -> {
			var context = ctx.get();
			context.enqueueWork(() -> {
				if (Platform.isClient()) {
					packetHandler.handle(msg, Minecraft.getInstance());
				}
			});
			context.setPacketHandled(true);
		});
	}

	@Override
	public <M extends NetworkPacket> void registerServerBound(String msgId, Class<M> msgClz, DecodeBytes<M> decoder, ServerNetPacketHandler<M> packetHandler) {
		// id, message class, encode data into bytes, decode bytes into data
		this.registerPacket(msgClz, decoder, (msg, ctx) -> {
			var context = ctx.get();
			context.enqueueWork(() -> packetHandler.handle(msg, context.getSender()));
			context.setPacketHandled(true);
		});
	}

	private <M extends NetworkPacket> void registerPacket(Class<M> msgClz, DecodeBytes<M> decoder, BiConsumer<M, Supplier<NetworkEvent.Context>> messageHandler) {
		this.channel.registerMessage(id++, msgClz, EncodeBytes::encode, decoder::decode, messageHandler);
	}

	@Override
	public void sendToServer(NetworkPacket packet) {
		channel.sendToServer(packet);
	}

	@Override
	public void sendToClient(ServerPlayer player, NetworkPacket packet) {
		channel.send(PacketDistributor.PLAYER.with(() -> player), packet);
	}
}
