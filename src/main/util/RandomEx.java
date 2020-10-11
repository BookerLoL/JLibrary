package main.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A subclass for {@code java.util.Random} for additional functionalities.
 * Helper methods to make common functionalities with generating random numbers.
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class RandomEx extends Random {
	@java.io.Serial
	private static final long serialVersionUID = 474436037464260737L;

	/**
	 * Creates a new random number generator calling on the {@code java.util.Random}
	 * constructor.
	 */
	public RandomEx() {
		super();
	}

	/**
	 * Use the same seed will generate the same sequence of pseudorandom numbers
	 * which helps with testing.
	 * 
	 * @param seed the initial seed
	 */
	public RandomEx(long seed) {
		super(seed);
	}

	/**
	 * A convenience method to generate a uniformed pseudorandom {@code int} within
	 * a bounded inclusive range. The arguments of {@code min} and {@code max} will
	 * be arranged so that {@code max >= min}, even if the caller has bad argument
	 * values.
	 * 
	 * @param min the inclusive min value that can be generated
	 * 
	 * @param max the inclusive max value that can be generated
	 * @return the next pseudorandom, uniformly distributed {@code int} value within
	 *         the given range from this rand number generator's sequence
	 * @since 1.0
	 */
	public int nextInt(int min, int max) {
		return min < max ? (int) (Math.random() * (max + 1 - min)) + min
				: (int) (Math.random() * (min + 1 - max)) + max;
	}

	/**
	 * A convenience method to generate a uniformed pseudorandom {@code double}
	 * within a bounded inclusive range. The arguments of {@code min} and
	 * {@code max} will be arranged so that {@code max >= min}, even if the caller
	 * has bad argument values.
	 * 
	 * @param min the inclusive min value that can be generated
	 * 
	 * @param max the inclusive max value that can be generated
	 * @return the next pseudorandom, uniformly distributed {@code double} value
	 *         within the given range from this rand number generator's sequence
	 * @since 1.0
	 */
	public double nextDouble(double min, double max) {
		return min < max ? nextDouble() * (max - min) + min : nextDouble() * (min - max) + max;
	}

	/**
	 * A convenience method to generate a uniformed pseudorandom {@code float}
	 * within a bounded inclusive range. The arguments of {@code min} and
	 * {@code max} will be arranged so that {@code max >= min}, even if the caller
	 * has bad argument values.
	 * 
	 * @param min the inclusive min value that can be generated
	 * 
	 * @param max the inclusive max value that can be generated
	 * @return the next pseudorandom, uniformly distributed {@code float} value
	 *         within the given range from this rand number generator's sequence
	 * @since 1.0
	 */
	public float nextFloat(float min, float max) {
		return min < max ? nextFloat() * (max - min) + min : nextFloat() * (min - max) + max;
	}

	/**
	 * A method to generate a number based on the given {@code numbers} by using the
	 * provided {@code chances} to generate the a random value.
	 * <p>
	 * This method is equivalent to making if statements to determine which number
	 * to select randomly.
	 * <p>
	 * Make that the {@code numbers} and {#code chances} are the same length.
	 * {@code chances} should add up to exactly 1.0 otherwise results may not be
	 * correct.
	 * 
	 * @param <T>     The type of numbers
	 * @param numbers The selected values to be picked randomly
	 * @param chances The associated chance for the number to be selected
	 * @return The random number selected
	 */
	public static <T> T rouletteWheel(T[] numbers, double[] chances) {
		Random random = new Random();
		double randVal = random.nextDouble();
		for (int i = 0; i < chances.length; i++) {
			if (randVal <= chances[i]) {
				return numbers[i];
			}
			randVal -= chances[i];
		}
		return numbers[numbers.length - 1];
	}

	/**
	 * A method to create random numbers using linear congruential generator
	 * algorithm. The random numbers are not high quality.
	 * 
	 * GCC usually uses 2e31, 1103515245, 12345 as parameter values
	 * 
	 * <p>
	 * To generate the next random number, use the previously generated value
	 * 
	 * 
	 * @param range
	 * @param multiplier
	 * @param increment
	 * @param startingValue
	 * @return an non-high quality random number
	 */
	public static int lcgPRNG(int range, int multiplier, int increment, int startingValue) {
		return (multiplier * startingValue + increment) % range;
	}

	/**
	 * Amethod to create random numbers using multiply with carry algorithm.
	 * {@link https://www.javamex.com/tutorials/random_numbers/multiply_with_carry.shtml#footnote2}
	 * source for implementation idea.
	 * 
	 * @return a random number
	 */
	public static int mwcPRNG() {
		final long a = 0xFFFFDA61;
		long x = System.nanoTime() & 0xFFFFFFFFL;
		return (int) ((a * (x & 0xFFFFFFFFL)) + (x >>> 32));
	}

	public static CMWCRandom cmwcPRNG() {
		return new CMWCRandom();
	}

	public static CMWCRandom cmwcPRNG(int numDefaultValues) {
		return new CMWCRandom(numDefaultValues);
	}

	public static CMWCRandom cmwcPRNG(int numDefaultValues, int lagNumber) {
		return new CMWCRandom(numDefaultValues, lagNumber);
	}

	public static CMWCRandom cmwcPRNG(int numDefaultValues, int lagNumber, int range, int multiplier) {
		return new CMWCRandom(numDefaultValues, lagNumber, range, multiplier);
	}

	/**
	 * A class to mimic the multiply with carry number generate algorithm idea.
	 * 
	 * @author Ethan
	 */
	public static class CMWCRandom {
		private int range;
		private int multiplier;
		private int lagNumber;
		private List<Integer> sequenceNumbers;
		private int prevCarryNumber;

		public CMWCRandom() {
			this(1);
		}

		public CMWCRandom(int numDefaultValues) {
			this(numDefaultValues, 1);
		}

		public CMWCRandom(int numDefaultValues, int lagNumber) {
			this(numDefaultValues, lagNumber, Integer.MAX_VALUE, 1103515245);
		}

		public CMWCRandom(int numDefaultValues, int lagNumber, int range, int multiplier) {
			sequenceNumbers = new ArrayList<>();
			numDefaultValues = numDefaultValues > 0 ? numDefaultValues : 1;
			lagNumber = lagNumber < numDefaultValues ? lagNumber : 1;
			this.range = range;
			this.multiplier = multiplier;

			Random rand = new Random();
			while (numDefaultValues > 0) {
				sequenceNumbers.add(rand.nextInt());
				numDefaultValues--;
			}
		}

		public int nextInt() {
			prevCarryNumber = calculateNextCarry();

			int nextNumber = (multiplier * sequenceNumbers.get(sequenceNumbers.size() - lagNumber) + prevCarryNumber)
					% range;
			sequenceNumbers.remove(0);
			sequenceNumbers.add(nextNumber);
			return nextNumber;
		}

		// Cn = floor( (AXn-r + Cn-1) / B ), n >= r
		// A is the multiplier
		// X is the previous value of sequence n-r
		// C carry number
		// B is range
		private int calculateNextCarry() {
			return (int) Math
					.floor(((multiplier * sequenceNumbers.get(sequenceNumbers.size() - lagNumber)) / (double) range));
		}
	}

	// Need to find paper, Java implementation is considered difficult
	public static int merseenTwisterPRNG() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates an object that will help generate a normal distribution.
	 * 
	 * @return A BoxMullerTransformation object
	 */
	public static BoxMullerTransformation normalDistribution() {
		return new BoxMullerTransformation();
	}

	public static class BoxMullerTransformation {
		double y1, y2;
		boolean useLast;

		public double getDistribution() {
			if (useLast) {
				y1 = y2;
				useLast = false;
			} else {
				double w, x1, x2;
				do {
					x1 = 2.0 * Math.random() - 1.0;
					x2 = 2.0 * Math.random() - 1.0;
					w = x1 * x1 + x2 * x2;
				} while (w >= 1.0);
				w = Math.sqrt((-2.0 * Math.log(w)) / w);

				y1 = x1 * w;
				y2 = x2 * w;
				useLast = true;
			}
			return y1;
		}
	}
}
