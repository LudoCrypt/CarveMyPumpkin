package net.ludocrypt.carvepump.client.render.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.carvepump.blocks.CarvableBlock;
import net.ludocrypt.carvepump.blocks.UncarvableBlock;
import net.ludocrypt.carvepump.blocks.entity.CarvedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

@Environment(EnvType.CLIENT)
public class CarvedBlockEntityRenderer extends BlockEntityRenderer<CarvedBlockEntity> {

	private static final MinecraftClient client = MinecraftClient.getInstance();

	public CarvedBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
		super(blockEntityRenderDispatcher);
	}

	@Override
	public void render(CarvedBlockEntity carvedBlockEntity, float f, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, int j) {

		Block block = carvedBlockEntity.getWorld().getBlockState(carvedBlockEntity.getPos()).getBlock();

		matrixStack.push();
		matrixStack.translate(0.5, 0.5, 0.5);

		int lightAbove = WorldRenderer.getLightmapCoordinates(carvedBlockEntity.getWorld(),
				carvedBlockEntity.getPos().up());
		if (block instanceof CarvableBlock) {
			MinecraftClient.getInstance().getItemRenderer().renderItem(
					new ItemStack(((CarvableBlock) block).getCarvingBlock()), ModelTransformation.Mode.NONE, lightAbove,
					OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider);

			matrixStack.translate(-0.5, -0.5, -0.5);

			renderFace(carvedBlockEntity, ((CarvableBlock) block).getRenderId(), matrixStack, vertexConsumerProvider,
					lightAbove, OverlayTexture.DEFAULT_UV);
		} else if (block instanceof UncarvableBlock) {
			MinecraftClient.getInstance().getItemRenderer().renderItem(
					new ItemStack(((UncarvableBlock) block).getCarvingBlock()), ModelTransformation.Mode.NONE,
					lightAbove, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider);

			matrixStack.translate(-0.5, -0.5, -0.5);

			renderFace(carvedBlockEntity, ((UncarvableBlock) block).getRenderId(), matrixStack, vertexConsumerProvider,
					lightAbove, OverlayTexture.DEFAULT_UV);
		}

		matrixStack.pop();

	}

	private static boolean isDarkest(int x, int y, byte[][] carving) {
		boolean darkest = ((carving[x != 0 ? x - 1 : x][y] == 0 || carving[x][y != 15 ? y + 1 : y] == 0) ? true
				: (carving[x != 0 ? x - 1 : x][y != 15 ? y + 1 : y] == 0
						&& carving[x != 15 ? x + 1 : x][y != 15 ? y + 1 : y] == 0) ? true : false) ? true
								: (x == 0 || y == 15) ? true : false;
		return darkest;
	}

	private static boolean isDark(int x, int y, byte[][] carving) {
		boolean dark = (isDarkest(x != 0 ? x - 1 : x, y, carving) || isDarkest(x, y != 15 ? y + 1 : y, carving)) ? true
				: (isDarkest(x != 0 ? x - 1 : x, y != 15 ? y + 1 : y, carving)
						&& isDarkest(x != 15 ? x + 1 : x, y != 15 ? y + 1 : y, carving) ? true : false) ? true
								: (x == 1 || y == 14) ? true : false;
		return dark;
	}

	public static void renderModel(int x, int y, byte[][] carving, Identifier colors, Direction dir,
			MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		if (carving != null) {
			if (carving[x][y] == 1) {
				boolean darkest = isDarkest(x, y, carving);
				boolean dark = isDark(x, y, carving);
				ModelPart modelPart = new ModelPart(8, 8, dark ? 4 : 0, darkest ? 4 : 0);
				if (dir == Direction.NORTH) {
					modelPart.addCuboid((-x) + 15, y, -0.005F, 1F, 1F, 0.0F);

				}
				if (dir == Direction.SOUTH) {
					modelPart.addCuboid(x, y, 16.005F, 1F, 1F, 0.0F);

				}
				if (dir == Direction.EAST) {
					modelPart.addCuboid(16.005F, y, (-x) + 15, 0F, 1F, 1.0F);

				}
				if (dir == Direction.WEST) {
					modelPart.addCuboid(-0.005F, y, x, 0F, 1F, 1.0F);

				}
				modelPart.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(colors)), i,
						j);
				modelPart = null;
			}
		}

	}

	public static void renderFace(CarvedBlockEntity carvedBlockEntity, Identifier colors, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, int j) {

		Direction dir = carvedBlockEntity.getWorld().getBlockState(carvedBlockEntity.getPos())
				.get(CarvableBlock.FACING);

		byte[][] carving = carvedBlockEntity.get2DArray();

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				renderModel(x, y, carving, colors, dir, matrixStack, vertexConsumerProvider, i, j);
			}
		}
	}

	public static void renderInHand(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		if (stack.getItem() instanceof BlockItem) {
			Block block = ((BlockItem) stack.getItem()).getBlock();

			if (block instanceof CarvableBlock) {
				CarvableBlock carvableBlock = ((CarvableBlock) block);
				ItemStack carvedBlockStack = new ItemStack(carvableBlock.getCarvingBlock());
				matrixStack.push();
				BakedModel model = client.getBakedModelManager()
						.getModel(new ModelIdentifier(carvableBlock.getCarvingBlock().toString().substring(15,
								carvableBlock.getCarvingBlock().toString().length() - 1)));
				matrixStack.translate(0.5, 0.5, 0.5);
				client.getItemRenderer().renderItem(carvedBlockStack, ModelTransformation.Mode.HEAD, false, matrixStack,
						vertexConsumerProvider, i, j, model);
				matrixStack.translate(-0.5, -0.5, -0.5);
				matrixStack.pop();
			} else if (block instanceof UncarvableBlock) {
				UncarvableBlock uncarvableBlock = ((UncarvableBlock) block);
				ItemStack carvedBlockStack = new ItemStack(uncarvableBlock.getCarvingBlock());
				matrixStack.push();
				BakedModel model = client.getBakedModelManager()
						.getModel(new ModelIdentifier(uncarvableBlock.getCarvingBlock().toString().substring(15,
								uncarvableBlock.getCarvingBlock().toString().length() - 1)));
				matrixStack.translate(0.5, 0.5, 0.5);
				client.getItemRenderer().renderItem(carvedBlockStack, ModelTransformation.Mode.HEAD, false, matrixStack,
						vertexConsumerProvider, i, j, model);
				matrixStack.translate(-0.5, -0.5, -0.5);
				matrixStack.pop();
			}
			if (stack.hasTag()) {
				if (stack.getSubTag("BlockEntityTag") != null) {
					byte[][] carving = CarvedBlockEntity.getCarvingFromStack(stack);
					Direction dir;
					switch (mode) {
					case NONE:
						dir = Direction.NORTH;
						break;
					case THIRD_PERSON_LEFT_HAND:
						dir = Direction.EAST;
						break;
					case THIRD_PERSON_RIGHT_HAND:
						dir = Direction.WEST;
						break;
					case FIRST_PERSON_LEFT_HAND:
						dir = Direction.SOUTH;
						break;
					case FIRST_PERSON_RIGHT_HAND:
						dir = Direction.SOUTH;
						break;
					case HEAD:
						dir = Direction.SOUTH;
						break;
					case GUI:
						dir = Direction.SOUTH;
						break;
					case GROUND:
						dir = Direction.NORTH;
						break;
					case FIXED:
						dir = Direction.SOUTH;
						break;
					default:
						dir = Direction.NORTH;
						break;
					}
					if (block instanceof CarvableBlock) {
						for (int x = 0; x < 16; x++) {
							for (int y = 0; y < 16; y++) {
								renderModel(x, y, carving, ((CarvableBlock) block).getRenderId(), dir, matrixStack,
										vertexConsumerProvider, i, j);
							}
						}
					} else if (block instanceof UncarvableBlock) {
						for (int x = 0; x < 16; x++) {
							for (int y = 0; y < 16; y++) {
								renderModel(x, y, carving, ((UncarvableBlock) block).getRenderId(), dir, matrixStack,
										vertexConsumerProvider, i, j);
							}
						}
					}

				}
			}
		}
	}
}
