package de.voodoosoft.tween;

import org.junit.Test;



public class TweenTest {
	public TweenTest() {
		timer = new SystemTimer();
	}

	@Test
	public void testDayNight() {
		SequenceTween dayNightTweens = new SequenceTween();
		dayNightTweens.setLoop(true);

		long start = timer.getTimeValue();

		LinearFixedStepTween dawnTween = new LinearFixedStepTween(0, 1, 0.1f, Timing.millisToNano(10));
		dawnTween.setCallback(new TweenCallbackAdapter() {
			@Override
			public void onUpdate(long time, float value) {
				daylight = value;
				System.out.println(Timing.nanoToSecs(time - start) + " dawn: " + value);
			}
		});
		dayNightTweens.addTween(dawnTween);

		DelayTween dayTween = new DelayTween(Timing.secsToNano(3));
		dayTween.setCallback(new TweenCallbackAdapter() {
			@Override
			public void onStart(long time, float value) {
				System.out.println(Timing.nanoToSecs(time - start) + " day");
			}
		});
		dayNightTweens.addTween(dayTween);

		LinearFixedStepTween duskTween = new LinearFixedStepTween(1, 0, -0.1f, Timing.millisToNano(10));
		duskTween.setCallback(new TweenCallbackAdapter() {
			@Override
			public void onUpdate(long time, float value) {
				daylight = value;
				System.out.println(Timing.nanoToSecs(time - start) + " dusk: " + value);
			}
		});
		dayNightTweens.addTween(duskTween);

		DelayTween nightTween = new DelayTween(Timing.secsToNano(3));
		nightTween.setCallback(new TweenCallbackAdapter() {
			@Override
			public void onStart(long time, float value) {
				System.out.println(Timing.nanoToSecs(time - start) + " night");
			}
		});
		dayNightTweens.addTween(nightTween);

		while (!dayNightTweens.isDone()) {
			dayNightTweens.update(timer.getTimeValue());
		}
	}

	private float daylight;
	private SystemTimer timer;
}
