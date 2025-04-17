package spyeedy.mods.spytwenohone.storage.fluid.forge;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class FluidHandlerImpl implements IFluidHandler {

	@Override
	public int getTanks() {
		return 0;
	}

	@Override
	public @NotNull FluidStack getFluidInTank(int i) {
		return null;
	}

	@Override
	public int getTankCapacity(int i) {
		return 0;
	}

	@Override
	public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
		return false;
	}

	@Override
	public int fill(FluidStack fluidStack, FluidAction fluidAction) {
		return 0;
	}

	@Override
	public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
		return null;
	}

	@Override
	public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
		return null;
	}
}
