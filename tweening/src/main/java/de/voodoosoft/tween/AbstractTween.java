package de.voodoosoft.tween;

public abstract class AbstractTween implements Tween {
	public void setCallback(TweenCallback callback) {
		this.tweenCallback = callback;
	}

	protected void onStart(long time, float value) {
		if (tweenCallback != null) {
			tweenCallback.onStart(time, value);
		}
	}

	protected void onUpdate(long time, float value) {
		if (tweenCallback != null) {
			tweenCallback.onUpdate(time, value);
		}
	}

	protected void onEnd(long time, float value) {
		if (tweenCallback != null) {
			tweenCallback.onEnd(time, value);
		}
	}

	private TweenCallback tweenCallback;
}
