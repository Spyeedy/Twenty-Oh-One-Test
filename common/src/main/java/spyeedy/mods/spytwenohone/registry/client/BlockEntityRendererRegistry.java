package spyeedy.mods.spytwenohone.registry.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

public class BlockEntityRendererRegistry {
	@ExpectPlatform
	public static <T extends BlockEntity> void register(RegistrySupplier<BlockEntityType<T>> entityType, BlockEntityRendererProvider<T> provider) {
		throw new AssertionError();
	}
}
