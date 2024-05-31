package spyeedy.mods.spytwenohone.compat.wthit;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.data.EnergyData;
import mcp.mobius.waila.api.data.FluidData;
import mcp.mobius.waila.api.data.ItemData;
import mcp.mobius.waila.api.data.ProgressData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import spyeedy.mods.spytwenohone.block.entity.SmeltineryBlockEntity;

public class SmeltineryBlockProvider implements IDataProvider<SmeltineryBlockEntity> {

	public static final SmeltineryBlockProvider INSTANCE = new SmeltineryBlockProvider();

	@Override
	public void appendData(IDataWriter data, IServerAccessor<SmeltineryBlockEntity> accessor, IPluginConfig config) {
		data.blockAll(ItemData.class);
		data.add(FluidData.class, result -> result.add(FluidData.of(FluidData.Unit.MILLIBUCKETS, 1).add(Fluids.WATER, null, accessor.getTarget().getFluidAmount(), SmeltineryBlockEntity.MAX_FLUID)));
		data.add(EnergyData.class, energyDataResult -> energyDataResult.add(EnergyData.of(accessor.getTarget().getEnergy(), SmeltineryBlockEntity.MAX_ENERGY)));
		data.add(ProgressData.class, result -> {
			var smeltinery = accessor.getTarget();
			var smeltineryRecipe = smeltinery.getRecipe();

			if (smeltinery.getProgress() > 0 && smeltineryRecipe != null) {
				result.add(ProgressData
						.ratio((float) smeltinery.getProgress() / smeltinery.getMaxProgress())
						.input(new ItemStack(smeltinery.getItem(SmeltineryBlockEntity.SLOT_METAL).getItem()))
						.output(smeltineryRecipe.getResultItem(null)));
			}
		});
	}
}
