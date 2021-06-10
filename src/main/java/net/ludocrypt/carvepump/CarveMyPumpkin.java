package net.ludocrypt.carvepump;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.ludocrypt.carvepump.blocks.CarvableBlock;
import net.ludocrypt.carvepump.blocks.entity.CarvedBlockEntity;
import net.ludocrypt.carvepump.items.CarverItem;
import net.ludocrypt.carvepump.recipe.JackORecipe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CarveMyPumpkin implements ModInitializer {

	public static final Block CARVED_PUMPKIN = new CarvableBlock(FabricBlockSettings.copy(Blocks.PUMPKIN).nonOpaque(), Blocks.PUMPKIN, new Identifier("carvepump", "textures/entity/pumpkin_halo_colors.png"), true);
	public static final Block JACK_O_LANTERN = new CarvableBlock(FabricBlockSettings.copy(Blocks.JACK_O_LANTERN).nonOpaque(), Blocks.PUMPKIN, new Identifier("carvepump", "textures/entity/jack_o_lantern_halo_colors.png"), false);
	public static final Block CARVED_MELON = new CarvableBlock(FabricBlockSettings.copy(Blocks.PUMPKIN).nonOpaque().mapColor(MapColor.GREEN), Blocks.MELON, new Identifier("carvepump", "textures/entity/melon_halo_colors.png"), true);
	public static final Block JACK_O_MELON = new CarvableBlock(FabricBlockSettings.copy(Blocks.JACK_O_LANTERN).nonOpaque().mapColor(MapColor.GREEN), Blocks.MELON, new Identifier("carvepump", "textures/entity/jack_o_melon_halo_colors.png"), false);

	public static final BlockItem CARVED_PUMPKIN_ITEM = new BlockItem(CARVED_PUMPKIN, new FabricItemSettings().equipmentSlot(stack -> EquipmentSlot.HEAD).maxCount(1));
	public static final BlockItem JACK_O_LANTERN_ITEM = new BlockItem(JACK_O_LANTERN, new FabricItemSettings().recipeRemainder(Blocks.PUMPKIN.asItem()).maxCount(1));
	public static final BlockItem CARVED_MELON_ITEM = new BlockItem(CARVED_MELON, new FabricItemSettings().equipmentSlot(stack -> EquipmentSlot.HEAD).maxCount(1));
	public static final BlockItem JACK_O_MELON_ITEM = new BlockItem(JACK_O_MELON, new FabricItemSettings().recipeRemainder(Blocks.MELON.asItem()).maxCount(1));

	public static final ToolItem WOODEN_CARVER = new CarverItem(ToolMaterials.WOOD, 2, 1, new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	public static final ToolItem STONE_CARVER = new CarverItem(ToolMaterials.STONE, 2, 1, new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	public static final ToolItem GOLD_CARVER = new CarverItem(ToolMaterials.GOLD, 2, 1, new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	public static final ToolItem IRON_CARVER = new CarverItem(ToolMaterials.IRON, 2, 1, new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	public static final ToolItem DIAMOND_CARVER = new CarverItem(ToolMaterials.DIAMOND, 2, 1, new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
	public static final ToolItem NETHERITE_CARVER = new CarverItem(ToolMaterials.NETHERITE, 2, 1, new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));

	public static final SpecialRecipeSerializer<JackORecipe> JACK_O_LANTERN_RECIPE = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("carvepump", "jack_o_lantern_special"), new SpecialRecipeSerializer<JackORecipe>((id) -> {
		return new JackORecipe(id, CARVED_PUMPKIN_ITEM, JACK_O_LANTERN_ITEM, () -> CarveMyPumpkin.JACK_O_LANTERN_RECIPE);
	}));

	public static final SpecialRecipeSerializer<JackORecipe> JACK_O_MELON_RECIPE = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("carvepump", "jack_o_melon_special"), new SpecialRecipeSerializer<JackORecipe>((id) -> {
		return new JackORecipe(id, CARVED_MELON_ITEM, JACK_O_MELON_ITEM, () -> CarveMyPumpkin.JACK_O_MELON_RECIPE);
	}));

	// List of carvable blocks
	public static final Block[] carvableBlocks = { CARVED_PUMPKIN, JACK_O_LANTERN, CARVED_MELON, JACK_O_MELON, Blocks.PUMPKIN, Blocks.MELON };
	// Not to be confused with the list of blockEntity carvable Blocks
	public static final Block[] carvedBlocksList = { CARVED_PUMPKIN, JACK_O_LANTERN, CARVED_MELON, JACK_O_MELON };
	// Blocks to spawn golems
	public static final Block[] golemSpawningBlocks = { CARVED_PUMPKIN, JACK_O_LANTERN, Blocks.PUMPKIN, Blocks.MELON };

	public static BlockEntityType<CarvedBlockEntity> CARVED_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("carved_block_entity_type"), FabricBlockEntityTypeBuilder.create(CarvedBlockEntity::new, carvedBlocksList).build(null));

	@Override
	public void onInitialize() {

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

		Registry.register(Registry.ITEM, id("carved_pumpkin"), CARVED_PUMPKIN_ITEM);
		Registry.register(Registry.ITEM, id("jack_o_lantern"), JACK_O_LANTERN_ITEM);
		Registry.register(Registry.ITEM, id("carved_melon"), CARVED_MELON_ITEM);
		Registry.register(Registry.ITEM, id("jack_o_melon"), JACK_O_MELON_ITEM);
	}

	public static Identifier id(String id) {
		return new Identifier("carvepump", id);
	}

	public static void renderBlurSquare(MinecraftClient client, double scaledWidth, double scaledHeight, double x, double y, Identifier id) {
		RenderSystem.setShaderTexture(0, id);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(x * scaledWidth, scaledHeight + (y * scaledHeight), -90.0D).texture(0.0F, 1.0F).next();
		bufferBuilder.vertex(scaledWidth + (x * scaledWidth), scaledHeight + (y * scaledHeight), -90.0D).texture(1.0F, 1.0F).next();
		bufferBuilder.vertex(scaledWidth + (x * scaledWidth), y * scaledHeight, -90.0D).texture(1.0F, 0.0F).next();
		bufferBuilder.vertex(x * scaledWidth, y * scaledHeight, -90.0D).texture(0.0F, 0.0F).next();
		tessellator.draw();
	}

}
