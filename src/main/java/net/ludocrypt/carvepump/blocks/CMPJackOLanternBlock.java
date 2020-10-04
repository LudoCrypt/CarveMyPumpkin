package net.ludocrypt.carvepump.blocks;

import java.util.Iterator;
import java.util.function.Predicate;

import org.eclipse.jdt.annotation.Nullable;

import net.ludocrypt.carvepump.CarveMyPumpkin;
import net.ludocrypt.carvepump.blocks.entity.LitCarvedBlockEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.function.MaterialPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CMPJackOLanternBlock extends BlockWithEntity {

	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

	@Nullable
	private BlockPattern snowGolemDispenserPattern;
	@Nullable
	private BlockPattern snowGolemPattern;
	@Nullable
	private BlockPattern ironGolemDispenserPattern;
	@Nullable
	private BlockPattern ironGolemPattern;
	private static final Predicate<BlockState> IS_GOLEM_HEAD_PREDICATE = (state) -> {
		return state != null && state.isOf(CarveMyPumpkin.JACK_O_LANTERN);
	};

	public CMPJackOLanternBlock() {
		super(AbstractBlock.Settings.of(Material.GOURD, MaterialColor.ORANGE).strength(1.0F)
				.sounds(BlockSoundGroup.WOOD).luminance((state) -> {
					return 15;
				}));
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
		return new LitCarvedBlockEntity();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof LitCarvedBlockEntity) {
			LitCarvedBlockEntity jackOLanternBlockEntity = (LitCarvedBlockEntity) blockEntity;
			if (!world.isClient && !jackOLanternBlockEntity.isUncarved()) {

				ItemStack jack_o_lantern = new ItemStack(
						!player.isCreative() ? CarveMyPumpkin.CARVED_PUMPKIN : CarveMyPumpkin.JACK_O_LANTERN);

				CompoundTag compoundTag = jackOLanternBlockEntity.serializeCarving(new CompoundTag());

				if (!compoundTag.isEmpty()) {
					jack_o_lantern.putSubTag("BlockEntityTag", compoundTag);
				}

				ItemEntity jack_o_lantern_entity = new ItemEntity(world, (double) pos.getX() + 0.5D,
						(double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, jack_o_lantern);
				jack_o_lantern_entity.setToDefaultPickupDelay();
				world.spawnEntity(jack_o_lantern_entity);

				if (!player.isCreative()) {
					ItemStack torch = new ItemStack(Items.TORCH);
					ItemEntity torch_entity = new ItemEntity(world, (double) pos.getX() + 0.5D,
							(double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, torch);
					torch_entity.setToDefaultPickupDelay();
					world.spawnEntity(torch_entity);
				}
			}
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof LitCarvedBlockEntity) {
			LitCarvedBlockEntity jackOLanternBlockEntity = (LitCarvedBlockEntity) blockEntity;
			if (itemStack.hasTag()) {
				jackOLanternBlockEntity.deserializeCarving(itemStack.getSubTag("BlockEntityTag"));
			}
		}
	}

	@Override
	public String getTranslationKey() {
		return Blocks.JACK_O_LANTERN.getTranslationKey();
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		ItemStack itemStack = new ItemStack(CarveMyPumpkin.JACK_O_LANTERN);
		CompoundTag compoundTag = ((LitCarvedBlockEntity) world.getBlockEntity(pos))
				.serializeCarving(new CompoundTag());
		if (!compoundTag.isEmpty()) {
			itemStack.putSubTag("BlockEntityTag", compoundTag);
		}
		return itemStack;
	}

	// Just Pumpkin Stuff

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!oldState.isOf(state.getBlock())) {
			this.trySpawnEntity(world, pos);
		}
	}

	private void trySpawnEntity(World world, BlockPos pos) {
		BlockPattern.Result result = this.getSnowGolemPattern().searchAround(world, pos);
		int k;
		Iterator<ServerPlayerEntity> var6;
		ServerPlayerEntity serverPlayerEntity2;
		int m;
		if (result != null) {
			for (k = 0; k < this.getSnowGolemPattern().getHeight(); ++k) {
				CachedBlockPosition cachedBlockPosition = result.translate(0, k, 0);
				world.setBlockState(cachedBlockPosition.getBlockPos(), Blocks.AIR.getDefaultState(), 2);
				world.syncWorldEvent(2001, cachedBlockPosition.getBlockPos(),
						Block.getRawIdFromState(cachedBlockPosition.getBlockState()));
			}

			SnowGolemEntity snowGolemEntity = (SnowGolemEntity) EntityType.SNOW_GOLEM.create(world);
			BlockPos blockPos = result.translate(0, 2, 0).getBlockPos();
			snowGolemEntity.refreshPositionAndAngles((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.05D,
					(double) blockPos.getZ() + 0.5D, 0.0F, 0.0F);
			world.spawnEntity(snowGolemEntity);
			var6 = world
					.getNonSpectatingEntities(ServerPlayerEntity.class, snowGolemEntity.getBoundingBox().expand(5.0D))
					.iterator();

			while (var6.hasNext()) {
				serverPlayerEntity2 = (ServerPlayerEntity) var6.next();
				Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity2, snowGolemEntity);
			}

			for (m = 0; m < this.getSnowGolemPattern().getHeight(); ++m) {
				CachedBlockPosition cachedBlockPosition2 = result.translate(0, m, 0);
				world.updateNeighbors(cachedBlockPosition2.getBlockPos(), Blocks.AIR);
			}
		} else {
			result = this.getIronGolemPattern().searchAround(world, pos);
			if (result != null) {
				for (k = 0; k < this.getIronGolemPattern().getWidth(); ++k) {
					for (int l = 0; l < this.getIronGolemPattern().getHeight(); ++l) {
						CachedBlockPosition cachedBlockPosition3 = result.translate(k, l, 0);
						world.setBlockState(cachedBlockPosition3.getBlockPos(), Blocks.AIR.getDefaultState(), 2);
						world.syncWorldEvent(2001, cachedBlockPosition3.getBlockPos(),
								Block.getRawIdFromState(cachedBlockPosition3.getBlockState()));
					}
				}

				BlockPos blockPos2 = result.translate(1, 2, 0).getBlockPos();
				IronGolemEntity ironGolemEntity = (IronGolemEntity) EntityType.IRON_GOLEM.create(world);
				ironGolemEntity.setPlayerCreated(true);
				ironGolemEntity.refreshPositionAndAngles((double) blockPos2.getX() + 0.5D,
						(double) blockPos2.getY() + 0.05D, (double) blockPos2.getZ() + 0.5D, 0.0F, 0.0F);
				world.spawnEntity(ironGolemEntity);
				var6 = world.getNonSpectatingEntities(ServerPlayerEntity.class,
						ironGolemEntity.getBoundingBox().expand(5.0D)).iterator();

				while (var6.hasNext()) {
					serverPlayerEntity2 = (ServerPlayerEntity) var6.next();
					Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity2, ironGolemEntity);
				}

				for (m = 0; m < this.getIronGolemPattern().getWidth(); ++m) {
					for (int n = 0; n < this.getIronGolemPattern().getHeight(); ++n) {
						CachedBlockPosition cachedBlockPosition4 = result.translate(m, n, 0);
						world.updateNeighbors(cachedBlockPosition4.getBlockPos(), Blocks.AIR);
					}
				}
			}
		}
	}

	private BlockPattern getSnowGolemPattern() {
		if (this.snowGolemPattern == null) {
			this.snowGolemPattern = BlockPatternBuilder.start().aisle("^", "#", "#")
					.where('^', CachedBlockPosition.matchesBlockState(IS_GOLEM_HEAD_PREDICATE))
					.where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK)))
					.build();
		}

		return this.snowGolemPattern;
	}

	private BlockPattern getIronGolemPattern() {
		if (this.ironGolemPattern == null) {
			this.ironGolemPattern = BlockPatternBuilder.start().aisle("~^~", "###", "~#~")
					.where('^', CachedBlockPosition.matchesBlockState(IS_GOLEM_HEAD_PREDICATE))
					.where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK)))
					.where('~', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR))).build();
		}

		return this.ironGolemPattern;
	}

}
