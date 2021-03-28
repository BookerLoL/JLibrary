package main.util.compression;

public class RunLength {
	public static String encode(String input) {
		StringBuilder sb = new StringBuilder();
		char prev = input.charAt(0);
		int count = 0;
		for (int i = 0; i < input.length(); i++) {
			char curr = input.charAt(i);
			if (curr != prev) {
				sb.append(prev);
				sb.append(count);
				count = 0;
			}
			count++;
			prev = curr;
		}

		sb.append(prev);
		sb.append(count);
		return sb.toString();
	}

	public static String decode(String encoding) {
		int totalCapacity = 0;
		for (int i = 1; i < encoding.length(); i += 2) {
			totalCapacity += encoding.charAt(i);
		}

		StringBuilder sb = new StringBuilder(totalCapacity);
		for (int i = 0; i < encoding.length(); i += 2) {
			char letter = encoding.charAt(i);
			int number = encoding.charAt(i + 1) - '0';
			while (number > 0) {
				sb.append(letter);
				number--;
			}
		}
		return sb.toString();
	}
}
