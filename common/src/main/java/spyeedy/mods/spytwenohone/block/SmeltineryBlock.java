package spyeedy.mods.spytwenohone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.block.entity.SmeltineryBlockEntity;
import spyeedy.mods.spytwenohone.recipe.SmeltineryRecipe;
import spyeedy.mods.spytwenohone.recipe.SpyTooRecipes;

import java.util.Optional;

public class SmeltineryBlock extends BaseEntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public SmeltineryBlock() {
		super(Properties.of().noOcclusion().lightLevel(state -> state.getValue(LIT) ? 15 : 0));
		registerDefaultState(this.getStateDefinition().any()
				.setValue(FACING, Direction.NORTH)
				.setValue(LIT, false)
		);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide) {
			var blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof SmeltineryBlockEntity) {
				SmeltineryBlockEntity be = (SmeltineryBlockEntity) blockEntity;
				if (player.isCrouching()) {
					Optional<SmeltineryRecipe> recipeMatch = level.getRecipeManager().getRecipeFor(SpyTooRecipes.SMELTINERY_RECIPE.get(), be, level);
					if (recipeMatch.isPresent()) {
						player.sendSystemMessage(Component.literal("Recipe matched!"));
						recipeMatch.get().consumeInputs(be, level);
						return InteractionResult.SUCCESS;
					}
				} else {
					player.openMenu((MenuProvider) blockEntity);
					SpyTwentyOhOne.LOGGER.info("Success opening!");
					return InteractionResult.SUCCESS;
				}
			}
			return InteractionResult.FAIL;
		} else {
			return InteractionResult.SUCCESS;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, LIT);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(LIT, false).setValue(FACING, context.getHorizontalDirection());
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SmeltineryBlockEntity(pos, state);
	}
}
