package spyeedy.mods.spytwenohone.platform.network;

import net.minecraft.network.FriendlyByteBuf;

public interface DecodeBytes<T> {
	T decode(FriendlyByteBuf buffer);
}
