package spyeedy.mods.spytwenohone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.network.message.client.TestToServerMessage;
import spyeedy.mods.spytwenohone.network.message.server.TestToClientMessage;
import spyeedy.mods.spytwenohone.block.entity.SmeltineryBlockEntity;
import spyeedy.mods.spytwenohone.block.entity.SpyTooBlockEntities;
import spyeedy.mods.spytwenohone.network.SpyTooNetwork;

public class SmeltineryBlock extends BaseEntityBlock {

	private static final VoxelShape SHAPE;

	static {
		SHAPE = Shapes.or(
			box(0.0, 3.0, 0.0, 16.0, 16.0, 16.0),
			// NW leg
			legShape(false), legShape(true).move(0,0, 0.125),
			// SW leg
			legShape(false).move(0, 0, 0.875), legShape(true).move(0,0, 0.75),

			// NE leg
			legShape(false).move(0.75, 0, 0), legShape(true).move(0.875, 0, 0.125),

			// SE leg
			legShape(false).move(0.75, 0, 0.875), legShape(true).move(0.875, 0, 0.75)
		);
	}

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public SmeltineryBlock() {
		super(Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.5f).noOcclusion().lightLevel(state -> state.getValue(LIT) ? 15 : 0));
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
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide) {
			BlockEntity blockEntity = level.getBlockEntity(pos);

			if (blockEntity instanceof SmeltineryBlockEntity) {
				ItemStack heldStack = player.getItemInHand(hand);
				SmeltineryBlockEntity be = (SmeltineryBlockEntity) blockEntity;
				Direction blockFacing = state.getValue(FACING);

				if ((heldStack.getItem() == Items.BUCKET || heldStack.getItem() == Items.WATER_BUCKET) && (hit.getDirection() == blockFacing.getCounterClockWise() || hit.getDirection() == blockFacing.getClockWise() || hit.getDirection() == blockFacing.getOpposite())) {
					if (heldStack.getItem() == Items.WATER_BUCKET && be.addFluid(SmeltineryBlockEntity.FLUID_PER_BUCKET, true)) {
						if (!player.isCreative()) {
							player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());
						}
						return InteractionResult.CONSUME;
					} else if (heldStack.getItem() == Items.BUCKET && be.removeFluid(SmeltineryBlockEntity.FLUID_PER_BUCKET, true)) {
						if (!player.isCreative()) {
							heldStack.shrink(1);
							player.addItem(Items.WATER_BUCKET.getDefaultInstance());
						}
						return InteractionResult.CONSUME;
					}
				}
				player.openMenu(be);
				SpyTwentyOhOne.LOGGER.info("Success opening!");

				if (player instanceof ServerPlayer)
					SpyTooNetwork.NETWORK.sendToClient((ServerPlayer) player, new TestToClientMessage(be.getFluidAmount()));

				return InteractionResult.SUCCESS;
			}
			return InteractionResult.FAIL;
		} else {
			SpyTooNetwork.NETWORK.sendToServer(new TestToServerMessage());
			return InteractionResult.SUCCESS;
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof SmeltineryBlockEntity) {
				SmeltineryBlockEntity be = (SmeltineryBlockEntity) blockEntity;
				if (!level.isClientSide) {
					Containers.dropContents(level, pos, be);
				}
			}
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
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

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return level.isClientSide ? null : createTickerHelper(blockEntityType, SpyTooBlockEntities.SMELTINERY.get(), SmeltineryBlockEntity::serverTick);
//		return null;
	}

	private static VoxelShape legShape(boolean isPillar) {
		if (isPillar) {
			return box(0.0, 0.0, 0.0, 2.0, 3.0, 2.0);
		}
		return box(0.0, 0.0, 0.0, 4.0, 3.0, 2.0);
	}
}
