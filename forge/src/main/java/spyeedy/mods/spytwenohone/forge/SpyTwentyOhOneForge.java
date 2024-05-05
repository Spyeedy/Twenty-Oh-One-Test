package spyeedy.mods.spytwenohone.forge;

import net.minecraftforge.fml.common.Mod;

import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.SpyTwentyOhOneClient;
import spyeedy.mods.spytwenohone.util.Platform;

@Mod(SpyTwentyOhOne.MOD_ID)
public final class SpyTwentyOhOneForge {
    public SpyTwentyOhOneForge() {
        // Run our common setup.
        SpyTwentyOhOne.init();

        if (Platform.isClient()) {
            SpyTwentyOhOneClient.init();
        }
    }
}
