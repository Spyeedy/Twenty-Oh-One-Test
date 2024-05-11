package spyeedy.mods.spytwenohone.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;

public class SpyTooItemTags {
	public static final TagKey<Item> PURIFIED_STONE_MATERIALS = tag("purified_stone_materials");
	public static final TagKey<Item> STONE_MATERIALS = tag("stone_materials");

	private static TagKey<Item> tag(String tagId) {
		return new TagKey<>(Registries.ITEM, new ResourceLocation(SpyTwentyOhOne.MOD_ID, tagId));
	}
}
