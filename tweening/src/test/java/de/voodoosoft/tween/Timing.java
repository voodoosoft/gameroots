package de.voodoosoft.tween;

public class Timing {

	public final static long MILLIS_AS_NANO = 1000000L;
	public final static long SECS_AS_NANO = MILLIS_AS_NANO * 1000L;

	public static long millisToNano(long ms) {
		return ms * MILLIS_AS_NANO;
	}

	public static long secsToNano(long sec) {
		return sec * SECS_AS_NANO;
	}

	public static long secsToMillis(long sec) {
		return sec * 1000;
	}

	public static float nanoToMillis(long ns) {
		float ms = (float)ns / (float)MILLIS_AS_NANO;
		return ms;
	}

	public static float nanoToSecs(long ns) {
		float ms = (float)ns / (float)SECS_AS_NANO;
		return ms;
	}

	public static long asNanos(String time, String timeUnit) {
		long val = Long.valueOf(time);
		if (timeUnit.equalsIgnoreCase("s")) {
			val = secsToNano(val);
		}
		else if (timeUnit.equalsIgnoreCase("ms")) {
			val = millisToNano(val);
		}
		else if (!timeUnit.equalsIgnoreCase("ns")) {
			throw new IllegalArgumentException("invalid time unit [" + timeUnit + "]");
		}

		return val;
	}

	public static long asNanos(String time) {
		String[] items = time.split("\\s");
		if (items.length != 2) {
			throw new IllegalArgumentException("invalid time value [" + time + "]");
		}

		long val = asNanos(items[0], items[1]);

		return val;
	}
}
