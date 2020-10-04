package net.ludocrypt.carvepump.blocks;

import net.ludocrypt.carvepump.CarveMyPumpkin;
import net.ludocrypt.carvepump.blocks.entity.CarvedBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class UncarvableMelonBlock extends UncarvableBlock {

	public UncarvableMelonBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CarvedBlockEntity) {
			CarvedBlockEntity carvedPumpkinBlockEntity = (CarvedBlockEntity) blockEntity;
			if (!world.isClient && !carvedPumpkinBlockEntity.isUncarved()) {
				if (player.isCreative()) {
					ItemStack itemStack = new ItemStack(CarveMyPumpkin.JACK_O_MELON);
					CompoundTag compoundTag = carvedPumpkinBlockEntity.serializeCarving(new CompoundTag());
					if (!compoundTag.isEmpty()) {
						itemStack.putSubTag("BlockEntityTag", compoundTag);
					}
					ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5D,
							(double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, itemStack);
					itemEntity.setToDefaultPickupDelay();
					world.spawnEntity(itemEntity);
				} else {
					ItemStack itemStack = new ItemStack(CarveMyPumpkin.CARVED_MELON);
					CompoundTag compoundTag = carvedPumpkinBlockEntity.serializeCarving(new CompoundTag());
					if (!compoundTag.isEmpty()) {
						itemStack.putSubTag("BlockEntityTag", compoundTag);
					}
					ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5D,
							(double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, itemStack);
					itemEntity.setToDefaultPickupDelay();
					world.spawnEntity(itemEntity);

					ItemStack torch = new ItemStack(Items.TORCH);
					ItemEntity torchEntity = new ItemEntity(world, (double) pos.getX() + 0.5D,
							(double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, torch);
					torchEntity.setToDefaultPickupDelay();
					world.spawnEntity(torchEntity);
				}
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
		return Blocks.MELON.getTranslationKey();
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		ItemStack itemStack = new ItemStack(CarveMyPumpkin.JACK_O_MELON);
		CompoundTag compoundTag = ((CarvedBlockEntity) world.getBlockEntity(pos)).serializeCarving(new CompoundTag());
		if (!compoundTag.isEmpty()) {
			itemStack.putSubTag("BlockEntityTag", compoundTag);
		}
		return itemStack;
	}

	@Override
	public Block getCarvingBlock() {
		return Blocks.MELON;
	}

}
