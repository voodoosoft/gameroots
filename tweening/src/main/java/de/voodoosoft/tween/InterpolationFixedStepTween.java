package de.voodoosoft.tween;

import com.badlogic.gdx.math.Interpolation;



public class InterpolationFixedStepTween extends AbstractTween {
	public InterpolationFixedStepTween(Interpolation interpolation, float startValue, float endValue, float valueDelta, long updateInterval) {
		if (valueDelta < 0 && endValue > startValue) {
			throw new IllegalArgumentException("end value must be < start value");
		}
		if (valueDelta > 0 && endValue < startValue) {
			throw new IllegalArgumentException("end value must be > start value");
		}

		this.interpolation = interpolation;
		this.updateInterval = updateInterval;
		this.value = Float.NaN;
		this.inputVal = startValue;
		this.startValue = startValue;
		this.endValue = endValue;
		this.valueDelta = valueDelta;
		active = true;
	}

	public float getValue() {
		return value;
	}

	public void setValueDelta(float valueDelta) {
		this.valueDelta = valueDelta;
	}

	public float getValueDelta() {
		return valueDelta;
	}

	public boolean isTicked() {
		return ticked;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public boolean isUpdated() {
		return updated;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void reset() {
		lastUpdateTime = 0;
		inputVal  = startValue;
		value = Float.NaN;
		updated = false;
		ticked = false;
		done = false;
	}

	@Override
	public void update(long time) {
		updated = false;
		ticked = false;
		if (!isActive() || done) {
			return;
		}
		if (lastUpdateTime == 0) {
			lastUpdateTime = time;
			onStart(time, value);
			return;
		}

		long dt = time - lastUpdateTime;
		if (dt > updateInterval) {
			ticked = true;

			inputVal += valueDelta;
			value = interpolation.apply(inputVal);

			if ((valueDelta > 0 && value >= endValue) || (valueDelta < 0 && value <= endValue)) {
				value = endValue;
				done = true;
			}

			onUpdate(time, value);
			updated = true;
			lastUpdateTime = time;

			if (done) {
				onEnd(time, value);
			}
		}
		else {
			ticked = false;
		}
	}

	private Interpolation interpolation;
	private float inputVal;
	private float value;
	private float startValue;
	private float endValue;
	private float valueDelta;
	private long updateInterval;
	private long lastUpdateTime;
	private boolean active;
	private boolean updated;
	private boolean ticked;
	private boolean done;
}