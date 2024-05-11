package spyeedy.mods.spytwenohone.data.forge;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;
import spyeedy.mods.spytwenohone.recipe.SpyTooRecipes;

import java.util.List;
import java.util.function.Consumer;

public class SmeltineryRecipeBuilder {

	private final NonNullList<Ingredient> materials = NonNullList.create();
	private final Ingredient metal;
	private final ItemStack result;
	private int processTime;
	private int fluidConsumption;
	private float exp;

	private SmeltineryRecipeBuilder(Ingredient requiredMaterial, Ingredient metal, ItemStack result, int processTime, int fluidConsumption, float exp) {
		this.materials.add(requiredMaterial);
		this.metal = metal;
		this.result = result;
		this.processTime = processTime;
		this.fluidConsumption = fluidConsumption;
		this.exp = exp;
	}

	public SmeltineryRecipeBuilder setProcessTime(int processTime) {
		this.processTime = processTime;
		return this;
	}

	public SmeltineryRecipeBuilder setFluidConsumption(int fluidConsumption) {
		this.fluidConsumption = fluidConsumption;
		return this;
	}

	public SmeltineryRecipeBuilder setExp(float exp) {
		this.exp = exp;
		return this;
	}

	public SmeltineryRecipeBuilder addMaterial(Ingredient... ingredients) {
		this.materials.addAll(List.of(ingredients));
		return this;
	}

	public static SmeltineryRecipeBuilder create(Ingredient requiredMaterial, Ingredient metal, ItemStack result) {
		return create(requiredMaterial, metal, result, 800, 500, 0.0f);
	}

	public static SmeltineryRecipeBuilder create(Ingredient requiredMaterial, Ingredient metal, ItemStack result, int processTime, int fluidConsumption, float exp) {
		return new SmeltineryRecipeBuilder(requiredMaterial, metal, result, processTime, fluidConsumption, exp);
	}

	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipeId) {
		consumer.accept(new Result(recipeId, materials, metal, result, processTime, fluidConsumption, exp));
	}

	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final NonNullList<Ingredient> materials;
		private final Ingredient metal;
		private final ItemStack result;
		private final int processTime;
		private final int fluidConsumption;
		private final float exp;

		public Result(ResourceLocation id, NonNullList<Ingredient> materials, Ingredient metal, ItemStack result, int processTime, int fluidConsumption, float exp) {
			this.id = id;
			this.materials = materials;
			this.metal = metal;
			this.result = result;
			this.processTime = processTime;
			this.fluidConsumption = fluidConsumption;
			this.exp = exp;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			JsonArray jsonMats = new JsonArray();
			for (Ingredient ingredient : materials) {
				jsonMats.add(ingredient.toJson());
			}
			json.add("materials", jsonMats);

			json.add("metal", metal.toJson());

			JsonObject jsonResult = new JsonObject();
			jsonResult.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result.getItem()).toString());
			if (this.result.getCount() > 1) {
				jsonResult.addProperty("count", this.result.getCount());
			}
			json.add("result", jsonResult);

			json.addProperty("process_time", processTime);
			json.addProperty("fluid_consumption", fluidConsumption);
			json.addProperty("experience", exp);
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return SpyTooRecipes.SMELTINERY_SERIALIZER.get();
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}
}