package spyeedy.mods.spytwenohone;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.packs.PackType;
import spyeedy.mods.spytwenohone.client.ScriptableScreenManager;

public class SpyTwentyOhOneClient {
    public static void init() {
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, ScriptableScreenManager.INSTANCE);
    }

    public static void keyPress(Minecraft minecraft, int key, int scanCode, int action, int modifiers) {
        if (minecraft == null || minecraft.screen != null) return;

        if (key == InputConstants.KEY_R && action == InputConstants.PRESS) {
            Screen screen = ScriptableScreenManager.INSTANCE.getScreen("test_screen_1");
            if (screen != null) {
                minecraft.setScreen(screen);
            }
        }
    }
}
