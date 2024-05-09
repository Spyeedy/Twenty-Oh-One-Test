package spyeedy.mods.spytwenohone.registry.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

@Environment(EnvType.CLIENT)
public class RenderTypeRegistry {

	@ExpectPlatform
	public static void registerBlock(RegistrySupplier<Block> block, RenderType renderType) {
		throw new AssertionError();
	}
}
