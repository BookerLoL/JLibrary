package main.nlp.stemmers;

/**
 * 
 * Snowball stemmer for standard Portuguese
 * 
 * Source: https://snowballstem.org/algorithms/portuguese/stemmer.html
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class PortugueseSnowballStemmer extends Stemmer {
	private static final int MIN_LENGTH = 2;
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'á', 'é', 'í', 'ó', 'ú', 'â', 'ê', 'ô' };

	private static final String[] STEP1_SUFFIXES1 = { "amentos", "imentos", "amento", "imento", "adoras", "adores",
			"aço~es", "ismos", "istas", "adora", "aça~o", "antes", "ância", "ezas", "icos", "icas", "ismo", "ável",
			"ível", "ista", "osos", "osas", "ador", "ante", "eza", "ico", "ica", "oso", "osa" };
	private static final String[] STEP1_SUFFIXES2 = { "logias", "logia" };
	private static final String STEP1_SUFFIXES2_REPLACEMENT = "log";
	private static final String[] STEP1_SUFFIXES4 = { "ências", "ência" };
	private static final String STEP1_SUFFIXES4_REPLACEMENT = "ente";
	private static final String STEP1_SUFFIXES5 = "amente";
	private static final String[] STEP1_SUFFIXES5_PRECEDING1 = { "ativ", "iv" };
	private static final String[] STEP1_SUFFIXES5_PRECEDING2 = { "os", "ic", "ad" };
	private static final String STEP1_SUFFIXES6 = "mente";
	private static final String[] STEP1_SUFFIXES6_PRECEDING1 = { "ante", "avel", "ível" };
	private static final String[] STEP1_SUFFIXES7 = { "idades", "idade" };
	private static final String[] STEP1_SUFFIXES7_PRECEDING1 = { "abil", "ic", "iv" };
	private static final String[] STEP1_SUFFIXES8 = { "ivos", "ivas", "ivo", "iva" };
	private static final String STEP1_SUFFIXES8_PRECEDING1 = "at";
	private static final String[] STEP1_SUFFIXES9 = { "iras", "ira" };
	private static final char STEP1_SUFFIXES9_PRECEDING1 = 'e';
	private static final String STEP1_SUFFIXES9_REPLACEMENT = "ir";
	private static final String[] STEP2_SUFFIXES1 = { "aríamos", "eríamos", "iríamos", "ássemos", "êssemos", "íssemos",
			"aremos", "aríeis", "eremos", "eríeis", "iremos", "iríeis", "áramos", "ásseis", "ávamos", "éramos",
			"ésseis", "íramos", "ísseis", "ara~o", "ardes", "areis", "ariam", "arias", "armos", "assem", "asses",
			"astes", "era~o", "erdes", "ereis", "eriam", "erias", "ermos", "essem", "esses", "estes", "ira~o", "irdes",
			"ireis", "iriam", "irias", "irmos", "issem", "isses", "istes", "áreis", "áveis", "éreis", "íamos", "íreis",
			"adas", "ados", "amos", "ando", "aram", "aras", "arei", "arem", "ares", "aria", "arás", "asse", "aste",
			"avam", "avas", "emos", "endo", "eram", "eras", "erei", "erem", "eres", "eria", "erás", "esse", "este",
			"idas", "idos", "imos", "indo", "iram", "iras", "irei", "irem", "ires", "iria", "irás", "isse", "iste",
			"ámos", "íeis", "ada", "ado", "ais", "ara", "ará", "ava", "eis", "era", "erá", "iam", "ias", "ida", "ido",
			"ira", "irá", "am", "ar", "as", "ei", "em", "er", "es", "eu", "ia", "ir", "is", "iu", "ou" };
	private static final String STEP3_SUFFIXES1 = "i";
	private static final char STEP3_SUFFIXES1_PRECEDING = 'c';
	private static final String[] STEP4_SUFFIXES1 = { "os", "a", "i", "o", "á", "í", "ó" };
	private static final String[] STEP5_SUFFIXES1 = { "e", "é", "ê" };
	private static final String[] STEP5_SUFFIXES1_PRECEDING1 = { "gu", "ci" };
	private static final String STEP5_SUFFIXES2_PRECEDING2 = "ç";
	private static final String STEP5_SUFFIXES2_REPLACEMENT2 = "c";

	private int R1;
	private int R2;
	private int RV;

	@Override
	public String stem(String word) {
		word = normalize(word);

		if (word.length() < MIN_LENGTH) {
			return word;
		}
		word = replace(word);
		markRegions(word);
		String didntChange = word;
		word = step1(word);

		if (didntChange.equals(word)) {
			word = step2(word);
		}
		if (!didntChange.equals(word)) {
			word = step3(word);
		}

		if (didntChange.equals(word)) {
			word = step4(word);
		}

		word = step5(word);
		return replaceBack(word);
	}

	private void markRegions(String word) {
		RV = markRV(word);
		R1 = calcR1VC(word, VOWELS);
		R2 = calcR2VC(word, R1, VOWELS);
	}

	private int markRV(String word) {
		int rv = word.length();
		char secondLetter = word.charAt(1);
		if (isConsonant(secondLetter, VOWELS)) { // X C
			for (int i = 2; i < word.length(); i++) {
				if (isVowel(word.charAt(i), VOWELS)) {
					rv = i + 1;
					break;
				}
			}
		} else if (isVowel(word.charAt(0), VOWELS)) { // V V
			for (int i = 2; i < word.length(); i++) {
				if (isConsonant(word.charAt(i), VOWELS)) {
					rv = i + 1;
					break;
				}
			}
		} else {
			rv = 3;
		}
		return rv;
	}

	private String replace(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (ch == 'ã') {
				sb.append("a~");
			} else if (ch == 'õ') {
				sb.append("o~");
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private String replaceBack(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (ch == '~') {
				char prev = sb.charAt(sb.length() - 1);
				sb.deleteCharAt(sb.length() - 1);
				if (prev == 'a') {
					sb.append('ã');
				} else if (prev == 'o') {
					sb.append('õ');
				}
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private String step1(String word) {
		String RVString = getRegionSubstring(word, RV);
		String R1String = getRegionSubstring(word, R1);
		String R2String = getRegionSubstring(word, R2);

		for (String suffix : STEP1_SUFFIXES1) {
			if (R2String.endsWith(suffix)) {
				return removeEnding(word, suffix.length());
			}
		}

		for (String suffix : STEP1_SUFFIXES2) {
			if (R2String.endsWith(suffix)) {
				return removeEnding(word, suffix.length()) + STEP1_SUFFIXES2_REPLACEMENT;
			}
		}

		for (String suffix : STEP1_SUFFIXES4) {
			if (R2String.endsWith(suffix)) {
				return removeEnding(word, suffix.length()) + STEP1_SUFFIXES4_REPLACEMENT;
			}
		}

		if (R1String.endsWith(STEP1_SUFFIXES5)) {
			word = removeEnding(word, STEP1_SUFFIXES5.length());
			R2String = removeEnding(R2String, STEP1_SUFFIXES5.length());

			for (String PRECEDINGSuffix : STEP1_SUFFIXES5_PRECEDING1) {
				if (R2String.endsWith(PRECEDINGSuffix)) {
					return removeEnding(word, PRECEDINGSuffix.length());
				}
			}

			for (String PRECEDINGSuffix : STEP1_SUFFIXES5_PRECEDING2) {
				if (R2String.endsWith(PRECEDINGSuffix)) {
					return removeEnding(word, PRECEDINGSuffix.length());
				}
			}
			return word;
		}

		if (R2String.endsWith(STEP1_SUFFIXES6)) {
			word = removeEnding(word, STEP1_SUFFIXES6.length());
			R2String = removeEnding(R2String, STEP1_SUFFIXES6.length());
			for (String PRECEDINGSuffix : STEP1_SUFFIXES6_PRECEDING1) {
				if (R2String.endsWith(PRECEDINGSuffix)) {
					return removeEnding(word, PRECEDINGSuffix.length());
				}
			}
			return word;
		}

		for (String suffix : STEP1_SUFFIXES7) {
			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				R2String = removeEnding(R2String, suffix.length());
				for (String PRECEDINGSuffix : STEP1_SUFFIXES7_PRECEDING1) {
					if (R2String.endsWith(PRECEDINGSuffix)) {
						return removeEnding(word, PRECEDINGSuffix.length());
					}
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES8) {
			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				R2String = removeEnding(R2String, suffix.length());
				if (word.endsWith(STEP1_SUFFIXES8_PRECEDING1)) {
					if (R2String.endsWith(STEP1_SUFFIXES8_PRECEDING1)) {
						return removeEnding(word, STEP1_SUFFIXES8_PRECEDING1.length());
					}
				}
				return word;
			}

		}

		for (String suffix : STEP1_SUFFIXES9) {
			if (RVString.endsWith(suffix) && word.length() > suffix.length()
					&& word.charAt(word.length() - suffix.length() - 1) == STEP1_SUFFIXES9_PRECEDING1) {
				return removeEnding(word, suffix.length()) + STEP1_SUFFIXES9_REPLACEMENT;
			}
		}

		return word;
	}

	private String step2(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : STEP2_SUFFIXES1) {
			if (RVString.endsWith(suffix)) {
				return removeEnding(word, suffix.length());
			}
		}
		return word;
	}

	private String step3(String word) {
		if (word.length() > 1 && word.charAt(word.length() - 2) == STEP3_SUFFIXES1_PRECEDING
				&& getRegionSubstring(word, RV).endsWith(STEP3_SUFFIXES1)) {
			word = removeEnding(word, 1);
		}
		return word;
	}

	private String step4(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : STEP4_SUFFIXES1) {
			if (RVString.endsWith(suffix)) {
				return removeEnding(word, suffix.length());
			}
		}
		return word;
	}

	private String step5(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : STEP5_SUFFIXES1) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				RVString = removeEnding(RVString, suffix.length());
				for (String precedSuffix : STEP5_SUFFIXES1_PRECEDING1) {
					if (word.endsWith(precedSuffix)) {
						if (RVString.endsWith(precedSuffix.substring(precedSuffix.length() - 1))) {
							return removeEnding(word, 1);
						}
						break;
					}
				}
				return word;
			}
		}

		if (word.endsWith(STEP5_SUFFIXES2_PRECEDING2)) {
			word = removeEnding(word, 1) + STEP5_SUFFIXES2_REPLACEMENT2;
		}
		return word;
	}

	@Override
	public Language getLanguage() {
		return Language.PORTUGUESE;
	}
}
