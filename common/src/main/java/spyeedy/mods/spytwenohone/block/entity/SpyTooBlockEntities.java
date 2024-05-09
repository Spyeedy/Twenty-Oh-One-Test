package spyeedy.mods.spytwenohone.block.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.block.SpyTooBlocks;
import spyeedy.mods.spytwenohone.registry.CoreRegister;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

public class SpyTooBlockEntities {
	public static final CoreRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = CoreRegister.create(SpyTwentyOhOne.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

	public static final RegistrySupplier<BlockEntityType<SmeltineryBlockEntity>> SMELTINERY = BLOCK_ENTITY_TYPES.register("smeltinery", () -> BlockEntityType.Builder.of(SmeltineryBlockEntity::new, SpyTooBlocks.SMELT_FINERY.get()).build(null));
}
