package spyeedy.mods.spytwenohone.util;

import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IngredientUtils {
	// Allow "count" in an ingredient JSON
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
			int count = GsonHelper.getAsInt(json, "count", 1);

			if (count < 1) {
				throw new JsonSyntaxException("Invalid output count: " + count);
			} else {
				TagKey<Item> tagKey = TagKey.create(Registries.ITEM, resourceLocation);
				return new TagCountValue(tagKey, count);
			}
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

	public static class TagCountValue extends Ingredient.TagValue {
		private final int count;

		public TagCountValue(TagKey<Item> tag, int count) {
			super(tag);
			this.count = count;
		}

		public int getCount() {
			return count;
		}

		@Override
		public Collection<ItemStack> getItems() {
			List<ItemStack> list = Lists.newArrayList();
			Iterator var2 = BuiltInRegistries.ITEM.getTagOrEmpty(this.tag).iterator();

			while(var2.hasNext()) {
				Holder<Item> holder = (Holder)var2.next();
				list.add(new ItemStack(holder, count));
			}

			return list;
		}

		@Override
		public JsonObject serialize() {
			JsonObject json = super.serialize();
			json.addProperty("count", count);
			return json;
		}
	}
}
