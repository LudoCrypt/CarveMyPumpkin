package net.ludocrypt.carvepump;

import java.util.function.Function;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.ludocrypt.carvepump.client.render.block.entity.CarvedPumpkinEntityRenderer;
import net.ludocrypt.carvepump.client.render.block.entity.JackOLanternBlockEntityRenderer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class CarveMyPumpkinClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		registerBlockEntityRenderer("carved_pumpkin", CarvedPumpkinEntityRenderer::new);
		registerBlockEntityRenderer("jack_o_lantern", JackOLanternBlockEntityRenderer::new);

	}

	@SuppressWarnings("unchecked")
	private <E extends BlockEntity> void registerBlockEntityRenderer(String identifier,
			Function<BlockEntityRenderDispatcher, BlockEntityRenderer<E>> blockEntityRenderer) {
		BlockEntityRendererRegistry.INSTANCE.register(
				(BlockEntityType<E>) Registry.BLOCK_ENTITY_TYPE.get(CarveMyPumpkin.id(identifier)),
				blockEntityRenderer);
	}

}
