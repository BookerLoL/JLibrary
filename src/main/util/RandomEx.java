package main.util;

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
	 * <p>
	 * The invocation {@code new RandomEx(seed)} is equivalent to:
	 * 
	 * <pre>
	 * {@code
	 * 	RandomEx rand = new RandomEx();
	 * 	rand.setSeed(seed);
	 * }
	 * </pre>
	 * 
	 * @param seed the initial seed
	 */
	public RandomEx(long seed) {
		super(seed);
	}

	/**
	 * A convenience method to generate a pseudorandom {@code int} within a bounded
	 * inclusive range. The arguments of {@code min} and {@code max} will be
	 * arranged so that {@code max >= min}, even if the caller has bad argument
	 * values.
	 * 
	 * <p>
	 * The method {@code nextInt(int min, int max)} is implemented by class
	 * {@code RandomEx} as if by:
	 * 
	 * <pre>
	 * {@code 
	 * public int nextInt(int min, int max) {
	 * 		return min < max ? (int) (Math.random() * (max + 1 - min)) + min : (int) (Math.random() * (min + 1 - max)) + max;
	 * }
	 * }
	 * </pre>
	 * 
	 * @param min the inclusive min value that can be generated
	 * 
	 * @param max the inclusive max value that can be generated
	 * @return the next pseudorandom, uniformly distributed {@code int} value within
	 *         the given range from this rand number generator's sequence
	 * @since 1.0
	 */
	public int nextInt(int min, int max) {
		return min < max ? (int) (Math.random() * (max + 1 - min)) + min : (int) (Math.random() * (min + 1 - max)) + max;
	}

	/**
	 * A convenience method to generate a pseudorandom {@code double} within a
	 * bounded inclusive range. The arguments of {@code min} and {@code max} will be
	 * arranged so that {@code max >= min}, even if the caller has bad argument
	 * values.
	 * 
	 * <p>
	 * The method {@code nextDouble(double min, double max)} is implemented by class
	 * {@code RandomEx} as if by:
	 * 
	 * <pre>
	 * {@code 
	 * public double nextDouble(double min, double max) {
	 * 		return min < max ? nextDouble() * (max - min) + min : nextDouble() * (min - max) + max;
	 * }
	 * }
	 * </pre>
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
	 * A convenience method to generate a pseudorandom {@code float} within a
	 * bounded inclusive range. The arguments of {@code min} and {@code max} will be
	 * arranged so that {@code max >= min}, even if the caller has bad argument
	 * values.
	 * 
	 * <p>
	 * The method {@code nextFloat(float min, float max)} is implemented by class
	 * {@code RandomEx} as if by:
	 * 
	 * <pre>
	 * {@code 
	 * public float nextFloat(float min, float max) {
	 * 		return min < max ? nextFloat() * (max - min) + min : nextFloat() * (min - max) + max;
	 * }
	 * }
	 * </pre>
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
}
