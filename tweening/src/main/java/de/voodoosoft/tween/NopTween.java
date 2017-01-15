package de.voodoosoft.tween;

public class NopTween implements Tween {
	@Override
	public void reset() {
	}

	@Override
	public void update(long time) {
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public boolean isUpdated() {
		return false;
	}
}
