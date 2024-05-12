package spyeedy.mods.spytwenohone.registry.fabric;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import spyeedy.mods.spytwenohone.registry.CreativeTabRegistry;

import java.util.function.Consumer;

public class CreativeTabRegistryImpl {
	public static void addToTab(ResourceKey<CreativeModeTab> tab, Consumer<CreativeTabRegistry.ItemGroupEntries> entriesConsumer) {
		ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> entriesConsumer.accept(new ItemGroupEntriesWrapper(entries)));
	}

	public record ItemGroupEntriesWrapper(FabricItemGroupEntries entries) implements CreativeTabRegistry.ItemGroupEntries {
		@Override
		public void add(ItemLike... items) {
			for (ItemLike item : items) {
				entries.accept(item);
			}
		}

		@Override
		public void addBefore(ItemLike beforeFirst, ItemLike... items) {
			entries.addBefore(beforeFirst, items);
		}

		@Override
		public void addAfter(ItemLike afterLast, ItemLike... items) {
			entries.addAfter(afterLast, items);
		}
	}
}
