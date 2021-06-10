package net.ludocrypt.carvepump;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.ludocrypt.carvepump.client.render.block.entity.CarvedBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class CarveMyPumpkinClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.INSTANCE.register(CarveMyPumpkin.CARVED_BLOCK_ENTITY, (context) -> new CarvedBlockEntityRenderer());
	}

}
