package de.voodoosoft.tween;

public class DelayTween extends AbstractTween {
	public DelayTween(long delay) {
		this.delay = delay;
	}

	public DelayTween(long delay, float value) {
		this.delay = delay;
		this.value = value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public void reset() {
		firstUpdate = 0;
		done = false;
		updated = false;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public boolean isUpdated() {
		return updated;
	}

	@Override
	public void update(long time) {
		updated = false;
		if (done) {
			return;
		}

		if (firstUpdate == 0) {
			firstUpdate = time;
			updated = true;
			onStart(time, value);
			return;
		}

		onUpdate(time, value);

		long dt = time - firstUpdate;
		if (dt >= delay) {
			done = true;
			onEnd(time, value);
		}
	}

	private float value;
	private boolean done;
	private boolean updated;
	private long delay;
	private long firstUpdate;
}
