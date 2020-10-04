package net.ludocrypt.carvepump.blocks;

import net.ludocrypt.carvepump.blocks.entity.CarvedBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class CarvableBlock extends BlockWithEntity {

	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

	public CarvableBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(
				(BlockState) ((BlockState) this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return (BlockState) this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new CarvedBlockEntity();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	public Block getCarvingBlock() {
		return Blocks.PUMPKIN;
	}

}
