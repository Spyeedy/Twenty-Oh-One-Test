package spyeedy.mods.spytwenohone.data.forge;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.tags.SpyTooItemTags;
import spyeedy.mods.spytwenohone.util.IngredientUtils;

import java.util.function.Consumer;

public class SpyTooRecipeProvider extends RecipeProvider implements IConditionBuilder {

	public SpyTooRecipeProvider(PackOutput output) {
		super(output);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> writer) {
		SmeltineryRecipeBuilder.create(Ingredient.of(Items.IRON_INGOT), Ingredient.of(ItemTags.DIAMOND_ORES), new ItemStack(Items.DIAMOND, 3)).addMaterial(Ingredient.of(SpyTooItemTags.STONE_MATERIALS)).setProcessTime(1800).save(writer, new ResourceLocation(SpyTwentyOhOne.MOD_ID, "smeltinery_ore_diamond"));
		smeltineryOre("copper", writer, ItemTags.COPPER_ORES, Items.COPPER_INGOT);
		smeltineryOre("gold", writer, ItemTags.GOLD_ORES, Items.GOLD_INGOT);
		smeltineryOre("iron", writer, ItemTags.IRON_ORES, Items.IRON_INGOT);
		
		smeltineryRawMetal("copper", writer, Items.RAW_COPPER, Items.COPPER_INGOT);
		smeltineryRawMetal("gold", writer, Items.RAW_GOLD, Items.GOLD_INGOT);
		smeltineryRawMetal("iron", writer, Items.RAW_IRON, Items.IRON_INGOT);
		
		SmeltineryRecipeBuilder.create(Ingredient.of(Items.IRON_INGOT), Ingredient.of(Items.DIAMOND), new ItemStack(Items.DIAMOND, 2)).addMaterial(IngredientUtils.ofValues(new IngredientUtils.TagCountValue(SpyTooItemTags.PURIFIED_STONE_MATERIALS, 2))).setProcessTime(1200).save(writer, new ResourceLocation(SpyTwentyOhOne.MOD_ID, "smeltinery_diamond"));
		smeltineryIngot("copper", writer, Items.COPPER_INGOT);
		smeltineryIngot("gold", writer, Items.GOLD_INGOT);
		smeltineryIngot("iron", writer, Items.IRON_INGOT);
	}
	
	private static void smeltineryOre(String metalName, Consumer<FinishedRecipe> writer, TagKey<Item> tagOres, Item metal) {
		Ingredient matIngredient = Ingredient.of(SpyTooItemTags.STONE_MATERIALS);
		var builder = SmeltineryRecipeBuilder.create(matIngredient, Ingredient.of(tagOres), new ItemStack(metal, 3), 1200, 500, 0.0f);
		builder.save(writer, new ResourceLocation(SpyTwentyOhOne.MOD_ID, "smeltinery_ore_" + metalName));
	}

	private static void smeltineryRawMetal(String metalName, Consumer<FinishedRecipe> writer, Item rawMetal, Item metal) {
		Ingredient matIngredient = Ingredient.of(SpyTooItemTags.PURIFIED_STONE_MATERIALS);
		var builder = SmeltineryRecipeBuilder.create(matIngredient, Ingredient.of(rawMetal), new ItemStack(metal, 3), 800, 500, 0.0f);
		builder.save(writer, new ResourceLocation(SpyTwentyOhOne.MOD_ID, "smeltinery_raw_" + metalName));
	}

	private static void smeltineryIngot(String metalName, Consumer<FinishedRecipe> writer, Item metal) {
		smeltineryIngot(metalName, writer, metal, 600, 500, 0.0f);
	}

	protected static void smeltineryIngot(String metalName, Consumer<FinishedRecipe> writer, Item metal, int processTime, int fluidConsumption, float exp) {
		Ingredient matIngredient = IngredientUtils.ofValues(new IngredientUtils.TagCountValue(SpyTooItemTags.PURIFIED_STONE_MATERIALS, 2));
		var builder = SmeltineryRecipeBuilder.create(matIngredient, Ingredient.of(metal), new ItemStack(metal, 2), processTime, fluidConsumption, exp);
		builder.save(writer, new ResourceLocation(SpyTwentyOhOne.MOD_ID, "smeltinery_ingot_" + metalName));
	}
}
