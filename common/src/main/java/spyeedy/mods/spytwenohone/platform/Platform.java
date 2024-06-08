package spyeedy.mods.spytwenohone.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class Platform {
	@ExpectPlatform
	public static boolean isClient() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static boolean isServer() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static boolean isModLoaded(String id) {
		throw new AssertionError();
	}
}
