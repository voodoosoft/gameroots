package de.voodoosoft.tween;

public interface TweenCallback {
	void onStart(long time, float value);

	void onUpdate(long time, float value);

	void onEnd(long time, float value);
}