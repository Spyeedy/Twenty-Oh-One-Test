package spyeedy.mods.spytwenohone.storage.entity;

import net.minecraft.nbt.CompoundTag;

public class PlayerDataStore {

	private int openCount = 0;

	public void readFromNBT(CompoundTag compound) {
		this.openCount = compound.getInt("openCount");
	}

	public void writeToNBT(CompoundTag compound) {
		compound.putInt("openCount", openCount);
	}

	public int getOpenCount() {
		return openCount;
	}

	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}
}
