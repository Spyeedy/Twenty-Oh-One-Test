package spyeedy.mods.spytwenohone.container;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.registry.CoreRegister;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

public class SpyTooContainers {

	public static CoreRegister<MenuType<?>> MENU_TYPES = CoreRegister.create(SpyTwentyOhOne.MOD_ID, Registries.MENU);

	public static final RegistrySupplier<MenuType<SmeltineryContainer>> SMELTINERY = MENU_TYPES.register("smeltinery", () -> new MenuType<>(SmeltineryContainer::new, FeatureFlags.VANILLA_SET));
}
