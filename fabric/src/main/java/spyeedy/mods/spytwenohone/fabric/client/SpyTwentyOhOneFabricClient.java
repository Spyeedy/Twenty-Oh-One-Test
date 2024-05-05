package spyeedy.mods.spytwenohone.fabric.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import spyeedy.mods.spytwenohone.SpyTwentyOhOneClient;

public final class SpyTwentyOhOneFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        SpyTwentyOhOneClient.init();
    }
}
