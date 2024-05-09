package spyeedy.mods.spytwenohone.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import spyeedy.mods.spytwenohone.block.entity.SmeltineryBlockEntity;

public class SmeltineryRecipe implements Recipe<Container> {

	private final ResourceLocation id;
	private final NonNullList<Ingredient> materials;
	private final Ingredient metal;
	private final ItemStack result;

	public SmeltineryRecipe(ResourceLocation id, NonNullList<Ingredient> materials, Ingredient metal, ItemStack result) {
		this.id = id;
		this.materials = materials;
		this.metal = metal;
		this.result = result;
	}

	@Override
	public boolean matches(Container container, Level level) {
		if (!metal.test(container.getItem(SmeltineryBlockEntity.SLOT_METAL))) return false;

		for (int i = 0; i < materials.size(); i++) {
			if (!materials.get(i).test(container.getItem(SmeltineryBlockEntity.SLOT_MATERIAL_START + i)))
				return false;
		}

		return true;
	}

	@Override
	public ItemStack assemble(Container container, RegistryAccess registryAccess) {
		return this.getResultItem(registryAccess).copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return result;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpyTooRecipes.SMELTINERY_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return SpyTooRecipes.SMELTINERY_RECIPE.get();
	}

	public static class Serializer implements RecipeSerializer<SmeltineryRecipe> {

		@Override
		public SmeltineryRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			NonNullList<Ingredient> materials = NonNullList.create();

			JsonArray jsonMaterials = GsonHelper.getAsJsonArray(json, "materials");
			for(int i = 0; i < jsonMaterials.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(jsonMaterials.get(i), false);
				materials.add(ingredient);
			}

			if (materials.isEmpty())
				throw new JsonParseException("No materials for smeltinery recipe");
			else if (materials.size() > SmeltineryBlockEntity.MAX_MATERIALS)
				throw new JsonParseException("Too many materials for smeltinery recipe. The maximum is " + SmeltineryBlockEntity.MAX_MATERIALS);

			Ingredient metal = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "metal"));
			ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

			return new SmeltineryRecipe(recipeId, materials, metal, result);
		}

		@Override
		public SmeltineryRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			int matCount = buffer.readVarInt();
			NonNullList<Ingredient> materials = NonNullList.withSize(matCount, Ingredient.EMPTY);
			for (int i = 0; i < matCount; i++) {
				materials.set(i, Ingredient.fromNetwork(buffer));
			}

			Ingredient metal = Ingredient.fromNetwork(buffer);
			ItemStack result = buffer.readItem();

			return new SmeltineryRecipe(recipeId, materials, metal, result);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, SmeltineryRecipe recipe) {
			buffer.writeVarInt(recipe.materials.size());
			for (Ingredient ingredient : recipe.materials) {
				ingredient.toNetwork(buffer);
			}

			recipe.metal.toNetwork(buffer);
			buffer.writeItem(recipe.getResultItem(null));
		}
	}
}
