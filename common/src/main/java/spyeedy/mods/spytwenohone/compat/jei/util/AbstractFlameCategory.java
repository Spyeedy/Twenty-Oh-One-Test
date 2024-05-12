package spyeedy.mods.spytwenohone.compat.jei.util;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractFlameCategory<T> implements IRecipeCategory<T> {
	protected final IDrawableStatic staticFlame;
	protected final IDrawable animatedFlame;

	public AbstractFlameCategory(IGuiHelper guiHelper, ResourceLocation flameTex, int flameU, int flameV, int uWidth, int vHeight) {
		this.staticFlame = guiHelper.createDrawable(flameTex, flameU, flameV, uWidth, vHeight);
		this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
	}
}
