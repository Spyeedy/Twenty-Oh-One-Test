package spyeedy.mods.spytwenohone.compat.palladium;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AttributeModifierAbility;
import net.threetag.palladium.util.property.DoubleProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;
import net.threetag.palladium.util.property.SyncType;

public class AdjustableAttributeModifierAbility extends AttributeModifierAbility {

	public static final PalladiumProperty<Double> AMOUNT_VARY = new DoubleProperty("amount_vary").sync(SyncType.SELF);

	@Override
	public void registerUniqueProperties(PropertyManager manager) {
		super.registerUniqueProperties(manager);

		manager.register(AMOUNT_VARY, null);
	}

	@Override
	public void firstTick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
		super.firstTick(entity, entry, holder, enabled);

		if (enabled) {
			if (entry.getProperty(AMOUNT_VARY) == null) {
				entry.setUniqueProperty(AMOUNT_VARY, entry.getProperty(AMOUNT));
			}
		}
	}

	@Override
	public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
		if (enabled) {
			Attribute attribute = entry.getProperty(ATTRIBUTE);
			AttributeInstance instance = entity.getAttribute(attribute);

			if (instance == null || entity.level().isClientSide) {
				return;
			}

			java.util.UUID uuid = entry.getProperty(UUID);
			AttributeModifier modifier = instance.getModifier(uuid);

			// Remove modifier if amount or operation dont match
			if (modifier != null && (modifier.getAmount() != entry.getProperty(AMOUNT_VARY) || modifier.getOperation().toValue() != entry.getProperty(OPERATION))) {
				instance.removeModifier(uuid);
				modifier = null;
			}

			if (modifier == null) {
				modifier = new AttributeModifier(uuid, entry.getConfiguration().getDisplayName().getString(), entry.getProperty(AMOUNT_VARY), AttributeModifier.Operation.fromValue(entry.getProperty(OPERATION)));
				instance.addTransientModifier(modifier);
			}
		} else {
			this.lastTick(entity, entry, holder, false);
		}
	}

	@Override
	public String getDocumentationDescription() {
		return "An extension of the Attribute Modifier ability from Palladium that allows addon pack developers to utilise KubeJS to adjust the \"amount\" value by modifying the unique property \"amount_vary\"";
	}
}
