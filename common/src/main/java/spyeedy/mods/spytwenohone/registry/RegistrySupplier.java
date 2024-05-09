package spyeedy.mods.spytwenohone.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class RegistrySupplier<T> implements Supplier<T> {

	private final ResourceLocation id;
	private T raw;
	private Supplier<T> supplier;

	public RegistrySupplier(ResourceLocation id, Supplier<T> supplier) {
		this.id = id;
		this.supplier = supplier;
	}

	public RegistrySupplier(ResourceLocation id, T raw) {
		this.id = id;
		this.raw = raw;
	}

	public ResourceLocation getId() {
		return id;
	}

	@Override
	public T get() {
		return this.raw != null ? raw : this.supplier.get();
	}
}
