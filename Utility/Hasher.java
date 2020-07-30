package utility;

public class Hasher {
	public static long lengthHashCode(String input) {
		return input.length();
	}

	public static long additiveCharacterHash(String input) {
		long hashVal = 0;
		for (int i = 0; i < input.length(); i++) {
			hashVal += input.charAt(i);
		}
		return hashVal;
	}

	public static long improveAdditiveCharacterHash(String input) {
		long hashVal = 0;
		for (int i = 0; i < input.length(); i++) {
			hashVal = input.charAt(i) + (31 * hashVal);
		}
		return hashVal;
	}

	public static long djb2(String input) {
		long hash = 5381;
		for (int i = 0; i < input.length(); i++) {
			hash = ((hash << 5) + hash) + input.charAt(i);
		}
		return hash;
	}

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

	public static long sdbm(String input) {
		long hash = 0;
		for (int i = 0; i < input.length(); i++) {
			hash = input.charAt(i) + (hash << 6) + (hash << 16) - hash;
		}
		return hash;
	}

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

	private static int getNextBytes(int index, String input) {
		int currentFourBytes = 0;
		currentFourBytes += getByte(index, input);
		currentFourBytes += getByte(index + 1, input) << 8;
		currentFourBytes += getByte(index + 2, input) << 16;
		currentFourBytes += getByte(index + 3, input) << 24;
		return currentFourBytes;
	}

	private static int getByte(int index, String input) {
		return index < input.length() ? input.charAt(index) : 0;
	}

}
