package main.nlp.phonetics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * American Soundex for standard English Names
 * 
 * Source: https://en.wikipedia.org/wiki/Soundex
 * 
 * Source Date: January 09, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class AmericanSoundex extends Phonetizer {
	private static final int MAX_LENGTH = 4;
	private static final int IGNORE_ROW = 0;
	private static final int NOT_FOUND_ROW = -1;
	private static final char PAD = '0';
	private static final Map<Character, Integer> CHAR_ENCODING_MAP = createCharEncodingMap();

	private static Map<Character, Integer> createCharEncodingMap() {
		Map<Character, Integer> map = new HashMap<>(26); // 26 letters in English alphabet
		Arrays.asList('a', 'e', 'i', 'o', 'u', 'h', 'w', 'y').forEach(c -> map.put(c, 0));
		Arrays.asList('b', 'f', 'p', 'v').forEach(c -> map.put(c, 1));
		Arrays.asList('c', 'g', 'j', 'k', 'q', 's', 'x', 'z').forEach(c -> map.put(c, 2));
		Arrays.asList('d', 't').forEach(c -> map.put(c, 3));
		Arrays.asList('l').forEach(c -> map.put(c, 4));
		Arrays.asList('m', 'n').forEach(c -> map.put(c, 5));
		Arrays.asList('r').forEach(c -> map.put(c, 6));
		return map;
	}

	private boolean reverse;

	public AmericanSoundex() {
		this(false);
	}

	public AmericanSoundex(boolean reverse) {
		this.reverse = reverse;
	}

	@Override
	public String encode(String name) {
		StringBuilder sb = new StringBuilder(MAX_LENGTH);

		if (name == null || name.isEmpty()) {
			return finalize(sb);
		}

		int index;
		int end;
		final char leadingChar;
		if (isReverse()) {
			index = 0;
			end = name.length() - 1;
			leadingChar = name.charAt(end);
		} else {
			index = 1;
			end = name.length();
			leadingChar = name.charAt(0);
		}

		sb.append(Character.toUpperCase(leadingChar));
		int prevEncoding = getEncoding(leadingChar);
		while (index < end && sb.length() < MAX_LENGTH) {
			int encoding = getEncoding(name.charAt(index));
			if (!isIgnoreRow(encoding) && encoding != prevEncoding) {
				sb.append(encoding);
				prevEncoding = encoding;
			}
			index++;
		}

		return finalize(sb);
	}

	private String finalize(StringBuilder sb) {
		return pad(sb, MAX_LENGTH, PAD).toString().toUpperCase();
	}

	public boolean isReverse() {
		return reverse;
	}

	private static int getEncoding(char ch) {
		return CHAR_ENCODING_MAP.getOrDefault(Character.toLowerCase(ch), NOT_FOUND_ROW);
	}

	private static boolean isIgnoreRow(int encodingRow) {
		return encodingRow == NOT_FOUND_ROW || encodingRow == IGNORE_ROW;
	}
}
