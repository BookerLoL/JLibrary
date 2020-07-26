package phonetics;

/*
 * Russell Soundex
 * 
 * For Standard English Names
 * 
 * https://caversham.otago.ac.nz/files/working/ctp060902.pdf
 * 
 * Note: If a name uses an apostrophe, then only the first letter before the apostrophe is kept.
 * Ex: Bo'donnel -> o'donnel 
 * 
 */
public class RussellSoundex extends Phonetizer {
	private static final int MAX_LENGTH = 5;
	private static final int IGNORE_ROW = 0;
	private static final int NOT_FOUND_ROW = -1;
	private static final char PAD = '0';
	private static final char[][] mapping = { { 'a', 'e', 'i', 'o', 'u', 'h', 'w', 'y' }, { 'b', 'f', 'p', 'v' },
			{ 'c', 'g', 'j', 'k', 'q', 's', 'x', 'z' }, { 'd', 't' }, { 'l' }, { 'm', 'n' }, { 'r' } };

	private static final String[] specialPrefixes = { "l'", "le", "la", "d'", "de", "di", "du", "dela", "con", "von",
			"van" };


	private boolean prefixIncluded;

	public RussellSoundex() {
		this(false);
	}

	public RussellSoundex(boolean prefixIncluded) {
		this.prefixIncluded = prefixIncluded;
	}

	private static int getEncoding(char ch) {
		ch = Character.toLowerCase(ch);
		if (!isLetter(ch)) {
			return NOT_FOUND_ROW;
		}

		for (int i = 0; i < mapping.length; i++) {
			char[] letters = mapping[i];
			for (char letter : letters) {
				if (ch == letter) {
					return i;
				}
			}
		}
		return NOT_FOUND_ROW;
	}

	private static boolean isIgnoreRow(int encodingRow) {
		return encodingRow == NOT_FOUND_ROW || encodingRow == IGNORE_ROW;
	}

	private static boolean isLetter(char ch) {
		return ch >= 'a' && ch <= 'z';
	}

	private String padZeros(StringBuilder sb) {
		while (sb.length() != MAX_LENGTH) {
			sb.append(PAD);
		}
		return sb.toString().toUpperCase();
	}

	public boolean isIncludingSpecialPrefixes() {
		return prefixIncluded;
	}

	private boolean isNotAllowed(char ch) {
		return ch == ' ' || ch == '\'' || ch == '-';
	}

	private String getLastPartOfFamilyName(String name) {
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

	private String startsWithSpecialPrefix(String name) {
		for (String prefix : specialPrefixes) {
			if (name.startsWith(prefix)) {
				return prefix;
			}
		}
		return null;
	}

	@Override
	public String encode(String name) {
		StringBuilder sb = new StringBuilder(MAX_LENGTH);
		if (name.isEmpty()) {
			return padZeros(sb);
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

		return padZeros(sb);
	}
}
