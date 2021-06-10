package net.ludocrypt.carvepump.recipe;

import java.util.List;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

import net.ludocrypt.carvepump.access.ItemStackCopy;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class JackORecipe extends SpecialCraftingRecipe {

	final BlockItem pump;
	final BlockItem jack;
	final Supplier<RecipeSerializer<JackORecipe>> serializer;

	public JackORecipe(Identifier id, BlockItem pump, BlockItem jack, Supplier<RecipeSerializer<JackORecipe>> serializer) {
		super(id);
		this.pump = pump;
		this.jack = jack;
		this.serializer = serializer;
	}

	@Override
	public boolean matches(CraftingInventory inventory, World world) {
		List<Integer> pointList = Lists.newArrayList();

		for (int i = 0; i < inventory.size(); ++i) {
			if (!inventory.getStack(i).isEmpty()) {
				pointList.add(i);
			}
		}

		if (pointList.size() == 2) {
			if (pointList.get(0) + inventory.getWidth() <= inventory.size() && inventory.getStack(pointList.get(0) + inventory.getWidth()).getItem().equals(Items.TORCH) && inventory.getStack(pointList.get(0)).getItem().equals(pump)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public ItemStack craft(CraftingInventory inventory) {
		List<Integer> pointList = Lists.newArrayList();

		for (int i = 0; i < inventory.size(); ++i) {
			if (!inventory.getStack(i).isEmpty()) {
				pointList.add(i);
			}
		}

		if (pointList.size() == 2) {
			if (pointList.get(0) + inventory.getWidth() <= inventory.size() && inventory.getStack(pointList.get(0) + inventory.getWidth()).getItem().equals(Items.TORCH) && inventory.getStack(pointList.get(0)).getItem().equals(pump)) {
				return ItemStackCopy.copy(inventory.getStack(pointList.get(0)).copy(), jack);
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return width >= 1 && height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return serializer.get();
	}

}
