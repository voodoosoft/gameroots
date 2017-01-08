package de.voodoosoft.tween;

public interface Tween {
	void reset();

	void update(long time);

	boolean isDone();

	boolean isUpdated();
}
