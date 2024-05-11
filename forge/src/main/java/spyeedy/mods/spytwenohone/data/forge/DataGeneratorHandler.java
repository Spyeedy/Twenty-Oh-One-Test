package spyeedy.mods.spytwenohone.data.forge;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;

@Mod.EventBusSubscriber(modid = SpyTwentyOhOne.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneratorHandler {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent e) {
		var dataGen = e.getGenerator();
		dataGen.addProvider(e.includeClient(), new SpyTooBlockStateProvider(dataGen.getPackOutput(), e.getExistingFileHelper()));
		dataGen.addProvider(e.includeClient(), new SpyTooItemModelProvider(dataGen.getPackOutput(), e.getExistingFileHelper()));
		dataGen.addProvider(e.includeClient(), new SpyTooLangProvider(dataGen.getPackOutput()));

		dataGen.addProvider(e.includeServer(), new SpyTooRecipeProvider(dataGen.getPackOutput()));
	}
}
