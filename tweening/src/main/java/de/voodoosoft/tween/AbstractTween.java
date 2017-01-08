package de.voodoosoft.tween;

public abstract class AbstractTween implements Tween {
	public void setCallback(TweenCallback callback) {
		this.tweenCallback = callback;
	}

	protected void onStart(long time, float value) {
		tweenCallback.onStart(time, value);
	}

	protected void onUpdate(long time, float value) {
		tweenCallback.onUpdate(time, value);
	}

	protected void onEnd(long time, float value) {
		tweenCallback.onEnd(time, value);
	}

	private TweenCallback tweenCallback;
}
