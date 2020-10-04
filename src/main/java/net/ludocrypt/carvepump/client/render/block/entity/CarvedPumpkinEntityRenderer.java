package net.ludocrypt.carvepump.client.render.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.carvepump.CarveMyPumpkin;
import net.ludocrypt.carvepump.blocks.CMPCarvedPumpkinBlock;
import net.ludocrypt.carvepump.blocks.entity.CarvedBlockEntity;
import net.ludocrypt.carvepump.blocks.entity.LitCarvedBlockEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class CarvedPumpkinEntityRenderer extends BlockEntityRenderer<CarvedBlockEntity> {
	private static final MinecraftClient client = MinecraftClient.getInstance();
	public static Identifier PUMPKIN_HALO = new Identifier("carvepump", "textures/entity/pumpkin_halo_colors.png");
	public static Identifier JACK_O_LANTERN_HALO = new Identifier("carvepump",
			"textures/entity/jack_o_lantern_halo_colors.png");

	public CarvedPumpkinEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
		super(blockEntityRenderDispatcher);
	}

	@Override
	public void render(CarvedBlockEntity carvedPumpkinBlockEntity, float f, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, int j) {

		matrixStack.push();
		matrixStack.translate(0.5, 0.5, 0.5);

		int lightAbove = WorldRenderer.getLightmapCoordinates(carvedPumpkinBlockEntity.getWorld(),
				carvedPumpkinBlockEntity.getPos().up());
		MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(Blocks.PUMPKIN),
				ModelTransformation.Mode.NONE, lightAbove, OverlayTexture.DEFAULT_UV, matrixStack,
				vertexConsumerProvider);

		matrixStack.translate(-0.5, -0.5, -0.5);

		renderFace(carvedPumpkinBlockEntity, false, matrixStack, vertexConsumerProvider, lightAbove,
				OverlayTexture.DEFAULT_UV);

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

	public static void renderModel(int x, int y, byte[][] carving, boolean lit, Direction dir, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		if (carving != null) {
			if (carving[x][y] == 1) {
				boolean darkest = isDarkest(x, y, carving);
				boolean dark = isDark(x, y, carving);
				ModelPart modelPart = new ModelPart(8, 8, dark ? 4 : 0, darkest ? 4 : 0);
				if (dir == Direction.NORTH) {
					modelPart.addCuboid((-x) + 15, y, -0.005F, 1F, 1F, 0.0F);
					modelPart.render(matrixStack, vertexConsumerProvider
							.getBuffer(RenderLayer.getEntitySolid(lit ? JACK_O_LANTERN_HALO : PUMPKIN_HALO)), i, j);

				}
				if (dir == Direction.SOUTH) {
					modelPart.addCuboid(x, y, 16.005F, 1F, 1F, 0.0F);
					modelPart.render(matrixStack, vertexConsumerProvider
							.getBuffer(RenderLayer.getEntitySolid(lit ? JACK_O_LANTERN_HALO : PUMPKIN_HALO)), i, j);

				}
				if (dir == Direction.EAST) {
					modelPart.addCuboid(16.005F, y, (-x) + 15, 0F, 1F, 1.0F);
					modelPart.render(matrixStack, vertexConsumerProvider
							.getBuffer(RenderLayer.getEntitySolid(lit ? JACK_O_LANTERN_HALO : PUMPKIN_HALO)), i, j);

				}
				if (dir == Direction.WEST) {
					modelPart.addCuboid(-0.005F, y, x, 0F, 1F, 1.0F);
					modelPart.render(matrixStack, vertexConsumerProvider
							.getBuffer(RenderLayer.getEntitySolid(lit ? JACK_O_LANTERN_HALO : PUMPKIN_HALO)), i, j);

				}
				modelPart = null;
			}
		}

	}

	public static void renderFace(BlockEntity carvedPumpkinBlockEntity, boolean lit, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, int j) {

		Direction dir = carvedPumpkinBlockEntity.getWorld() != null ? carvedPumpkinBlockEntity.getWorld()
				.getBlockState(carvedPumpkinBlockEntity.getPos()).get(CMPCarvedPumpkinBlock.FACING) : Direction.NORTH;

		byte[][] carving = null;
		if (carvedPumpkinBlockEntity instanceof CarvedBlockEntity) {
			carving = ((CarvedBlockEntity) carvedPumpkinBlockEntity).get2DArray();
		} else if (carvedPumpkinBlockEntity instanceof LitCarvedBlockEntity) {
			carving = ((LitCarvedBlockEntity) carvedPumpkinBlockEntity).get2DArray();
		}

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				renderModel(x, y, carving, lit, dir, matrixStack, vertexConsumerProvider, i, j);
			}
		}
	}

	public static void renderInHand(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		Item CARVED_PUMPKIN_ITEM = Registry.ITEM.get(CarveMyPumpkin.id("carved_pumpkin"));
		Item JACK_O_LANTERN_ITEM = Registry.ITEM.get(CarveMyPumpkin.id("jack_o_lantern"));
		if (stack.getItem() == CARVED_PUMPKIN_ITEM || stack.getItem() == JACK_O_LANTERN_ITEM) {

			ItemStack pumpkinStack = new ItemStack(Blocks.PUMPKIN);
			matrixStack.push();
			BakedModel model = client.getBakedModelManager().getModel(new ModelIdentifier("pumpkin"));
			matrixStack.translate(0.5, 0.5, 0.5);
			client.getItemRenderer().renderItem(pumpkinStack, ModelTransformation.Mode.HEAD, false, matrixStack,
					vertexConsumerProvider, i, j, model);
			matrixStack.translate(-0.5, -0.5, -0.5);
			matrixStack.pop();
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
						dir = Direction.NORTH;
						break;
					default:
						dir = Direction.NORTH;
						break;
					}
					for (int x = 0; x < 16; x++) {
						for (int y = 0; y < 16; y++) {
							renderModel(x, y, carving, stack.getItem() == CARVED_PUMPKIN_ITEM ? false : true, dir,
									matrixStack, vertexConsumerProvider, i, j);
						}
					}
				}
			}
		}
	}
}
