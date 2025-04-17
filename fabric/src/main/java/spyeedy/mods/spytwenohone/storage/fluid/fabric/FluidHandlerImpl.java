package spyeedy.mods.spytwenohone.storage.fluid.fabric;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;

public class FluidHandlerImpl extends SingleVariantStorage<FluidVariant> {

	@Override
	protected FluidVariant getBlankVariant() {
		return null;
	}

	@Override
	protected long getCapacity(FluidVariant variant) {
		return 0;
	}
}
