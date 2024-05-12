package spyeedy.mods.spytwenohone.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.compat.jei.smeltinery.SmeltineryRecipeCategory;
import spyeedy.mods.spytwenohone.item.SpyTooItems;
import spyeedy.mods.spytwenohone.recipe.SpyTooRecipes;

@JeiPlugin
public class SpyTooJeiPlugin implements IModPlugin {

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new SmeltineryRecipeCategory(registration));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

		registration.addRecipes(SmeltineryRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(SpyTooRecipes.SMELTINERY_RECIPE.get()));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(SpyTooItems.SMELT_FINERY.get().getDefaultInstance(), SmeltineryRecipeCategory.RECIPE_TYPE);
	}

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(SpyTwentyOhOne.MOD_ID, "jei_plugin");
	}
}
