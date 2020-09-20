package test.util;

import main.util.Stopwatch;

public class StopwatchTester {
	private static final int MILLISECONDS_METHOD = 1;
	private static final int SECONDS_METHOD = 2;
	private static Stopwatch stopwatch = new Stopwatch();

	public static void main(String[] args) {
		stopwatch.clear();
		runStopwatch();
		runStopwatch();
		runStopwatch();
		stopwatch.clear();
		testGetTimeFormatSuite();
		testGetTimeValueSuite();
		testSetMillisecondsDecimalsSuite();
		testSetSecondsDecimalsSuite();
		System.out.println(stopwatch.getTimeInNanoseconds() + "\t" + stopwatch.getTimeInMilliseconds() + "\t" + stopwatch.getTimeInSeconds());
	}

	private static void runStopwatch() {
		stopwatch.start();
		long i = 0;
		while (i < 100000) {
			i++;
		}
		stopwatch.stop();
	}

	private static boolean testGetTimeFormatSuite() {
		return testGetTimeInNanosecondsFormat("getTimeInNanoseconds") && testGetTimeInMillisecondsFormat("")
				&& testGetTimeInSecondsFormat("getTimeInSeconds");
	}

	private static boolean testGetTimeInNanosecondsFormat(String methodName) {
		String ns = stopwatch.getTimeInNanoseconds();

		String nanosecondsRegex = "^[0-9]+$";
		if (!ns.matches(nanosecondsRegex)) {
			System.out.println(methodName + " failed because nanoseconds should only have numbers" + ns);
			return false;
		}

		return true;
	}

	private static boolean testGetTimeInMillisecondsFormat(String methodName) {
		String ms = stopwatch.getTimeInMilliseconds();
		String range = "{0," + Stopwatch.MAX_MILLISECONDS_DECIMALS + "}";
		String millisecondsRegex1 = "^[0-9]+[.][0-9]" + range + "$";
		String millisecondsRegex2 = "^[0-9]+$";

		if (!(ms.matches(millisecondsRegex1) || ms.matches(millisecondsRegex2))) {
			System.out.println(methodName
					+ " failed milliseconds should only have 1 decimal value with a number after it or milliseconds should only have all numbers"
					+ ms);
			return false;
		}
		return true;

	}

	private static boolean testGetTimeInSecondsFormat(String methodName) {
		String s = stopwatch.getTimeInSeconds();

		String range = "{0," + Stopwatch.MAX_SECONDS_DECIMALS + "}";
		String secondsRegex1 = "^[0-9]+[.][0-9]" + range + "$";
		String secondsRegex2 = "^[0-9]+$";

		if (!(s.matches(secondsRegex1) || s.matches(secondsRegex2))) {
			System.out.println(methodName
					+ " failed milliseconds should only have 1 decimal value with a number after it or milliseconds should only have all numbers"
					+ s);
			return false;
		}
		return true;

	}

	private static boolean testGetTimeValueSuite() {
		return testGetTimeInNanosecondsValue("getTimeInNanoseconds")
				&& testGetTimeInMillisecondsValue("getTimeInMilliseconds")
				&& testGetTimeInSecondsValue("getTimeInSeconds");
	}

	private static boolean testGetTimeInNanosecondsValue(String methodName) {
		long ns = getValueBeforeDecimal(stopwatch.getTimeInNanoseconds());
		long ms = getValueBeforeDecimal(stopwatch.getTimeInMilliseconds());
		long s = getValueBeforeDecimal(stopwatch.getTimeInSeconds());

		if (ns < ms) {
			System.out.println(methodName + " failed because nanoseconds is less than milliseconds: (ns vs ms)" + ns
					+ " vs " + ms);
			return false;
		}
		if (ns < s) {
			System.out.println(
					methodName + " failed because nanoseconds is less than seconds: (ns vs s)" + ns + " vs " + s);
			return false;
		}

		return true;
	}

	private static boolean testGetTimeInMillisecondsValue(String methodName) {
		long ns = getValueBeforeDecimal(stopwatch.getTimeInNanoseconds());
		long ms = getValueBeforeDecimal(stopwatch.getTimeInMilliseconds());
		long s = getValueBeforeDecimal(stopwatch.getTimeInSeconds());

		if (ms < s) {
			System.out.println(
					methodName + " failed because milliseconds is less than seconds: (ms vs s)" + ms + " vs " + s);
			return false;
		}
		if (ms > ns) {
			System.out.println(methodName + " failed because milliseconds is greater than nanoseconds: (ms vs ns)" + ms
					+ " vs " + ns);
			return false;
		}

		return true;
	}

	private static boolean testGetTimeInSecondsValue(String methodName) {
		long ns = getValueBeforeDecimal(stopwatch.getTimeInNanoseconds());
		long ms = getValueBeforeDecimal(stopwatch.getTimeInMilliseconds());
		long s = getValueBeforeDecimal(stopwatch.getTimeInSeconds());

		if (s > ns) {
			System.out.println(
					methodName + " failed because seconds is greater than nanoseconds: (s vs ns)" + s + " vs " + s);
			return false;
		}
		if (s > ms) {
			System.out
					.println(methodName + " failed because seconds is less than seconds: (s vs ms)" + s + " vs " + ms);
			return false;
		}

		return true;
	}

	private static long getValueBeforeDecimal(String input) {
		int decimalIndex = input.indexOf(".");
		return decimalIndex != -1 ? Long.parseLong(input.substring(0, decimalIndex)) : Long.parseLong(input);
	}

	private static void testSetMillisecondsDecimalsSuite() {
		testSetMillisecondsDecimals("setMilliseconds", -1);
		testSetMillisecondsDecimals("setMilliseconds", 0);
		testSetMillisecondsDecimals("setMilliseconds", Stopwatch.MAX_MILLISECONDS_DECIMALS / 2);
		testSetMillisecondsDecimals("setMilliseconds", Stopwatch.MAX_MILLISECONDS_DECIMALS);
		testSetMillisecondsDecimals("setMilliseconds", Stopwatch.MAX_MILLISECONDS_DECIMALS + 1);
	}

	private static void testSetSecondsDecimalsSuite() {
		testSetSecondsDecimals("setSeconds", -1);
		testSetSecondsDecimals("setSeconds", 0);
		testSetSecondsDecimals("setSeconds", Stopwatch.MAX_SECONDS_DECIMALS / 2);
		testSetSecondsDecimals("setSeconds", Stopwatch.MAX_SECONDS_DECIMALS);
		testSetSecondsDecimals("setSeconds", Stopwatch.MAX_SECONDS_DECIMALS + 1);
	}

	private static boolean testSetMillisecondsDecimals(String methodName, int numDecimals) {
		stopwatch.setMillisecondsDecimals(numDecimals);
		boolean result = testSetDecimals(methodName, stopwatch.getTimeInMilliseconds(), numDecimals,
				MILLISECONDS_METHOD);
		stopwatch.setMillisecondsDecimals(Stopwatch.MAX_MILLISECONDS_DECIMALS);
		return result;
	}

	private static boolean testSetSecondsDecimals(String methodName, int numDecimals) {
		stopwatch.setSecondsDecimals(numDecimals);
		boolean result = testSetDecimals(methodName, stopwatch.getTimeInMilliseconds(), numDecimals, SECONDS_METHOD);
		stopwatch.setSecondsDecimals(Stopwatch.MAX_SECONDS_DECIMALS);
		return result;
	}

	private static boolean testSetDecimals(String methodName, String format, int numDecimals, int setMethod) {
		String time = "";
		if (setMethod == MILLISECONDS_METHOD) {
			time = stopwatch.getTimeInMilliseconds();
		} else if (setMethod == SECONDS_METHOD) {
			time = stopwatch.getTimeInSeconds();
		}

		int decimalIndex = time.indexOf(".");
		if (decimalIndex == -1) {
			if (numDecimals > 0) {
				System.out.println(
						methodName + " failed because couldn't find decimal point when given a positive of decimals: "
								+ numDecimals);
			}
		} else {
			time = time.substring(decimalIndex + 1);

			if (numDecimals <= 0) {
				if (time.length() >= 0) {
					System.out.println(methodName + " failed because a decimal was found but expected no decimal");
					return false;
				}

			} else if (setMethod == MILLISECONDS_METHOD
					&& time.length() != Stopwatch.getValidMillisecondsDecimal(numDecimals)) {
				System.out.println(methodName + " failed because number after decimal aren't the same, expected: "
						+ Stopwatch.getValidMillisecondsDecimal(numDecimals) + " but got: " + time.length());
				return false;
			} else if (setMethod == SECONDS_METHOD && time.length() != Stopwatch.getValidSecondsDecimal(numDecimals)) {
				System.out.println(methodName + " failed because number after decimal aren't the same, expected: "
						+ Stopwatch.getValidSecondsDecimal(numDecimals) + " but got: " + time.length());
				return false;
			}
		}
		return true;
	}
}
