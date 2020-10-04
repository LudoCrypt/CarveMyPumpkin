package net.ludocrypt.carvepump.blocks;

import java.util.Iterator;
import java.util.function.Predicate;

import net.ludocrypt.carvepump.CarveMyPumpkin;
import net.ludocrypt.carvepump.blocks.entity.CarvedBlockEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.function.MaterialPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CarvablePumpkinBlock extends CarvableBlock {

	private BlockPattern snowGolemPattern;
	private BlockPattern ironGolemPattern;

	private static Predicate<BlockState> IS_GOLEM_HEAD_PREDICATE = (state) -> {
		return state != null && state.isOf(CarveMyPumpkin.CARVED_PUMPKIN);
	};

	public CarvablePumpkinBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CarvedBlockEntity) {
			CarvedBlockEntity carvedPumpkinBlockEntity = (CarvedBlockEntity) blockEntity;
			if (!world.isClient && !carvedPumpkinBlockEntity.isUncarved()) {
				ItemStack itemStack = new ItemStack(CarveMyPumpkin.CARVED_PUMPKIN);
				CompoundTag compoundTag = carvedPumpkinBlockEntity.serializeCarving(new CompoundTag());
				if (!compoundTag.isEmpty()) {
					itemStack.putSubTag("BlockEntityTag", compoundTag);
				}
				ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
						(double) pos.getZ() + 0.5D, itemStack);
				itemEntity.setToDefaultPickupDelay();
				world.spawnEntity(itemEntity);
			}
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CarvedBlockEntity) {
			CarvedBlockEntity carvedPumpkinBlockEntity = (CarvedBlockEntity) blockEntity;
			if (itemStack.hasTag()) {
				carvedPumpkinBlockEntity.deserializeCarving(itemStack.getSubTag("BlockEntityTag"));
			}
		}
	}

	@Override
	public String getTranslationKey() {
		return Blocks.CARVED_PUMPKIN.getTranslationKey();
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		ItemStack itemStack = new ItemStack(CarveMyPumpkin.CARVED_PUMPKIN);
		CompoundTag compoundTag = ((CarvedBlockEntity) world.getBlockEntity(pos)).serializeCarving(new CompoundTag());
		if (!compoundTag.isEmpty()) {
			itemStack.putSubTag("BlockEntityTag", compoundTag);
		}
		return itemStack;
	}

	@Override
	public Block getCarvingBlock() {
		return Blocks.PUMPKIN;
	}

	// Pumpkin Spawning

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
