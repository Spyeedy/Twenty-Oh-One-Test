package spyeedy.mods.spytwenohone.util;

import com.google.gson.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IngredientUtils {
	public static Ingredient ingredientFromJson(JsonElement jsonElement) {
		if (jsonElement != null && !jsonElement.isJsonNull()) {
			if (jsonElement.isJsonObject()) {
				return Ingredient.fromValues(Stream.of(valueFromJson(jsonElement.getAsJsonObject())));
			} else if (jsonElement.isJsonArray()) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				if (jsonArray.isEmpty()) {
					throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
				} else {
					return Ingredient.fromValues(StreamSupport.stream(jsonArray.spliterator(), false).map((json) -> valueFromJson(GsonHelper.convertToJsonObject(json, "item"))));
				}
			} else {
				throw new JsonSyntaxException("Expected item to be object or array of objects");
			}
		} else {
			throw new JsonSyntaxException("Item cannot be null");
		}
	}

	public static Ingredient.Value valueFromJson(JsonObject json) {
		if (json.has("item") && json.has("tag")) {
			throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
		} else if (json.has("item")) {
			return new Ingredient.ItemValue(ShapedRecipe.itemStackFromJson(json));
		} else if (json.has("tag")) {
			ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(json, "tag"));
			TagKey<Item> tagKey = TagKey.create(Registries.ITEM, resourceLocation);
			return new Ingredient.TagValue(tagKey);
		} else {
			throw new JsonParseException("An ingredient entry needs either a tag or an item");
		}
	}

	public static boolean testStackDetails(Ingredient ingredient, ItemStack stack) {
		if (stack == null) {
			return false;
		} else if (ingredient.isEmpty()) {
			return stack.isEmpty();
		} else {
			ItemStack[] stackArr = ingredient.getItems();

			for(int i = 0; i < stackArr.length; ++i) {
				ItemStack itemStack = stackArr[i];
				if (itemStack.is(stack.getItem()) && stack.getCount() >= itemStack.getCount()) {
					return true;
				}
			}

			return false;
		}
	}

	public static void shrinkStack(Ingredient ingredient, ItemStack stack) {
		if (stack == null || ingredient.isEmpty()) return;

		ItemStack[] stackArr = ingredient.getItems();

		for (ItemStack itemStack : stackArr) {
			if (itemStack.is(stack.getItem()) && stack.getCount() >= itemStack.getCount()) {
				stack.shrink(itemStack.getCount());
				break;
			}
		}
	}
}
