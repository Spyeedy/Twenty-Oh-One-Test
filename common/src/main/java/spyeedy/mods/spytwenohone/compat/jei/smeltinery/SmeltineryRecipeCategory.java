package spyeedy.mods.spytwenohone.compat.jei.smeltinery;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.block.SpyTooBlocks;
import spyeedy.mods.spytwenohone.block.entity.SmeltineryBlockEntity;
import spyeedy.mods.spytwenohone.compat.jei.util.AbstractFlameCategory;
import spyeedy.mods.spytwenohone.item.SpyTooItems;
import spyeedy.mods.spytwenohone.recipe.SmeltineryRecipe;
import spyeedy.mods.spytwenohone.util.RenderUtils;

public class SmeltineryRecipeCategory extends AbstractFlameCategory<SmeltineryRecipe> {

	public static final RecipeType<SmeltineryRecipe> RECIPE_TYPE = RecipeType.create(SpyTwentyOhOne.MOD_ID, "smeltinery_recipe", SmeltineryRecipe.class);
	private static final ResourceLocation GUI_TEX = new ResourceLocation(SpyTwentyOhOne.MOD_ID, "textures/gui/jei/smeltinery.png");
	private static int GUI_WIDTH = 141;
	private static int GUI_HEIGHT = 72;

	private final IDrawable background;
	private final IDrawable icon;

	private final IDrawable fluidMarkings;
	private final TextureAtlasSprite waterSprite;
	private final IDrawableStatic staticArrow;
	private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

	public SmeltineryRecipeCategory(IRecipeCategoryRegistration registration) {
		super(registration.getJeiHelpers().getGuiHelper(), GUI_TEX, GUI_WIDTH, 0, 14, 14);
		var guiHelper = registration.getJeiHelpers().getGuiHelper();
		this.background = guiHelper.createDrawable(GUI_TEX, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		this.icon = guiHelper.createDrawableItemStack(SpyTooItems.SMELT_FINERY.get().getDefaultInstance());
		this.fluidMarkings = guiHelper.createDrawable(GUI_TEX, GUI_WIDTH, 0, 18, 51);
		this.waterSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(RenderUtils.FLUID_STILL);

		this.staticArrow = guiHelper.createDrawable(GUI_TEX, GUI_WIDTH + 18, 0, 24, 17);
		this.cachedArrows = CacheBuilder.newBuilder()
				.maximumSize(25)
				.build(new CacheLoader<>() {
					@Override
					public IDrawableAnimated load(Integer cookTime) {
						return guiHelper.drawableBuilder(GUI_TEX, GUI_WIDTH + 18, 0, 24, 17)
								.buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
					}
				});
	}

	@Override
	public RecipeType<SmeltineryRecipe> getRecipeType() {
		return RECIPE_TYPE;
	}

	@Override
	public Component getTitle() {
		return SpyTooBlocks.SMELT_FINERY.get().getName();
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
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 19).addIngredients(recipe.getMetal());

		for (int i = 0; i < recipe.getMaterials().size(); i++) {
			builder.addSlot(RecipeIngredientRole.INPUT, 28, 1 + i * 18).addIngredients(recipe.getMaterials().get(i));
		}

		builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 19).addItemStack(recipe.getResultItem(null));
	}

	@Override
	public void draw(SmeltineryRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;

		guiGraphics.blit(GUI_TEX, 74, 52, GUI_WIDTH + 18, 17, (int) ((recipe.getEnergyRequired() / (float) SmeltineryBlockEntity.MAX_ENERGY) * 46), 8);
		if (mouseX >= 74 && mouseX < 120 && mouseY >= 52 && mouseY < 60) {
			guiGraphics.renderTooltip(font, Component.translatable("gui.spytoo.smeltinery.energy", recipe.getEnergyRequired(), SmeltineryBlockEntity.MAX_ENERGY), (int) mouseX, (int) mouseY);
		}

		if (recipe.getFluidConsumption() > 0) {
			int maxHeight = 49;
			int fluidHeight = (int)(recipe.getFluidConsumption() / (float) SmeltineryBlockEntity.MAX_FLUID * maxHeight);
			RenderUtils.renderGuiFluid(guiGraphics, waterSprite, 1, 11 + maxHeight - fluidHeight, 0, 16, fluidHeight, RenderUtils.COLOR_WATER_FLUID);
		}
		fluidMarkings.draw(guiGraphics, 0, 10);

		if (mouseX >= 1 && mouseX < 17 && mouseY >= 11 && mouseY < 60) {
			guiGraphics.renderTooltip(font, Component.translatable("gui.spytoo.smeltinery.fluid_level", recipe.getFluidConsumption(), SmeltineryBlockEntity.MAX_FLUID), (int) mouseX, (int) mouseY);
		}

//		animatedFlame.draw(guiGraphics, 60, 38);

		int cookTime = recipe.getProcessTime();
		if (cookTime > 0) {
			int cookTimeSeconds = cookTime / 20;
			Component timeString = Component.translatable("jei.spytoo.smeltinery.time.seconds", cookTimeSeconds);
			int stringWidth = font.width(timeString);
			guiGraphics.drawString(font, timeString, getWidth() - stringWidth, this.getHeight() - 9, 0xFF808080, false);
		}

		IDrawable arrow = staticArrow;
		if (recipe.getProcessTime() > 0) {
			arrow = this.cachedArrows.getUnchecked(recipe.getProcessTime());
		}
		arrow.draw(guiGraphics, 84, 18);
	}
}
