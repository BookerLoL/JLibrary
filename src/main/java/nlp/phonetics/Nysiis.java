package main.nlp.phonetics;

/**
 * New York State Identification and Intelligence Algorithm 1970 algorithm for
 * standard English names.
 * 
 * 
 * Source: https://caversham.otago.ac.nz/files/working/ctp060902.pdf
 * 
 * Source:
 * https://en.wikipedia.org/wiki/New_York_State_Identification_and_Intelligence_System#cite_note-taft-2
 * 
 * Source: http://www.dropby.com/NYSIIS.html
 * 
 * Source Date: January 09, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class Nysiis extends Phonetizer {
	public static final int STANDARD_LENGTH = 6;
	public static final int NO_LIMIT_LENGTH = Integer.MAX_VALUE;
	private static final char[] VOWELS = { 'A', 'E', 'I', 'O', 'U' };
	private static final String[][] PREFIXES_AND_REPLACEMENTS = { { "MAC", "MCC" }, { "KN", "NN" }, { "K", "C" },
			{ "PH", "FF" }, { "PF", "FF" }, { "SCH", "SSS" } };
	private static final String[][] SUFFIXES_AND_REPLACEMENTS = { { "EE", "Y" }, { "IE", "Y" }, { "DT", "D" },
			{ "RT", "D" }, { "RD", "D" }, { "NT", "D" }, { "ND", "D" } };

	private int maxLength;

	public Nysiis() {
		this(NO_LIMIT_LENGTH);
	}

	public Nysiis(int length) {
		maxLength = length;
	}

	public Nysiis(boolean useStandardLength) {
		maxLength = useStandardLength ? STANDARD_LENGTH : NO_LIMIT_LENGTH;
	}

	private String replacePrefix(String name) {
		for (String[] prefixInfo : PREFIXES_AND_REPLACEMENTS) {
			if (name.startsWith(prefixInfo[0])) {
				name = prefixInfo[1] + name.substring(prefixInfo[0].length());
				break;
			}
		}
		return name;
	}

	private String replaceSuffix(String name) {
		for (String[] suffixInfo : SUFFIXES_AND_REPLACEMENTS) {
			if (name.endsWith(suffixInfo[0])) {
				name = name.substring(0, name.length() - suffixInfo[0].length()) + suffixInfo[1];
				break;
			}
		}
		return name;
	}

	private String transform(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		char prev = '\0';

		for (int i = 1; i < name.length(); i++) {
			char curr = name.charAt(i);
			if (curr != prev) { // avoid current char with last char added
				if (isVowel(curr, VOWELS)) {
					if (curr == 'E' && name.startsWith("V", i + 1)) {
						sb.append("AF");
						i++; // skip V
					} else {
						sb.append("A");
					}
				} else if (curr == 'Q') {
					sb.append('G');
				} else if (curr == 'M') {
					sb.append('N');
				} else if (curr == 'K') {
					if (name.startsWith("N", i + 1)) {
						sb.append('N');
						i++; // skip N
					} else {
						sb.append('C');
					}
				} else if (curr == 'S' && name.startsWith("CH", i + 1)) {
					sb.append("SSS");
					i += 2; // skip CH
				} else if (curr == 'W' && i != 1 && isVowel(prev, VOWELS)) {
					sb.append(prev);
				} else if (curr == 'H' && i != 1 && (!isVowel(prev, VOWELS)
						|| (i + 1 < name.length() && !isVowel(name.charAt(i + 1), VOWELS)))) {
					sb.append(prev);
				} else if (curr == 'Z') {
					sb.append('S');
				} else if (curr == 'P' && name.startsWith("H", i + 1)) {
					sb.append("FF");
					i++; // skip H
				} else {
					sb.append(curr);
				}
			}
			prev = sb.charAt(sb.length() - 1);
		}

		// check last character
		int currLength = sb.length();
		if (currLength >= 2 && sb.charAt(currLength - 2) == 'A' && sb.charAt(currLength - 1) == 'Y') {
			sb.deleteCharAt(currLength - 2);
		} else if (currLength >= 1) {
			char lastCh = sb.charAt(currLength - 1);
			if (lastCh == 'S' || lastCh == 'A') {
				sb.deleteCharAt(currLength - 1);
			}
		}

		return name.charAt(0) + removeAdjacentDuplicates(sb.toString());
	}

	private String removeAdjacentDuplicates(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		char prev = '\0';
		char curr;
		for (int i = 0; i < name.length(); i++) {
			curr = name.charAt(i);
			if (curr != prev) {
				sb.append(curr);
				prev = curr;
			}
		}
		return sb.toString();
	}

	@Override
	public String encode(String name) {
		name = name.trim().toUpperCase();
		name = replacePrefix(name);
		name = replaceSuffix(name);
		name = transform(name);
		return limitLength(name, maxLength);
	}
}
