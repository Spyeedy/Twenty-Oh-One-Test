package spyeedy.mods.spytwenohone.registry.client.forge;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import spyeedy.mods.spytwenohone.SpyTwentyOhOne;
import spyeedy.mods.spytwenohone.registry.RegistrySupplier;

@Mod.EventBusSubscriber(modid = SpyTwentyOhOne.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BlockEntityRendererRegistryImpl {
	public static <T extends BlockEntity> void register(RegistrySupplier<BlockEntityType<T>> entityType, BlockEntityRendererProvider<T> provider) {
		BlockEntityRenderers.register(entityType.get(), provider);
	}

	/*@SubscribeEvent
	public static void entityRenderersEvent(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer();
	}*/
}
