package spyeedy.mods.spytwenohone.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface SpyTooJSEvents {
	EventGroup GROUP = EventGroup.of("SpyTooEvents");

	EventHandler REGISTER_SCREENS = GROUP.client("registerScreens", () -> RegisterScreenEventJS.class);
}
