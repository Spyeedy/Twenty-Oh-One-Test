package spyeedy.mods.spytwenohone.event;

import java.util.ArrayList;
import java.util.List;

public class Event<T> {
	private final List<T> listeners;
	private T invoker;

	public Event() {
		this.listeners = new ArrayList<>();
	}

	public void subscribe(T handler) {
		this.listeners.add(handler);
	}

	public T invoker() {
		return invoker;
	}
}
