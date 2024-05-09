package spyeedy.mods.spytwenohone.kubejs;

import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.resources.ResourceLocation;
import spyeedy.mods.spytwenohone.client.ScriptableScreenManager;
import spyeedy.mods.spytwenohone.client.screen.ScriptableScreenImpl;

public class RegisterScreenEventJS extends EventJS {
	public ScriptableScreenImpl.Builder register(String id, int width, int height) {
		ScriptableScreenImpl.Builder builder = new ScriptableScreenImpl.Builder(width, height);

		ScriptableScreenManager.INSTANCE.registerScreen(new ResourceLocation(id), builder);

		return builder;
	}
}
