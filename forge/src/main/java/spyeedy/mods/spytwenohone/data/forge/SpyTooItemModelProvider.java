package spyeedy.mods.spytwenohone.data.forge;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.item.SpyTooItems;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

public class SpyTooItemModelProvider extends ItemModelProvider {

	public SpyTooItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, SpyTwentyOhOne.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		this.blockItem(SpyTooItems.SMELT_FINERY);
	}

	public void blockItem(RegistrySupplier<Item> item) {
		this.withExistingParent(item.getId().getPath(), new ResourceLocation(item.getId().getNamespace(), "block/" + item.getId().getPath()));
	}
}
