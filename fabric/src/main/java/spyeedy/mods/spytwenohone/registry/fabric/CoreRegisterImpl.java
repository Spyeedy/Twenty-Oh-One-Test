package spyeedy.mods.spytwenohone.registry.fabric;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import spyeedy.mods.spytwenohone.registry.CoreRegister;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CoreRegisterImpl {
	public static <T> CoreRegister<T> create(String modid, ResourceKey<? extends Registry<T>> resourceKey) {
		return new Impl<>(modid, resourceKey);
	}

	public static class Impl<T> extends CoreRegister<T> {

		private final String modid;
		private final Registry<T> registry;
		private final List<RegistrySupplier<T>> entries;

		public Impl(String modid, ResourceKey<? extends Registry<T>> resourceKey) {
			this.modid = modid;
			this.registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(resourceKey.location());
			this.entries = new ArrayList<>();
		}

		@Override
		public void submit() {
			for (RegistrySupplier<T> sup : entries) {
				Registry.register(this.registry, sup.getId(), sup.get());
			}
		}

		@Override
		public <R extends T> RegistrySupplier<R> register(String id, Supplier<R> supplier) {
			ResourceLocation registeredId = new ResourceLocation(modid, id);
			RegistrySupplier<R> registrySupplier = new RegistrySupplier<>(registeredId, supplier.get() /*Registry.register(this.registry, registeredId, supplier.get())*/);
			this.entries.add((RegistrySupplier<T>) registrySupplier);
			return registrySupplier;
		}
	}
}
