package main.util;

/**
 * A class to help with keep track of elapsed time like a stopwatch. The class
 * allows a little bit of formatting for displaying time.
 * <p>
 * Useful for benchmarking code
 * 
 * <p>
 * As of currently, if the accumulated time exceeds {@code Long.MAX_VALUE} then
 * an overflow will occur.
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class Stopwatch {

	/**
	 * The maximum amount of decimal digits that can appear in a milliseconds
	 * {@code String}.
	 */
	public static final int MAX_MILLISECONDS_DECIMALS = 6;
	/**
	 * The maximum amount of decimal digits that can appear in a seconds
	 * {@code String}.
	 */
	public static final int MAX_SECONDS_DECIMALS = 9;

	/**
	 * Default decimal digits for milliseconds.
	 */
	private int millisecondsDecimals = 4;

	/**
	 * Default decimal digits for seconds.
	 */
	private int secondsDecimals = 2;

	/**
	 * An indicator to tell whether the stopwatch is in the state of running (has
	 * been running)
	 */
	private boolean running;

	/**
	 * The initial time when the stopwatch started running.
	 */
	private long startTime;

	/**
	 * The accumlated time that has elapsed from starting and stopping without
	 * clearing.
	 */
	private long elapsedTime;

	/**
	 * Constructs a stopwatch with milliseconds decimals to show 4 digits and
	 * seconds decimals to show 2 digits by default;
	 */
	public Stopwatch() {
	}

	/**
	 * Constructs a stopwatch with millisecond decimals to show
	 * {@code msNumDecimals} digits and seconds decimals to show
	 * {@code sNumdecimals}. The arguments are bounded from 0 to their max decimal
	 * digit value.
	 * 
	 * <p>
	 * The invocation {@code new Stopwatch(msNumDecimals, sNumDecimals)} is
	 * equivalent to:
	 * 
	 * <pre>
	 * {
	 * 	&#64;code
	 * 	Stopwatch stopwatch = new Stopwatch();
	 * 	stopwatch.setMilisecondsDecimals(msNumDecimals);
	 * 	stopwatch.setSecondsDecimals(sNumDecimals);
	 * }
	 * </pre>
	 * 
	 * @param msNumDecimals the number of decimal digits to appear in {@code String}
	 *                      format
	 * 
	 * @param sNumDecimals  the number of decimal digits to appear in {@code String}
	 *                      format
	 */
	public Stopwatch(int msNumDecimals, int sNumDecimals) {
		millisecondsDecimals = Stopwatch.getValidMillisecondsDecimal(msNumDecimals);
		secondsDecimals = Stopwatch.getValidSecondsDecimal(sNumDecimals);
	}

	/**
	 * Starts the stopwatch to keep track of time if it's not running.
	 */
	public void start() {
		if (!running) {
			running = true;
			startTime = System.nanoTime();
		}
	}

	/**
	 * Stops the stopwatch from keeping track of time if it's currently running.
	 * When stopped, an accumulated elapsed amount of time will be kept based on the
	 * formula {@code elapsedTime += endTime - startTime}. A running stopwatch will
	 * need to be stopped in order to get the updated elapsed time.
	 * <p>
	 * <i>If time exceeds the Long.MAX_VALUE, an overflow error will occur.</i>
	 * @since 1.0
	 */
	public void stop() {
		if (running) {

			long endTime = System.nanoTime();
			elapsedTime += endTime - startTime;
			startTime = endTime;
			running = false;
		}
	}

	/**
	 * The stopwatch will stop running and the accumulated elapsed time will be
	 * reset to 0.
	 * @since 1.0
	 */
	public void clear() {
		running = false;
		elapsedTime = 0;
	}

	/**
	 * Returns the elapsed nanoseconds that's been accumulated over time after
	 * starting and stopping.
	 * 
	 * @return the elapsed nanoseconds that's been accumulated over time.
	 * @since 1.0
	 */
	public String getTimeInNanoseconds() {
		return String.valueOf(elapsedTime);
	}

	/**
	 * Returns the elapsed formatted milliseconds that's been accumulated over time
	 * after starting and stopping. The formatted milliseconds will return a
	 * formatted string based on how many decimal digits are expected.
	 * 
	 * @return the elapsed formatted milliseconds that's been accumulated over time.
	 * @since 1.0
	 */
	public String getTimeInMilliseconds() {
		return String.format("%." + millisecondsDecimals + "f", convertNanosecondsToMilliseconds(elapsedTime));
	}

	/**
	 * Returns the elapsed formatted seconds that's been accumulated over time after
	 * starting and stopping. The formatted seconds will return a formatted string
	 * based on how many decimal digits are expected.
	 * 
	 * @return the elapsed formatted seconds that's been accumulated over time.
	 * @since 1.0
	 */
	public String getTimeInSeconds() {
		return String.format("%." + secondsDecimals + "f", convertNanosecondsToSeconds(elapsedTime));
	}

	/**
	 * Converts nanoseconds to milliseconds with the forumula:
	 * 
	 * <pre>
	 * nanoseconds / 1000000.0
	 * </pre>
	 * 
	 * @param time the time in nanoseconds
	 * @return converted nanoseconds to milliseconds value
	 * @since 1.0
	 */
	private static double convertNanosecondsToMilliseconds(long time) {
		return time / 1_000_000.0;
	}

	/**
	 * Converts nanoseconds to seconds with the forumula:
	 * 
	 * <pre>
	 * nanoseconds / 1000000000.0
	 * </pre>
	 * 
	 * @param time the time in nanoseconds
	 * @return converted nanoseconds to seconds value
	 * @since 1.0
	 */
	private static double convertNanosecondsToSeconds(long time) {
		return time / 1_000_000_000.0;
	}

	/**
	 * Will set the number of decimal digits the formatted milliseconds will
	 * contain. Even if {@code numDecimals} has bad values, a valid digit will be
	 * assigned.
	 * 
	 * <p>
	 * The method {@code setMillisecondsDecimals(int numDecimals)} will interally call
	 * {@code getValidMillisecondsDecimal(int numDecimals)} to set the formatted milliseconds decimals.
	 * 
	 * @param numDecimals the number of decimal digits that should appear
	 * @since 1.0
	 */
	public void setMillisecondsDecimals(int numDecimals) {
		millisecondsDecimals = getValidMillisecondsDecimal(numDecimals);
	}

	/**
	 * Will set the number of decimal digits the formatted seconds will contain.
	 * Even if {@code numDecimals} has bad values, a valid digit will be assigned.
	 * 
	 * <p>
	 * The method {@code setSecondsDecimals(int numDecimals)} will interally call
	 * {@code getValidSecondsDecimal(int numDecimals)} to set the formatted seconds decimals.
	 * 
	 * @param numDecimals the number of decimal digits that should appear
	 * @since 1.0
	 */
	public void setSecondsDecimals(int numDecimals) {
		secondsDecimals = getValidSecondsDecimal(numDecimals);
	}

	/**
	 * Returns a valid number between 0 and {@code MAX_MILLISECONDS_DECIMALS}
	 * 
	 * @param numDecimals numDecimals the number of decimal digits that should
	 *                    appear
	 * @return a number between 0 and {@code MAX_MILLISECONDS_DECIMALS}
	 * @since 1.0
	 */
	public static int getValidMillisecondsDecimal(int numDecimals) {
		return getValidRangeNumber(numDecimals, MAX_MILLISECONDS_DECIMALS);
	}

	/**
	 * Returns a valid number between 0 and {@code MAX_SECONDS_DECIMALS}
	 * 
	 * @param numDecimals the inputted number of decimal values
	 * @return a number between 0 and {@code MAX_SECONDS_DECIMALS}
	 * @since 1.0
	 */
	public static int getValidSecondsDecimal(int numDecimals) {
		return getValidRangeNumber(numDecimals, MAX_SECONDS_DECIMALS);
	}

	/**
	 * Determines a valid number within the range of 0 and max given an input
	 * {@code number}. A number less than 0 will be set to 0 and a number greater
	 * than {@code max} will be set to {@code max}. Otherewise the number will be
	 * set to the given {@code number} input.
	 * 
	 * @param number the expected input value
	 * @param max  the max value that {@code number} can be
	 * @return a valid number within the range of 0 and {@code max} given the input
	 *         {@code number}
	 * @since 1.0
	 */
	private static int getValidRangeNumber(int number, int max) {
		if (number < 0) {
			number = 0;
		} else if (number > max) {
			number = max;
		}
		return number;
	}
}
