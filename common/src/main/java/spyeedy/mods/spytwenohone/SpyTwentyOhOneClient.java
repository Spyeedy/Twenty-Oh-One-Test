package spyeedy.mods.spytwenohone;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import spyeedy.mods.spytwenohone.block.SpyTooBlocks;
import spyeedy.mods.spytwenohone.block.entity.SpyTooBlockEntities;
import spyeedy.mods.spytwenohone.client.ScriptableScreenManager;
import spyeedy.mods.spytwenohone.client.gui.inventory.SmeltineryInvScreen;
import spyeedy.mods.spytwenohone.client.renderer.blockentity.SmeltineryBlockEntityRenderer;
import spyeedy.mods.spytwenohone.container.SpyTooContainers;
import spyeedy.mods.spytwenohone.registry.ReloadListenerRegistry;
import spyeedy.mods.spytwenohone.registry.client.BlockEntityRendererRegistry;
import spyeedy.mods.spytwenohone.registry.client.RenderTypeRegistry;
import spyeedy.mods.spytwenohone.util.ModIdRefs;
import spyeedy.mods.spytwenohone.platform.Platform;

public class SpyTwentyOhOneClient {
	public static void init() {
		if (Platform.isModLoaded(ModIdRefs.KubeJS)) {
			 ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new ResourceLocation(SpyTwentyOhOne.MOD_ID, "scriptable_screens"), ScriptableScreenManager.INSTANCE);
		}
	}

	public static void registerMenuScreens() {
		MenuScreens.register(SpyTooContainers.SMELTINERY.get(), SmeltineryInvScreen::new);
	}

	public static void registerRenderTypes() {
		RenderTypeRegistry.registerBlock(SpyTooBlocks.SMELT_FINERY, RenderType.cutout());
	}

	public static void registerEntityRenderers() {
		BlockEntityRendererRegistry.register(SpyTooBlockEntities.SMELTINERY, SmeltineryBlockEntityRenderer::new);
	}

	public static void keyPress(Minecraft minecraft, int key, int scanCode, int action, int modifiers) {
		if (minecraft == null || minecraft.screen != null) return;

		if (key == InputConstants.KEY_R && action == InputConstants.PRESS) {
			if (Platform.isModLoaded(ModIdRefs.KubeJS)) {
				Screen screen = ScriptableScreenManager.INSTANCE.getScreen("test_screen_1");
				if (screen != null) {
					minecraft.setScreen(screen);
				}
			}
		}
	}
}
