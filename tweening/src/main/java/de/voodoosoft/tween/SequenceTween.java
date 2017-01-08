package de.voodoosoft.tween;

import java.util.ArrayList;
import java.util.List;



public class SequenceTween implements Tween {
	public SequenceTween() {
		tweens = new ArrayList<>();
		idxDelta = 1;
	}

	public void addTween(Tween tween) {
		tweens.add(tween);
	}

	public void setCommute(boolean commute) {
		this.commute = commute;
	}

	public boolean isCommute() {
		return commute;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public boolean isLoop() {
		return loop;
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
	public void reset() {
		activeTweenIdx = 0;
	}

	@Override
	public void update(long time) {
		if (!done && activeTweenIdx < tweens.size()) {
			Tween activeTween = tweens.get(activeTweenIdx);
			activeTween.update(time);
			if (activeTween.isDone()) {
				activeTweenIdx += idxDelta;
				if (activeTweenIdx == tweens.size() || activeTweenIdx < 0) {
					if (isLoop()) {
						activeTweenIdx = activeTweenIdx == tweens.size() ? 0 : tweens.size() - 1;
					}
					else if (isCommute()) {
						idxDelta *= -1;
						activeTweenIdx += idxDelta;
					}
					else {
						done = true;
					}
				}

				if (!done) {
					Tween nextTween = tweens.get(activeTweenIdx);
					nextTween.reset();
				}
			}

			updated = activeTween.isUpdated();
		}
	}

	private boolean loop;
	private boolean commute;
	private int idxDelta;
	private boolean done;
	private boolean updated;
	private int activeTweenIdx;
	private final List<Tween> tweens;
}
