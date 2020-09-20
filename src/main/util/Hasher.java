package main.util;
/**
 * A utility class for hashing specific inputs.
 * 
 * @apiNote Still incomplete
 * 
 * @author Ethan
 * @version 1.0
 */
public class Hasher {
	/**
	 * A basic hasher that uses the length of the given {@code String} input as the
	 * hash.
	 * 
	 * @param input non-null {@code String}
	 * @return non-negative hash number
	 * @since 1.0
	 */
	public static long lengthHashCode(String input) {
		return input.length();
	}

	/**
	 * A basic hasher that uses the number of digits from the {@code long} input as
	 * the hash. A zero long number will return 0 as the length.
	 * 
	 * @param number a number to be hashed
	 * @return non-negative hash number
	 * @since 1.0
	 */
	public static long lengthHashCode(long number) {
		long numberOfDigits = 0;
		while (number != 0) {
			numberOfDigits++;
			number /= 10;
		}
		return numberOfDigits;
	}

	/**
	 * 
	 * @param input non-null {@code String}
	 * @return non-negative hash number
	 * @since 1.0
	 */
	public static long additiveCharacterHash(String input) {
		long hashVal = 0;
		for (int i = 0; i < input.length(); i++) {
			hashVal += input.charAt(i);
		}
		return hashVal;
	}

	/**
	 * 
	 * @param input non-null {@code String}
	 * @return hash number
	 */
	public static long improveAdditiveCharacterHash(String input) {
		long hashVal = 0;
		for (int i = 0; i < input.length(); i++) {
			hashVal = input.charAt(i) + (31 * hashVal);
		}
		return hashVal;
	}

	/**
	 * 
	 * @param input non-null {@code String}
	 * @return non-negative hash number
	 */
	public static long djb2(String input) {
		long hash = 5381;
		for (int i = 0; i < input.length(); i++) {
			hash = ((hash << 5) + hash) + input.charAt(i);
		}
		return hash;
	}

	/**
	 * 
	 * @param input non-null {@code String}
	 * @return non-negative hash number
	 */
	public static long jenkinsOneAtATimeHash(String input) {
		long hash = 0;
		for (int i = 0; i < input.length(); i++) {
			hash += input.charAt(i);
			hash += (hash << 10);
			hash ^= (hash >> 6);
		}
		hash += (hash << 3);
		hash ^= (hash >> 11);
		hash += (hash << 15);
		return hash;
	}

	/**
	 * 
	 * @param input non-null {@code String}
	 * @return non-negative hash number
	 */
	public static long sdbm(String input) {
		long hash = 0;
		for (int i = 0; i < input.length(); i++) {
			hash = input.charAt(i) + (hash << 6) + (hash << 16) - hash;
		}
		return hash;
	}

	/**
	 * 
	 * @param input non-null {@code String}
	 * @return non-negative hash number
	 */
	public static long foldingHash(String input) {
		long hashVal = 0;
		int index = 0, currentFourBytes;
		do {
			currentFourBytes = getNextBytes(index, input);
			hashVal += currentFourBytes;
			index += 4;
		} while (currentFourBytes != 0);
		return hashVal;
	}

	/**
	 * This is a helper method to get the next four bytes of at the current index
	 * using the input. This method is used for
	 * {@code Memory.foldingHash(String input)}.
	 * 
	 * @param index a valid index position for the {@code String input} param
	 * @param input the {@code String input}
	 * @return the next four byte values
	 */
	private static int getNextBytes(int index, String input) {
		int currentFourBytes = 0;
		currentFourBytes += getByte(index, input);
		currentFourBytes += getByte(index + 1, input) << 8;
		currentFourBytes += getByte(index + 2, input) << 16;
		currentFourBytes += getByte(index + 3, input) << 24;
		return currentFourBytes;
	}

	/**
	 * Get the byte value associated at the index with the associated String input.
	 * Index values that would cause an error will return 0
	 * 
	 * @param index index to get the current byte
	 * @param input the {@code String input}
	 * @return the char value at the string index
	 */
	private static int getByte(int index, String input) {
		return index >= 0 && index < input.length() ? input.charAt(index) : 0;
	}

}
