package spyeedy.mods.spytwenohone.data.forge;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.block.SpyTooBlocks;

public class SpyTooLangProvider extends LanguageProvider {

	public SpyTooLangProvider(PackOutput output) {
		super(output, SpyTwentyOhOne.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		this.addBlock(SpyTooBlocks.SMELT_FINERY, "Smeltinery");
	}
}
