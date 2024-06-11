package spyeedy.mods.spytwenohone.compat.palladium;

import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;

public class SpyTooAbilities {
	public static final DeferredRegister<Ability> ABILITIES = DeferredRegister.create(SpyTwentyOhOne.MOD_ID, Ability.REGISTRY);

	public static final RegistrySupplier<AdjustableAttributeModifierAbility> ADJUSTABLE_ATTRIBUTE_MODIFIER = ABILITIES.register("adjustable_attribute_modifier", AdjustableAttributeModifierAbility::new);
}
