package de.voodoosoft.tween;

import org.junit.Test;



public class TweenTest {
	public TweenTest() {
		timer = new SystemTimer();
	}

	@Test
	public void testDayNight() {
		long start = timer.getTimeValue();

		SequenceTween dayNightTweens = new SequenceTween();
		dayNightTweens.setLoop(true);

		// dawn tween: increase value from 0.0 to 1.0 in timed steps of 0.1
		LinearFixedStepTween dawnTween = new LinearFixedStepTween(0, 1, 0.1f, Timing.millisToNano(10));
		dawnTween.setCallback(new TweenCallbackAdapter() {
			@Override
			public void onUpdate(long time, float value) {
				System.out.println(Timing.nanoToSecs(time - start) + " dawn: " + value);
			}
		});
		dayNightTweens.addTween(dawnTween);

		// day tween: simply wait for for 3 seconds
		DelayTween dayTween = new DelayTween(Timing.secsToNano(3));
		dayTween.setCallback(new TweenCallbackAdapter() {
			@Override
			public void onStart(long time, float value) {
				System.out.println(Timing.nanoToSecs(time - start) + " day");
			}
		});
		dayNightTweens.addTween(dayTween);

		// dusk tween: decrease value from 1.0 to 0.0 in timed steps of 0.1
		LinearFixedStepTween duskTween = new LinearFixedStepTween(1, 0, -0.1f, Timing.millisToNano(10));
		duskTween.setCallback(new TweenCallbackAdapter() {
			@Override
			public void onUpdate(long time, float value) {
				System.out.println(Timing.nanoToSecs(time - start) + " dusk: " + value);
			}
		});
		dayNightTweens.addTween(duskTween);

		// night tween: simply wait for 3 seconds
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

	private SystemTimer timer;
}
