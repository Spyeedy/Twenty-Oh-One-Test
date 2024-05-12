package spyeedy.mods.spytwenohone.compat.wthit;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import spyeedy.mods.spytwenohone.block.entity.SmeltineryBlockEntity;

public class SpyTooWailaPlugin implements IWailaPlugin {
	@Override
	public void register(IRegistrar registrar) {
		registrar.addBlockData(SmeltineryBlockProvider.INSTANCE, SmeltineryBlockEntity.class);
	}
}
