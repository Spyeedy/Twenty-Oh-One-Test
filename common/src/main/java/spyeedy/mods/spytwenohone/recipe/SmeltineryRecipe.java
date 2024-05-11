package spyeedy.mods.spytwenohone.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import spyeedy.mods.spytwenohone.block.entity.SmeltineryBlockEntity;
import spyeedy.mods.spytwenohone.util.IngredientUtils;

public class SmeltineryRecipe implements Recipe<SmeltineryBlockEntity> {

	private final ResourceLocation id;
	private final NonNullList<Ingredient> materials;
	private final Ingredient metal;
	private final ItemStack result;
	private final float experience;
	private final int processTime;
	private final int fluidConsumption;

	public SmeltineryRecipe(ResourceLocation id, NonNullList<Ingredient> materials, Ingredient metal, ItemStack result, float experience, int processTime, int fluidConsumption) {
		this.id = id;
		this.materials = materials;
		this.metal = metal;
		this.result = result;
		this.experience = experience;
		this.processTime = processTime;
		this.fluidConsumption = fluidConsumption;
	}

	public int getProcessTime() {
		return processTime;
	}

	public float getExperience() {
		return experience;
	}

	public int getFluidConsumption() {
		return fluidConsumption;
	}

	@Override
	public boolean matches(SmeltineryBlockEntity container, Level level) {
		if (!metal.test(container.getItem(SmeltineryBlockEntity.SLOT_METAL))) return false;

		int matIdx = 0;
		for (int slotIdx = SmeltineryBlockEntity.SLOT_MATERIAL_START; slotIdx < SmeltineryBlockEntity.SLOT_RESULT && matIdx < materials.size(); slotIdx++) {
			ItemStack slotItem = container.getItem(slotIdx);
			if (!slotItem.isEmpty()) {
				if (!IngredientUtils.testStackDetails(materials.get(matIdx++), slotItem)) {
					return false;
				}
			}
		}

		return matIdx >= materials.size();
	}

	@Override
	public ItemStack assemble(SmeltineryBlockEntity container, RegistryAccess registryAccess) {
		return this.getResultItem(registryAccess).copy();
	}

	public void consumeInputs(SmeltineryBlockEntity container, Level level) {
		if (!level.isClientSide) {
			IngredientUtils.shrinkStack(metal, container.getItem(SmeltineryBlockEntity.SLOT_METAL));

			int matIdx = 0;
			for (int slotIdx = SmeltineryBlockEntity.SLOT_MATERIAL_START; slotIdx < SmeltineryBlockEntity.SLOT_RESULT && matIdx < materials.size(); slotIdx++) {
				ItemStack slotItem = container.getItem(slotIdx);
				if (!slotItem.isEmpty()) {
					Ingredient matIngredient = materials.get(matIdx++);
					if (IngredientUtils.testStackDetails(matIngredient, slotItem)) {
						IngredientUtils.shrinkStack(matIngredient, slotItem);
					}
				}
			}
		}
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
			for (int i = 0; i < jsonMaterials.size(); ++i) {
				Ingredient ingredient = IngredientUtils.ingredientFromJson(jsonMaterials.get(i));
				materials.add(ingredient);
			}

			if (materials.isEmpty())
				throw new JsonParseException("No materials for smeltinery recipe");
			else if (materials.size() > SmeltineryBlockEntity.MAX_MATERIALS)
				throw new JsonParseException("Too many materials for smeltinery recipe. The maximum is " + SmeltineryBlockEntity.MAX_MATERIALS);

			// Normal ingredient...
			Ingredient metal = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "metal"), false);
			ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

			float exp = GsonHelper.getAsFloat(json, "experience", 0.0f);
			int processTime = GsonHelper.getAsInt(json, "process_time", 800);
			int fluidUseRate = GsonHelper.getAsInt(json, "fluid_consumption", 500);

			return new SmeltineryRecipe(recipeId, materials, metal, result, exp, processTime, fluidUseRate);
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
			float exp = buffer.readFloat();
			int processTime = buffer.readInt();
			int fluidUseRate = buffer.readInt();

			return new SmeltineryRecipe(recipeId, materials, metal, result, exp, processTime, fluidUseRate);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, SmeltineryRecipe recipe) {
			buffer.writeVarInt(recipe.materials.size());
			for (Ingredient ingredient : recipe.materials) {
				ingredient.toNetwork(buffer);
			}

			recipe.metal.toNetwork(buffer);
			buffer.writeItem(recipe.getResultItem(null));

			buffer.writeFloat(recipe.experience);
			buffer.writeInt(recipe.processTime);
			buffer.writeInt(recipe.fluidConsumption);
		}
	}
}
