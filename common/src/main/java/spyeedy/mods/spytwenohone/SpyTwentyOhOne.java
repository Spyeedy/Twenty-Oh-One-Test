package spyeedy.mods.spytwenohone;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import spyeedy.mods.spytwenohone.block.SpyTooBlocks;
import spyeedy.mods.spytwenohone.block.entity.SpyTooBlockEntities;
import spyeedy.mods.spytwenohone.compat.palladium.SpyTooAbilities;
import spyeedy.mods.spytwenohone.container.SpyTooContainers;
import spyeedy.mods.spytwenohone.item.SpyTooItems;
import spyeedy.mods.spytwenohone.platform.Platform;
import spyeedy.mods.spytwenohone.recipe.SpyTooRecipes;

public final class SpyTwentyOhOne {
	public static final String MOD_ID = "spytoo";

	public static final Logger LOGGER = LogUtils.getLogger();

	public static void init() {
		SpyTooBlocks.BLOCKS.submit();
		SpyTooItems.ITEMS.submit();
		SpyTooItems.addCreativeTabs();
		SpyTooBlockEntities.BLOCK_ENTITY_TYPES.submit();

		SpyTooContainers.MENU_TYPES.submit();

		SpyTooRecipes.RECIPE_TYPES.submit();
		SpyTooRecipes.RECIPE_SERIALIZERS.submit();

		if (Platform.isModLoaded("palladium")) {
			SpyTooAbilities.ABILITIES.register();
		}
	}
}
