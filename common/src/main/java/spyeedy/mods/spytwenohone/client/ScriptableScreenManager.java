package spyeedy.mods.spytwenohone.client;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.client.screen.ScriptableScreenImpl;
import spyeedy.mods.spytwenohone.kubejs.SpyTooJSEvents;
import spyeedy.mods.spytwenohone.kubejs.event.RegisterScreenEventJS;

import java.util.HashMap;
import java.util.Map;

public class ScriptableScreenManager extends SimplePreparableReloadListener<String> {

    public static ScriptableScreenManager INSTANCE = new ScriptableScreenManager();

    private final Map<ResourceLocation, ScriptableScreenImpl.Builder> screens = new HashMap<>();

    @Override
    protected String prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        return null;
    }

    @Override
    protected void apply(String object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.screens.clear();

        SpyTooJSEvents.REGISTER_SCREENS.post(new RegisterScreenEventJS());
        SpyTwentyOhOne.LOGGER.info("Loaded {} screens", screens.size());
    }

    public void registerScreen(ResourceLocation id, ScriptableScreenImpl.Builder builderConsumer) {
        if (id.getNamespace().isBlank())
            id = new ResourceLocation(SpyTwentyOhOne.MOD_ID, id.getPath());

        if (!screens.containsKey(id))
            screens.put(id, builderConsumer);
        else {
            ConsoleJS.CLIENT.log("Screen id %s is taken, please choose a different id", id);
        }
    }

    @Nullable
    public ScriptableScreenImpl getScreen(String id) {
        return getScreen(new ResourceLocation(id));
    }

    @Nullable
    public ScriptableScreenImpl getScreen(ResourceLocation id) {
        if (id.getNamespace().isBlank())
            id = new ResourceLocation(SpyTwentyOhOne.MOD_ID, id.getPath());

        ScriptableScreenImpl.Builder builderConsumer = screens.getOrDefault(id, null);

        if (builderConsumer == null) return null;

        return builderConsumer.build();
    }
}
