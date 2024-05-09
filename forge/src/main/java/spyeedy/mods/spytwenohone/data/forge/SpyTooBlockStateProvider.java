package spyeedy.mods.spytwenohone.data.forge;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.block.SpyTooBlocks;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

import java.util.function.BiConsumer;

public class SpyTooBlockStateProvider extends BlockStateProvider {

	public SpyTooBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, SpyTwentyOhOne.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		complexBlock(SpyTooBlocks.SMELT_FINERY, (block, builder) -> {
			ModelFile smeltFineryModel = models().getExistingFile(new ResourceLocation(SpyTwentyOhOne.MOD_ID, "block/" + block.getId().getPath()));

			builder.forAllStates(state -> ConfiguredModel.builder()
					.modelFile(smeltFineryModel)
					.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
					.build());
		});
	}

	public void complexBlock(RegistrySupplier<Block> block, BiConsumer<RegistrySupplier<Block>, VariantBlockStateBuilder> builderConsumer) {
		builderConsumer.accept(block, getVariantBuilder(block.get()));
	}
}
