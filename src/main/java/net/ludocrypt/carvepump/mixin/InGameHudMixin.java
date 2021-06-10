package net.ludocrypt.carvepump.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.carvepump.CarveMyPumpkin;
import net.ludocrypt.carvepump.blocks.CarvableBlock;
import net.ludocrypt.carvepump.blocks.entity.CarvedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {

	@Shadow
	private @Final MinecraftClient client;
	@Shadow
	private int scaledWidth;
	@Shadow
	private int scaledHeight;

	@Unique
	private Identifier SQUARE = new Identifier("carvepump", "textures/gui/pumpkin_dark_square.png");
	@Unique
	private Identifier LEFT = new Identifier("carvepump", "textures/gui/pumpkin_blur_left.png");
	@Unique
	private Identifier RIGHT = new Identifier("carvepump", "textures/gui/pumpkin_blur_right.png");
	@Unique
	private Identifier TOP = new Identifier("carvepump", "textures/gui/pumpkin_blur_top.png");
	@Unique
	private Identifier BOTTOM = new Identifier("carvepump", "textures/gui/pumpkin_blur_bottom.png");
	@Unique
	private Identifier TOP_LEFT = new Identifier("carvepump", "textures/gui/pumpkin_blur_top_left.png");
	@Unique
	private Identifier TOP_RIGHT = new Identifier("carvepump", "textures/gui/pumpkin_blur_top_right.png");
	@Unique
	private Identifier BOTTOM_LEFT = new Identifier("carvepump", "textures/gui/pumpkin_blur_bottom_left.png");
	@Unique
	private Identifier BOTTOM_RIGHT = new Identifier("carvepump", "textures/gui/pumpkin_blur_bottom_right.png");
	@Unique
	private Identifier OUTLINE = new Identifier("carvepump", "textures/gui/pumpkin_blur_outline.png");

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;getArmorStack(I)Lnet/minecraft/item/ItemStack;"), cancellable = true)
	private void carvemypumpkin_render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {

		ItemStack itemStack = this.client.player.getInventory().getArmorStack(3);

		if (itemStack.getItem() instanceof BlockItem) {
			Block block = ((BlockItem) itemStack.getItem()).getBlock();

			if (this.client.options.getPerspective().isFirstPerson() && block instanceof CarvableBlock) {

				RenderSystem.disableDepthTest();
				RenderSystem.depthMask(false);
				RenderSystem.defaultBlendFunc();
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.setShaderTexture(0, OUTLINE);
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferBuilder = tessellator.getBuffer();
				bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
				bufferBuilder.vertex(0.0D, (double) this.scaledHeight, -90.0D).texture(0.0F, 1.0F).next();
				bufferBuilder.vertex((double) this.scaledWidth, (double) this.scaledHeight, -90.0D).texture(1.0F, 1.0F).next();
				bufferBuilder.vertex((double) this.scaledWidth, 0.0D, -90.0D).texture(1.0F, 0.0F).next();
				bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
				tessellator.draw();

				byte[][] carving = CarvedBlockEntity.getCarvingFromStack(itemStack);

				int dividedWidth = (scaledWidth / 16);
				int dividedHeight = (scaledHeight / 16);

				for (int x = 0; x < 16; x++) {
					for (int y = 0; y < 16; y++) {
						int oppY = -y + 15;
						if (carving[x][oppY] == 0) {
							CarveMyPumpkin.renderBlurSquare(client, dividedWidth, dividedHeight, x, y, SQUARE);

							if (carving[x < 15 ? x + 1 : x][oppY] == 1) {
								CarveMyPumpkin.renderBlurSquare(client, dividedWidth, dividedHeight, x + 1, y, LEFT);
							}

							if (carving[x > 0 ? x - 1 : x][oppY] == 1) {
								CarveMyPumpkin.renderBlurSquare(client, dividedWidth, dividedHeight, x - 1, y, RIGHT);
							}

							if (carving[x][oppY < 15 ? oppY + 1 : oppY] == 1) {
								CarveMyPumpkin.renderBlurSquare(client, dividedWidth, dividedHeight, x, y - 1, BOTTOM);
							}

							if (carving[x][oppY > 0 ? oppY - 1 : oppY] == 1) {
								CarveMyPumpkin.renderBlurSquare(client, dividedWidth, dividedHeight, x, y + 1, TOP);
							}

							if (carving[x][oppY > 0 ? oppY - 1 : oppY] == 1 && carving[x < 15 ? x + 1 : x][oppY] == 1) {
								CarveMyPumpkin.renderBlurSquare(client, dividedWidth, dividedHeight, x + 1, y + 1, TOP_LEFT);
							}

							if (carving[x][oppY > 0 ? oppY - 1 : oppY] == 1 && carving[x > 0 ? x - 1 : x][oppY] == 1) {
								CarveMyPumpkin.renderBlurSquare(client, dividedWidth, dividedHeight, x - 1, y + 1, TOP_RIGHT);
							}

							if (carving[x][oppY < 15 ? oppY + 1 : oppY] == 1 && carving[x < 15 ? x + 1 : x][oppY] == 1) {
								CarveMyPumpkin.renderBlurSquare(client, dividedWidth, dividedHeight, x + 1, y - 1, BOTTOM_LEFT);
							}

							if (carving[x][oppY < 15 ? oppY + 1 : oppY] == 1 && carving[x > 0 ? x - 1 : x][oppY] == 1) {
								CarveMyPumpkin.renderBlurSquare(client, dividedWidth, dividedHeight, x - 1, y - 1, BOTTOM_RIGHT);
							}
						}
					}
				}

				RenderSystem.depthMask(true);
				RenderSystem.enableDepthTest();
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

			}
		}
	}

}
