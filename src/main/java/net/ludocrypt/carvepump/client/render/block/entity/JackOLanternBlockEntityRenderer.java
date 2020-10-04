package net.ludocrypt.carvepump.client.render.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.carvepump.blocks.entity.LitCarvedBlockEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class JackOLanternBlockEntityRenderer extends BlockEntityRenderer<LitCarvedBlockEntity> {

	public JackOLanternBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
		super(blockEntityRenderDispatcher);
	}

	@Override
	public void render(LitCarvedBlockEntity jackOLanternBlockEntity, float f, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, int j) {

		matrixStack.push();
		matrixStack.translate(0.5, 0.5, 0.5);

		int lightAbove = WorldRenderer.getLightmapCoordinates(jackOLanternBlockEntity.getWorld(),
				jackOLanternBlockEntity.getPos().up());
		MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(Blocks.PUMPKIN),
				ModelTransformation.Mode.NONE, lightAbove, OverlayTexture.DEFAULT_UV, matrixStack,
				vertexConsumerProvider);

		matrixStack.translate(-0.5, -0.5, -0.5);

		CarvedPumpkinEntityRenderer.renderFace(jackOLanternBlockEntity, true, matrixStack, vertexConsumerProvider,
				lightAbove, OverlayTexture.DEFAULT_UV);

		matrixStack.pop();

	}

}
