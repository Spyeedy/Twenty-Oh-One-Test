package spyeedy.mods.spytwenohone.registry.client.fabric;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

public class BlockEntityRendererRegistryImpl {

	public static <T extends BlockEntity> void register(RegistrySupplier<BlockEntityType<T>> entityType, BlockEntityRendererProvider<T> provider) {
		BlockEntityRenderers.register(entityType.get(), provider);
	}
}
