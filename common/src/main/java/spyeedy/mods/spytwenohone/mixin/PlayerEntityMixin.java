package spyeedy.mods.spytwenohone.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import spyeedy.mods.spytwenohone.entity.PlayerEntityExtension;
import spyeedy.mods.spytwenohone.storage.entity.PlayerDataStore;

@Mixin(Player.class)
public class PlayerEntityMixin implements PlayerEntityExtension {
	@Unique
	private PlayerDataStore spytoo$dataStore;

	@Inject(method = "<init>", at = @At("RETURN"))
	public void init(Level level, BlockPos pos, float yRot, GameProfile profile, CallbackInfo ci) {
		this.spytoo$dataStore = new PlayerDataStore();
	}

	@Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
	public void readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
		if (compound.contains("SpyToo"))
			this.spytoo$dataStore.readFromNBT(compound.getCompound("SpyToo"));
	}

	@Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
	public void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
		CompoundTag spyTooTag = new CompoundTag();
		this.spytoo$dataStore.writeToNBT(spyTooTag);
		compound.put("SpyToo", spyTooTag);
	}

	@Override
	public PlayerDataStore spytoo$getDataStore() {
		return this.spytoo$dataStore;
	}
}
