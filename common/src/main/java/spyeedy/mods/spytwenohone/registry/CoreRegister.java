package spyeedy.mods.spytwenohone.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public abstract class CoreRegister<T> {
	// Send the registered <T> to the respective mod loaders to be registered with the game
	public abstract void submit();

	public abstract <R extends T> RegistrySupplier<R> register(String id, Supplier<R> supplier);

	@ExpectPlatform
	public static <T> CoreRegister<T> create(String modid, ResourceKey<? extends Registry<T>> resourceKey) {
		throw new AssertionError();
	}
}
