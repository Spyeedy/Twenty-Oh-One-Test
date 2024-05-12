package spyeedy.mods.spytwenohone.registry.forge;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.registry.CreativeTabRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = SpyTwentyOhOne.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTabRegistryImpl {
	private static final Map<ResourceKey<CreativeModeTab>, Consumer<CreativeTabRegistry.ItemGroupEntries>> TAB_ITEMS = new HashMap<>();

	public static void addToTab(ResourceKey<CreativeModeTab> tab, Consumer<CreativeTabRegistry.ItemGroupEntries> entriesConsumer) {
		TAB_ITEMS.put(tab, entriesConsumer);
	}

	@SubscribeEvent
	public static void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
		for (var entry : TAB_ITEMS.entrySet()) {
			if (entry.getKey() == event.getTabKey()) {
				entry.getValue().accept(new ItemGroupEntriesWrapper(event));
			}
		}
	}

	public record ItemGroupEntriesWrapper(BuildCreativeModeTabContentsEvent event) implements CreativeTabRegistry.ItemGroupEntries {

		public ItemStack findFirst(ItemLike itemLike) {
			for (var entry : this.event.getEntries()) {
				if (entry.getKey().is(itemLike.asItem())) {
					return entry.getKey();
				}
			}

			return null;
		}

		public ItemStack findLast(ItemLike itemLike) {
			ItemStack stack = null;

			for (var entry : this.event.getEntries()) {
				if (entry.getKey().is(itemLike.asItem())) {
					stack = entry.getKey();
				}
			}

			return stack;
		}

		@Override
		public void add(ItemLike... items) {
			for (ItemLike item : items) {
				event.accept(item);
			}
		}

		@Override
		public void addBefore(ItemLike beforeFirst, ItemLike... items) {
			for (var item : items)
				this.event.getEntries().putBefore(findFirst(beforeFirst), item.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		}

		@Override
		public void addAfter(ItemLike afterLast, ItemLike... items) {
			for (var item : items) {
				this.event.getEntries().putAfter(findLast(afterLast), item.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			}
		}
	}
}
