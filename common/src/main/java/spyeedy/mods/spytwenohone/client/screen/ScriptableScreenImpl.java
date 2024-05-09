package spyeedy.mods.spytwenohone.client.screen;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class ScriptableScreenImpl extends Screen implements ScriptableScreen {

	private final Consumer<ScriptableScreenImpl> initFunc;

	public ScriptableScreenImpl(Runnable constructFunc, Consumer<ScriptableScreenImpl> initFunc) {
		super(Component.empty());

		constructFunc.run();
		this.initFunc = initFunc;
	}

	@Override
	protected void init() {
		super.init();

		this.initFunc.accept(this);
	}

	@Override
	public void closeScreen() {

	}

	@Override
	public void addWidget() {

	}

	@Override
	public void removeWidget() {

	}

	@Override
	public int getScreenWidth() {
		return 0;
	}

	@Override
	public int getScreenHeight() {
		return 0;
	}

	public static class Builder {
		private final int width;

		private final int height;

		private ResourceLocation id;
		private Runnable constructFunc = null;
		private Consumer<ScriptableScreenImpl> initFunc;

		public Builder(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public Builder onConstruct(Runnable constructor) {
			constructFunc = constructor;
			return this;
		}

		public Builder onInit(Consumer<ScriptableScreenImpl> screenInit) {
			this.initFunc = screenInit;
			return this;
		}

		@HideFromJS
		public ScriptableScreenImpl build() {
			return new ScriptableScreenImpl(constructFunc, initFunc);
		}
	}
}
