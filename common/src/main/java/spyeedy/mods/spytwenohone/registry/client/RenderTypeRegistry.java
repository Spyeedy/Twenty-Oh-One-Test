package spyeedy.mods.spytwenohone.registry.client;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class RenderTypeRegistry {
	private static final List<Pair<RenderType, RegistrySupplier<Block>>> BLOCK_RENDER_TYPES = new ArrayList<>();

	public static void registerBlock(RenderType renderType, RegistrySupplier<Block> block) {
		BLOCK_RENDER_TYPES.add(Pair.of(renderType, block));
	}

	public static Collection<Pair<RenderType, RegistrySupplier<Block>>> getBlocks() {
		return Collections.unmodifiableList(BLOCK_RENDER_TYPES);
	}
}
