package spyeedy.mods.spytwenohone.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class CreativeTabRegistry {
	/*@ExpectPlatform
	public static void addToTab(CreativeModeTab tab, Consumer<ItemGroupEntries> entriesConsumer) {
		throw new AssertionError();
	}*/

	@ExpectPlatform
	public static void addToTab(ResourceKey<CreativeModeTab> tab, Consumer<ItemGroupEntries> entriesConsumer) {
		throw new AssertionError();
	}

	public interface ItemGroupEntries {
		void add(ItemLike... items);

		void addBefore(ItemLike beforeFirst, ItemLike... items);

		void addAfter(ItemLike afterLast, ItemLike... items);
	}
}
