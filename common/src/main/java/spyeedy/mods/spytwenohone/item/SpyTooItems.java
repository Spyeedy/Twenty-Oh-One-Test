package spyeedy.mods.spytwenohone.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.block.SpyTooBlocks;
import spyeedy.mods.spytwenohone.registry.CoreRegister;
import spyeedy.mods.spytwenohone.registry.CreativeTabRegistry;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

public class SpyTooItems {
	public static final CoreRegister<Item> ITEMS = CoreRegister.create(SpyTwentyOhOne.MOD_ID, Registries.ITEM);

	public static final RegistrySupplier<Item> SMELT_FINERY = ITEMS.register("smeltinery", () -> new BlockItem(SpyTooBlocks.SMELT_FINERY.get(), new Item.Properties()));

	public static void addCreativeTabs() {
		CreativeTabRegistry.addToTab(CreativeModeTabs.FUNCTIONAL_BLOCKS, entries -> entries.addAfter(Items.BLAST_FURNACE, SpyTooItems.SMELT_FINERY.get()));
	}
}
