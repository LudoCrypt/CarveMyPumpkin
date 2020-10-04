package net.ludocrypt.carvepump;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.ludocrypt.carvepump.blocks.CMPCarvedPumpkinBlock;
import net.ludocrypt.carvepump.blocks.CMPJackOLanternBlock;
import net.ludocrypt.carvepump.blocks.entity.CarvedBlockEntity;
import net.ludocrypt.carvepump.blocks.entity.LitCarvedBlockEntity;
import net.ludocrypt.carvepump.items.CarverItem;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CarveMyPumpkin implements ModInitializer {

	public static final Block CARVED_PUMPKIN = new CMPCarvedPumpkinBlock();
	public static final Block JACK_O_LANTERN = new CMPJackOLanternBlock();
	public static final ToolItem WOODEN_CARVER = new CarverItem(ToolMaterials.WOOD, 3, 1,
			new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	public static final ToolItem STONE_CARVER = new CarverItem(ToolMaterials.STONE, 4, 1,
			new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	public static final ToolItem GOLD_CARVER = new CarverItem(ToolMaterials.GOLD, 3, 1,
			new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	public static final ToolItem IRON_CARVER = new CarverItem(ToolMaterials.IRON, 5, 1,
			new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	public static final ToolItem DIAMOND_CARVER = new CarverItem(ToolMaterials.DIAMOND, 6, 1,
			new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	public static final ToolItem NETHERITE_CARVER = new CarverItem(ToolMaterials.NETHERITE, 7, 1,
			new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));

	@Override
	public void onInitialize() {

		Registry.register(Registry.ITEM, id("wooden_carver"), WOODEN_CARVER);
		Registry.register(Registry.ITEM, id("stone_carver"), STONE_CARVER);
		Registry.register(Registry.ITEM, id("gold_carver"), GOLD_CARVER);
		Registry.register(Registry.ITEM, id("iron_carver"), IRON_CARVER);
		Registry.register(Registry.ITEM, id("diamond_carver"), DIAMOND_CARVER);
		Registry.register(Registry.ITEM, id("netherite_carver"), NETHERITE_CARVER);

		registerBlockEntity("carved_pumpkin", CARVED_PUMPKIN, CarvedBlockEntity::new,
				(blockEntityType) -> CarvedBlockEntity.blockEntityType = blockEntityType, true);

		registerBlockEntity("jack_o_lantern", JACK_O_LANTERN, LitCarvedBlockEntity::new,
				(blockEntityType) -> LitCarvedBlockEntity.blockEntityType = blockEntityType, false);

	}

	public static Identifier id(String id) {
		return new Identifier("carvepump", id);
	}

	private <T extends BlockEntity> ItemStack registerBlockEntity(String identifier, Block block,
			Supplier<? extends T> blockEntitySupplier,
			@SuppressWarnings("rawtypes") Consumer<BlockEntityType> blockEntityConsumer, boolean wearable) {

		Registry.register(Registry.BLOCK, id(identifier), block);

		BlockEntityType<?> blockEntityType = Registry.register(Registry.BLOCK_ENTITY_TYPE, id(identifier),
				BlockEntityType.Builder.create(blockEntitySupplier, block).build(null));

		blockEntityConsumer.accept(blockEntityType);

		BlockItem blockItem = new BlockItem(block,
				wearable ? new FabricItemSettings().equipmentSlot(stack -> EquipmentSlot.HEAD)
						: new FabricItemSettings());

		Registry.register(Registry.ITEM, id(identifier), blockItem);

		return new ItemStack(blockItem);
	}

	public static void renderPumpkinSquare(MinecraftClient client, double scaledWidth, double scaledHeight, double x,
			double y, Identifier id) {

		client.getTextureManager().bindTexture(id);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);

		bufferBuilder.vertex(x * scaledWidth, scaledHeight + (y * scaledHeight), -90.0D).texture(0.0F, 1.0F).next();

		bufferBuilder.vertex(scaledWidth + (x * scaledWidth), scaledHeight + (y * scaledHeight), -90.0D)
				.texture(1.0F, 1.0F).next();

		bufferBuilder.vertex(scaledWidth + (x * scaledWidth), y * scaledHeight, -90.0D).texture(1.0F, 0.0F).next();

		bufferBuilder.vertex(x * scaledWidth, y * scaledHeight, -90.0D).texture(0.0F, 0.0F).next();

		tessellator.draw();
	}

}
