package spyeedy.mods.spytwenohone.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.registry.CoreRegister;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

public class SpyTooBlocks {

	public static final CoreRegister<Block> BLOCKS = CoreRegister.create(SpyTwentyOhOne.MOD_ID, Registries.BLOCK);

	public static final RegistrySupplier<Block> SMELT_FINERY = BLOCKS.register("smeltinery", SmeltineryBlock::new);
}
