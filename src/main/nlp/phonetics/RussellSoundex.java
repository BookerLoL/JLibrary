package main.nlp.phonetics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Russell Sound for standard English names.
 * 
 * Source: https://caversham.otago.ac.nz/files/working/ctp060902.pdf
 * 
 * Source Date: January 09, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class RussellSoundex extends Phonetizer {
	private static final int MAX_LENGTH = 5;
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

	private static final String[] SPECIAL_PREFIXES = { "l'", "le", "la", "d'", "de", "di", "du", "dela", "con", "von",
			"van" };

	private boolean prefixIncluded;

	public RussellSoundex() {
		this(false);
	}

	public RussellSoundex(boolean prefixIncluded) {
		this.prefixIncluded = prefixIncluded;
	}

	@Override
	public String encode(String name) {
		StringBuilder sb = new StringBuilder(MAX_LENGTH);
		if (name == null || name.isEmpty()) {
			return pad(sb, MAX_LENGTH, PAD).toString();
		}

		name = name.trim();
		name = getLastPartOfFamilyName(name);
		int end = name.length();
		int idx;
		final char leadingChar;
		String prefix = startsWithSpecialPrefix(name);
		if (!isIncludingSpecialPrefixes() && prefix != null) {
			idx = prefix.length() + 1;
			leadingChar = name.charAt(idx - 1);
		} else {
			idx = 1;
			leadingChar = name.charAt(0);
		}

		sb.append(Character.toUpperCase(leadingChar));
		sb.append('-');
		int prevEncoding = getEncoding(leadingChar);
		while (idx < end && sb.length() != MAX_LENGTH) {
			int encoding = getEncoding(name.charAt(idx));
			if (!isIgnoreRow(encoding) && encoding != prevEncoding) {
				sb.append(encoding);
				prevEncoding = encoding;
			}
			idx++;
		}

		return pad(sb, MAX_LENGTH, PAD).toString();
	}

	public boolean isIncludingSpecialPrefixes() {
		return prefixIncluded;
	}

	private static String getLastPartOfFamilyName(String name) {
		int i = name.length() - 1;
		while (i >= 0) {
			if (isNotAllowed(name.charAt(i))) {
				if (i > 0 && !isNotAllowed(name.charAt(i - 1))) {
					i -= 2;
				}
				break;
			}
			i--;
		}
		return name.substring(i + 1).toLowerCase();
	}

	private static String startsWithSpecialPrefix(String name) {
		for (String prefix : SPECIAL_PREFIXES) {
			if (name.startsWith(prefix)) {
				return prefix;
			}
		}
		return null;
	}

	private static int getEncoding(char ch) {
		return CHAR_ENCODING_MAP.getOrDefault(Character.toLowerCase(ch), NOT_FOUND_ROW);
	}

	private static boolean isIgnoreRow(int encodingRow) {
		return encodingRow == NOT_FOUND_ROW || encodingRow == IGNORE_ROW;
	}

	private static boolean isNotAllowed(char ch) {
		return ch == ' ' || ch == '\'' || ch == '-';
	}
}
