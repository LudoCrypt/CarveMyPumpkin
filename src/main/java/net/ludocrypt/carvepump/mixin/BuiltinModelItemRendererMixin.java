package net.ludocrypt.carvepump.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.carvepump.client.render.block.entity.CarvedPumpkinEntityRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

	@Inject(method = "render", at = @At("HEAD"))
	private void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices,
			VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
		CarvedPumpkinEntityRenderer.renderInHand(stack, mode, matrices, vertexConsumers, light, overlay);
	}
}
