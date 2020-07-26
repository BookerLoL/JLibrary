package phonetics;

/*
 * New York State Identification and Intelligence Algorithm, Tafts original algorithm
 * 
 * For standard English
 * 
 * http://www.dropby.com/NYSIIS.html
 * 
 * 
 */
public class ModifiedNysiis extends Phonetizer {
	public static final int STANDARD_LENGTH = 6;
	public static final int NO_LIMIT_LENGTH = Integer.MAX_VALUE;
	private static final char[] VOWELS = { 'A', 'E', 'I', 'O', 'U' };
	private static final String[][] PREFIXES_AND_REPLACEMENTS = { { "MAC", "MC" }, { "PF", "F" } };
	private static final String[][] SUFFIXES_AND_REPLACEMENTS = { { "IX", "IC" }, { "EX", "EC" }, { "YE", "Y" },
			{ "EE", "Y" }, { "IE", "Y" } };
	private static final String[][] REPEAT_SUFFIXES_AND_REPLACEMENTS = { { "DT", "D" }, { "RT", "D" }, { "RD", "D" },
			{ "NT", "D" }, { "ND", "D" } };

	private static final char EMPTY_CHAR = '\0';

	private int maxLength;

	public ModifiedNysiis() {
		this(NO_LIMIT_LENGTH);
	}

	public ModifiedNysiis(int length) {
		maxLength = length;
	}

	public ModifiedNysiis(boolean useStandardLength) {
		maxLength = useStandardLength ? STANDARD_LENGTH : NO_LIMIT_LENGTH;
	}

	private boolean isVowel(char ch) {
		for (char vowel : VOWELS) {
			if (ch == vowel) {
				return true;
			}
		}
		return false;
	}

	private String removeAllTraillingSandZ(String name) {
		int i = name.length() - 1;
		char trailing = name.charAt(i);
		while (i != 0 && (trailing == 'S' || trailing == 'Z')) {
			i--;
			trailing = name.charAt(i);
		}

		i++; // since it will decrement i then check, keep at least 1 char even if it's an S
				// or Z
		return name.substring(0, i);
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

	private String replaceRepeatSuffixes(String name) {
		String prev = "";
		while (!prev.equals(name)) {
			prev = name;
			for (String[] rSuffixInfo : REPEAT_SUFFIXES_AND_REPLACEMENTS) {
				if (name.endsWith(rSuffixInfo[0])) {
					name = name.substring(0, name.length() - rSuffixInfo[0].length()) + rSuffixInfo[1];
					break;
				}
			}
		}
		return name;
	}

	// EV -> EF
	private String replaceEV(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			if (ch == 'E' && i != 0 && name.startsWith("V", i + 1)) {
				sb.append("EF");
				i++; // skip v
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private String removeWAfterVowelAndCollapseVowelsToA(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		char prev = EMPTY_CHAR;
		char ch;
		for (int i = 0; i < name.length(); i++) {
			ch = name.charAt(i);
			if (isVowel(ch)) {
				if (prev != 'A') {
					sb.append('A');
				}
			} else if (ch == 'W') {
				if (prev != 'A') {
					sb.append(ch);
				}
			} else {
				sb.append(ch);
			}
			prev = sb.charAt(sb.length() - 1);
		}
		return sb.toString();
	}

	private String transform1(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			int oneAhead = i + 1;
			if (ch == 'G' && name.startsWith("HT", oneAhead)) {
				sb.append("GT");
				i += 2;
			} else if (ch == 'D' && name.startsWith("G", oneAhead)) {
				sb.append('G');
				i++;
			} else if (ch == 'P' && name.startsWith("H", oneAhead)) {
				sb.append('F');
				i++;
			} else if (ch == 'H' && i != 0
					&& (isVowel(name.charAt(i - 1)) || (i + 1 < name.length() && isVowel(name.charAt(i + 1))))) {
				continue;
			} else if (ch == 'K') {
				if (name.startsWith("N", oneAhead)) {
					sb.append('N');
				} else {
					sb.append('C');
				}
			} else if (ch == 'M' && i != 0) {
				sb.append('N');
			} else if (ch == 'Q' && i != 0) {
				sb.append('G');
			} else if (ch == 'Y' && name.startsWith("W", oneAhead)) {
				i++;
				sb.append(ch);
			} else if (ch == 'W' && name.startsWith("R", oneAhead)) {
				i++;
				sb.append('R');
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private String transform2(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			if (name.startsWith("SCH", i)) {
				sb.append('S');
				i += 2;
			} else if (name.startsWith("SH", i)) {
				sb.append('S');
				i++;
			} else if (ch == 'Y' && i != 0 && i != name.length() - 1) {
				continue;
			} else if (ch == 'Z') {
				sb.append('S');
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private String removeTerminalAY(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			if (ch == 'Y' && sb.length() > 0 && sb.charAt(sb.length() - 1) == 'A') {
				int cutOff = sb.length() - 2;
				while (cutOff >= 0 && sb.charAt(cutOff) == 'A') { // Consecutive A's which would resulted in AAA...Y
																	// reduced to AY
					cutOff--;
				}
				sb.setLength(cutOff + 1);
				sb.append('Y');
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private String addFirstLetter(String name, char firstLetter) {
		if (firstLetter != EMPTY_CHAR) {
			int index = name.startsWith("A") ? 1 : 0;
			name = firstLetter + name.substring(index);
		}
		return name;
	}

	private char getFirstLetter(String name) {
		return isVowel(name.charAt(0)) ? name.charAt(0) : EMPTY_CHAR;
	}

	private String removeTrailingVowels(String name) {
		int index = name.length() - 1;
		while (index > 0 && isVowel(name.charAt(index))) {
			index--;
		}
		index++;
		return name.substring(0, index);
	}

	private String removeAdjacentDuplicates(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		char prev = EMPTY_CHAR;
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

	private String limitLength(String name) {
		if (name.length() <= maxLength) {
			return name;
		}
		return name.substring(0, maxLength);
	}

	@Override
	public String encode(String name) {
		name = name.trim().toUpperCase();
		char firstLetter = getFirstLetter(name);
		name = removeAllTraillingSandZ(name);
		name = replacePrefix(name);
		name = replaceSuffix(name);
		name = replaceRepeatSuffixes(name);
		name = replaceEV(name);
		name = removeWAfterVowelAndCollapseVowelsToA(name);
		name = transform1(name);
		name = transform2(name);
		name = removeTerminalAY(name);
		name = removeTrailingVowels(name);
		name = removeAdjacentDuplicates(name);
		name = addFirstLetter(name, firstLetter);
		return limitLength(name);
	}
}
