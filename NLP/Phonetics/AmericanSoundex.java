package phonetics;

/*
 * American Soundex
 * 
 * For Standard English Names
 * 
 * https://en.wikipedia.org/wiki/Soundex#:~:text=Soundex%20is%20a%20phonetic%20algorithm,despite%20minor%20differences%20in%20spelling.
 * 
 * Note: need to modify code to handle uppercase code instead of relying on toUpperCase()
 */
public class AmericanSoundex extends Phonetizer {
	private static final int MAX_LENGTH = 4;
	private static final int IGNORE_ROW = 0;
	private static final int NOT_FOUND_ROW = -1;
	private static final char PAD = '0';
	private static final char[][] mapping = { { 'a', 'e', 'i', 'o', 'u', 'h', 'w', 'y' }, { 'b', 'f', 'p', 'v' },
			{ 'c', 'g', 'j', 'k', 'q', 's', 'x', 'z' }, { 'd', 't' }, { 'l' }, { 'm', 'n' }, { 'r' } };

	private boolean reverse;

	public AmericanSoundex() {
		this(false);
	}

	public AmericanSoundex(boolean reverse) {
		this.reverse = reverse;
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

	public boolean isReverse() {
		return reverse;
	}

	@Override
	public String encode(String name) {
		StringBuilder sb = new StringBuilder(MAX_LENGTH);
		if (name.isEmpty()) {
			return padZeros(sb);
		}

		int idx;
		int end;
		final char leadingChar;
		if (isReverse()) {
			idx = 0;
			end = name.length() - 1;
			leadingChar = name.charAt(end);
		} else {
			idx = 1;
			end = name.length();
			leadingChar = name.charAt(0);
		}

		sb.append(Character.toUpperCase(leadingChar));
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
