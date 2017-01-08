package de.voodoosoft.tween;

public class DelayTween extends AbstractTween {
	public DelayTween(long delay) {
		this.delay = delay;
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
			onStart(time, Float.NaN);
			return;
		}

		onUpdate(time, Float.NaN);

		long dt = time - firstUpdate;
		if (dt >= delay) {
			done = true;
			onEnd(time, Float.NaN);
		}
	}

	private boolean done;
	private boolean updated;
	private long delay;
	private long firstUpdate;
}
