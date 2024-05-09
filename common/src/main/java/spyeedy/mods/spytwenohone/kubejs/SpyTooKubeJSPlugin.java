package spyeedy.mods.spytwenohone.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class SpyTooKubeJSPlugin extends KubeJSPlugin {

	@Override
	public void init() {
		SpyTooJSEvents.GROUP.register();
	}

	@Override
	public void registerBindings(BindingsEvent event) {
		super.registerBindings(event);
	}
}
