package net.ludocrypt.carvepump.items;

import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.Sets;

import net.ludocrypt.carvepump.CarveMyPumpkin;
import net.ludocrypt.carvepump.blocks.CMPCarvedPumpkinBlock;
import net.ludocrypt.carvepump.blocks.entity.CarvedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class CarverItem extends MiningToolItem {

	private static final Set<Block> EFFECTIVE_BLOCKS = Sets
			.newHashSet(new Block[] { Blocks.PUMPKIN, Blocks.CARVED_PUMPKIN, Blocks.JACK_O_LANTERN,
					CarveMyPumpkin.CARVED_PUMPKIN, CarveMyPumpkin.JACK_O_LANTERN });

	public CarverItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super((float) attackDamage, attackSpeed, material, EFFECTIVE_BLOCKS, settings);
	}

	@Override
	public boolean isEffectiveOn(BlockState state) {
		if (state.getMaterial() == Material.GOURD) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {

		BlockPos pos = context.getBlockPos();
		World world = context.getWorld();
		Direction dir = context.getSide();

		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CarvedBlockEntity) {
			CarvedBlockEntity carvedPumpkinBlockEntity = (CarvedBlockEntity) blockEntity;

			double xHitPos = context.getHitPos().getX() - pos.getX();
			double yHitPos = context.getHitPos().getY() - pos.getY();
			double zHitPos = context.getHitPos().getZ() - pos.getZ();

			if (dir == world.getBlockState(pos).get(CMPCarvedPumpkinBlock.FACING)) {
				for (int x = 0; x < 16; x++) {
					for (int y = 0; y < 16; y++) {
						if (dir == Direction.NORTH) {
							if ((xHitPos >= x * 0.0625 && xHitPos <= (x + 1) * 0.0625)
									&& (yHitPos >= y * 0.0625 && yHitPos <= (y + 1) * 0.0625)) {
								carvedPumpkinBlockEntity.setValue((-x) + 15, y,
										(byte) (carvedPumpkinBlockEntity.getValue((-x) + 15, y) == 0 ? 1 : 0));
							}
						} else if (dir == Direction.EAST) {
							if ((zHitPos >= x * 0.0625 && zHitPos <= (x + 1) * 0.0625)
									&& (yHitPos >= y * 0.0625 && yHitPos <= (y + 1) * 0.0625)) {
								carvedPumpkinBlockEntity.setValue((-x) + 15, y,
										(byte) (carvedPumpkinBlockEntity.getValue((-x) + 15, y) == 0 ? 1 : 0));
							}
						} else if (dir == Direction.SOUTH) {
							if ((xHitPos >= x * 0.0625 && xHitPos <= (x + 1) * 0.0625)
									&& (yHitPos >= y * 0.0625 && yHitPos <= (y + 1) * 0.0625)) {
								carvedPumpkinBlockEntity.setValue(x, y,
										(byte) (carvedPumpkinBlockEntity.getValue(x, y) == 0 ? 1 : 0));
							}
						} else if (dir == Direction.WEST) {
							if ((zHitPos >= x * 0.0625 && zHitPos <= (x + 1) * 0.0625)
									&& (yHitPos >= y * 0.0625 && yHitPos <= (y + 1) * 0.0625)) {
								carvedPumpkinBlockEntity.setValue(x, y,
										(byte) (carvedPumpkinBlockEntity.getValue(x, y) == 0 ? 1 : 0));
							}
						}
					}
				}
				if (context.getPlayer() != null) {
					context.getStack().damage(1, (LivingEntity) context.getPlayer(), (Consumer<LivingEntity>) ((p) -> {
						((LivingEntity) p).sendToolBreakStatus(context.getHand());
					}));
				}
				if (carvedPumpkinBlockEntity.isUncarved()) {
					world.setBlockState(pos, Blocks.PUMPKIN.getDefaultState(), 2);
				}
				return ActionResult.SUCCESS;
			} else {
				return ActionResult.FAIL;
			}
		} else if (world.getBlockState(pos) == Blocks.PUMPKIN.getDefaultState()
				&& (dir != Direction.UP && dir != Direction.DOWN)) {
			world.setBlockState(pos,
					CarveMyPumpkin.CARVED_PUMPKIN.getDefaultState().with(CMPCarvedPumpkinBlock.FACING, dir), 2);
			useOnBlock(context);
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.FAIL;
		}
	}

}
