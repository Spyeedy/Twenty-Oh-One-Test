package spyeedy.mods.spytwenohone.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.registry.CoreRegister;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

public class SpyTooRecipes {
	public static final CoreRegister<RecipeType<?>> RECIPE_TYPES = CoreRegister.create(SpyTwentyOhOne.MOD_ID, Registries.RECIPE_TYPE);
	public static final CoreRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = CoreRegister.create(SpyTwentyOhOne.MOD_ID, Registries.RECIPE_SERIALIZER);

	public static final RegistrySupplier<RecipeType<SmeltineryRecipe>> SMELTINERY_RECIPE = registerRecipe("smeltinery");

	public static final RegistrySupplier<RecipeSerializer<SmeltineryRecipe>> SMELTINERY_SERIALIZER = RECIPE_SERIALIZERS.register("smeltinery", SmeltineryRecipe.Serializer::new);

	private static <T extends Recipe<?>> RegistrySupplier<RecipeType<T>> registerRecipe(String id) {
		return RECIPE_TYPES.register(id, () -> new RecipeType<>() {
			@Override
			public String toString() {
				return id;
			}
		});
	}
}
