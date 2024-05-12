package spyeedy.mods.spytwenohone.compat.jei.smeltinery;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.item.SpyTooItems;
import spyeedy.mods.spytwenohone.recipe.SmeltineryRecipe;

public class SmeltineryRecipeCategory implements IRecipeCategory<SmeltineryRecipe> {

	public static final RecipeType<SmeltineryRecipe> RECIPE_TYPE = RecipeType.create(SpyTwentyOhOne.MOD_ID, "smeltinery_recipe", SmeltineryRecipe.class);
	private static final ResourceLocation GUI_TEX = new ResourceLocation(SpyTwentyOhOne.MOD_ID, "textures/gui/jei/smeltinery.png");
	private static int GUI_WIDTH = 141;
	private static int GUI_HEIGHT = 72;

	private IDrawable background;
	private IDrawable icon;

	public SmeltineryRecipeCategory(IRecipeCategoryRegistration registration) {
		var guiHelper = registration.getJeiHelpers().getGuiHelper();
		this.background = guiHelper.createDrawable(GUI_TEX, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		this.icon = guiHelper.createDrawableItemStack(SpyTooItems.SMELT_FINERY.get().getDefaultInstance());
	}

	@Override
	public RecipeType<SmeltineryRecipe> getRecipeType() {
		return RECIPE_TYPE;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("jei.spytoo.smeltinery.title");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, SmeltineryRecipe recipe, IFocusGroup focuses) {
		for (int i = 0; i < recipe.getMaterials().size(); i++) {
			builder.addSlot(RecipeIngredientRole.INPUT, 28, 1 + i * 18).addIngredients(recipe.getMaterials().get(i));
		}
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 28).addIngredients(recipe.getMetal());

		builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 28).addItemStack(recipe.getResultItem(null));
	}

	@Override
	public void draw(SmeltineryRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
	}
}
