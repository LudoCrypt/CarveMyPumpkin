package net.ludocrypt.carvepump;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.ludocrypt.carvepump.blocks.CarvableMelonBlock;
import net.ludocrypt.carvepump.blocks.CarvablePumpkinBlock;
import net.ludocrypt.carvepump.blocks.UncarvableMelonBlock;
import net.ludocrypt.carvepump.blocks.UncarvablePumpkinBlock;
import net.ludocrypt.carvepump.blocks.entity.CarvedBlockEntity;
import net.ludocrypt.carvepump.items.CarverItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CarveMyPumpkin implements ModInitializer {

	public static BlockEntityType<CarvedBlockEntity> CARVED_BLOCK_ENTITY;

	public static final Block CARVED_PUMPKIN = new CarvablePumpkinBlock(AbstractBlock.Settings
			.of(Material.GOURD, MaterialColor.ORANGE).strength(1.0F).sounds(BlockSoundGroup.WOOD));
	public static final Block JACK_O_LANTERN = new UncarvablePumpkinBlock(AbstractBlock.Settings
			.of(Material.GOURD, MaterialColor.ORANGE).strength(1.0F).sounds(BlockSoundGroup.WOOD).luminance((state) -> {
				return 15;
			}));

	public static final Block CARVED_MELON = new CarvableMelonBlock(AbstractBlock.Settings
			.of(Material.GOURD, MaterialColor.ORANGE).strength(1.0F).sounds(BlockSoundGroup.WOOD));
	public static final Block JACK_O_MELON = new UncarvableMelonBlock(AbstractBlock.Settings
			.of(Material.GOURD, MaterialColor.ORANGE).strength(1.0F).sounds(BlockSoundGroup.WOOD).luminance((state) -> {
				return 15;
			}));

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

	// List of carvable blocks
	public static final Block[] carvableBlocks = { CARVED_PUMPKIN, JACK_O_LANTERN, CARVED_MELON, JACK_O_MELON,
			Blocks.PUMPKIN, Blocks.MELON };
	// Not to be confused with the list of blockEntity carvable Blocks
	public static final Block[] carvedBlocksList = { CARVED_PUMPKIN, JACK_O_LANTERN, CARVED_MELON, JACK_O_MELON };

	@Override
	public void onInitialize() {

		CARVED_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("carved_block_entity_type"),
				BlockEntityType.Builder.create(CarvedBlockEntity::new, carvedBlocksList).build(null));

		Registry.register(Registry.ITEM, id("wooden_carver"), WOODEN_CARVER);
		Registry.register(Registry.ITEM, id("stone_carver"), STONE_CARVER);
		Registry.register(Registry.ITEM, id("gold_carver"), GOLD_CARVER);
		Registry.register(Registry.ITEM, id("iron_carver"), IRON_CARVER);
		Registry.register(Registry.ITEM, id("diamond_carver"), DIAMOND_CARVER);
		Registry.register(Registry.ITEM, id("netherite_carver"), NETHERITE_CARVER);

		Registry.register(Registry.BLOCK, id("carved_pumpkin"), CARVED_PUMPKIN);
		Registry.register(Registry.BLOCK, id("jack_o_lantern"), JACK_O_LANTERN);
		Registry.register(Registry.BLOCK, id("carved_melon"), CARVED_MELON);
		Registry.register(Registry.BLOCK, id("jack_o_melon"), JACK_O_MELON);

		Registry.register(Registry.ITEM, id("carved_pumpkin"),
				new BlockItem(CARVED_PUMPKIN, new FabricItemSettings().equipmentSlot(stack -> EquipmentSlot.HEAD)));

		Registry.register(Registry.ITEM, id("jack_o_lantern"), new BlockItem(JACK_O_LANTERN, new FabricItemSettings()));

		Registry.register(Registry.ITEM, id("carved_melon"),
				new BlockItem(CARVED_MELON, new FabricItemSettings().equipmentSlot(stack -> EquipmentSlot.HEAD)));

		Registry.register(Registry.ITEM, id("jack_o_melon"), new BlockItem(JACK_O_MELON, new FabricItemSettings()));

	}

	public static Identifier id(String id) {
		return new Identifier("carvepump", id);
	}

	public static void renderBlurSquare(MinecraftClient client, double scaledWidth, double scaledHeight, double x,
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
