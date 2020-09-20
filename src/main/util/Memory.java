package main.util;

/**
 * A utility class used to deal with memory such as using a good memory growth
 * formula and dealing with memory consumption of a program.
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class Memory {
	/**
	 * A basic memory growth formula that grows by 2x.
	 * 
	 * @param length the current length
	 * @return the new length
	 * @since 1.0
	 */
	public static int doubleSize(int length) {
		return length == 0 ? 16 : length << 1;
	}

	/**
	 * A slower growth formula that grows by 1.5x. The following is similar to
	 * {@code Memory.javaGrowth(int length)}
	 * <p>
	 * The following uses the code:
	 * 
	 * <pre>
	 * {@code
	 * return (length * 3) / 2 + 1;
	 * }
	 * </pre>
	 * 
	 * @param length the current length
	 * @return the new length
	 * @since 1.0
	 */
	public static int slowGrowth(int length) {
		return (length * 3) / 2 + 1;
	}

	/**
	 * This growth is java implementation of their memory growth which grows by
	 * 1.5x. The following is similar to {@code Memory.slowGrowth(int length)}
	 * <p>
	 * The following uses the code:
	 * 
	 * <pre>
	 * {@code
	 * return length + (length >> 1);
	 * }
	 * </pre>
	 * 
	 * @param length the current length
	 * @return the new length
	 * @since 1.0
	 */
	public static int javaGrowth(int length) {
		return length + (length >> 1);
	}

	/**
	 * Returns an estimated {@code long} amount of memory used.
	 * <p>
	 * Need to allocate a lot of objects in order to see changes in the memory. Uses
	 * the formula:
	 * 
	 * <pre>
	 * {@code
	 * return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	 * }
	 * </pre>
	 * 
	 * @return estimated amount of memory used in bytes
	 * @since 1.0
	 */
	public static long memoryUsed() {
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	/**
	 * A wrapper method for using {@code Runetime.getRunetime().maxMemory()}.
	 * 
	 * @return estimated amount of memory used
	 * @since 1.0
	 */
	public static long maxMemory() {
		return Runtime.getRuntime().maxMemory();
	}
}
