package spyeedy.mods.spytwenohone.registry.forge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DeferredRegister;
import spyeedy.mods.spytwenohone.forge.SpyTwentyOhOneForge;
import spyeedy.mods.spytwenohone.registry.CoreRegister;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CoreRegisterImpl {

	public static <T> CoreRegister<T> create(String modid, ResourceKey<? extends Registry<T>> resourceKey) {
		return new Impl<>(modid, resourceKey);
	}

	@SuppressWarnings({"unchecked", "MismatchedQueryAndUpdateOfCollection"})
	public static class Impl<T> extends CoreRegister<T> {

		private final DeferredRegister<T> registry;
		private final List<RegistrySupplier<T>> entries;

		public Impl(String modid, ResourceKey<? extends Registry<T>> resourceKey) {
			this.registry = DeferredRegister.create(resourceKey, modid);
			this.entries = new ArrayList<>();
		}

		@Override
		public void submit() {
			this.registry.register(SpyTwentyOhOneForge.getModBus());
		}

		@Override
		public <R extends T> RegistrySupplier<R> register(String id, Supplier<R> supplier) {
			// forge gives us back the supplier, so we shall use that to pass to our RegistrySupplier
			var forgeReg = this.registry.register(id, supplier);
			// Create our own wrapper to safely store the raw T value without it being lost to garbage collection
			var registrySupplier = new RegistrySupplier<>(forgeReg.getId(), forgeReg);
			this.entries.add((RegistrySupplier<T>) registrySupplier);
			return registrySupplier;
		}
	}
}
