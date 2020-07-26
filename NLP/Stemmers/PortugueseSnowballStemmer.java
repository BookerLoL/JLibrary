package stemmers;
/*
 * Snowball stemmer
 * 
 * For Standard Portuguese
 * 
 * https://snowballstem.org/algorithms/portuguese/stemmer.html
 * 
 */
public class PortugueseSnowballStemmer extends Stemmer {
	private static final int MIN_LENGTH = 2;
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'á', 'é', 'í', 'ó', 'ú', 'â', 'ê', 'ô' };
	private static final String[] step1_suffixes1 = { "amentos", "imentos", "amento", "imento", "adoras", "adores",
			"aço~es", "ismos", "istas", "adora", "aça~o", "antes", "ância", "ezas", "icos", "icas", "ismo", "ável",
			"ível", "ista", "osos", "osas", "ador", "ante", "eza", "ico", "ica", "oso", "osa" };
	private static final String[] step1_suffixes2 = { "logias", "logia" };
	private static final String step1_suffixes2_replacement = "log";
	private static final String[] step1_suffixes3 = { "uço~es", "uça~o" };
	private static final String step1_suffixes3_replacement = "u";
	private static final String[] step1_suffixes4 = { "ências", "ência" };
	private static final String step1_suffixes4_replacement = "ente";
	private static final String step1_suffixes5 = "amente";
	private static final String[] step1_suffixes5_preceding1 = { "ativ", "iv" };
	private static final String[] step1_suffixes5_preceding2 = { "os", "ic", "ad" };
	private static final String step1_suffixes6 = "mente";
	private static final String[] step1_suffixes6_preceding1 = { "ante", "avel", "ível" };
	private static final String[] step1_suffixes7 = { "idades", "idade" };
	private static final String[] step1_suffixes7_preceding1 = { "abil", "ic", "iv" };
	private static final String[] step1_suffixes8 = { "ivos", "ivas", "ivo", "iva" };
	private static final String step1_suffixes8_preceding1 = "at";
	private static final String[] step1_suffixes9 = { "iras", "ira" };
	private static final char step1_suffixes9_preceding1 = 'e';
	private static final String step1_suffixes9_replacement = "ir";
	private static final String[] step2_suffixes1 = { "aríamos", "eríamos", "iríamos", "ássemos", "êssemos", "íssemos",
			"aremos", "aríeis", "eremos", "eríeis", "iremos", "iríeis", "áramos", "ásseis", "ávamos", "éramos",
			"ésseis", "íramos", "ísseis", "ara~o", "ardes", "areis", "ariam", "arias", "armos", "assem", "asses",
			"astes", "era~o", "erdes", "ereis", "eriam", "erias", "ermos", "essem", "esses", "estes", "ira~o", "irdes",
			"ireis", "iriam", "irias", "irmos", "issem", "isses", "istes", "áreis", "áveis", "éreis", "íamos", "íreis",
			"adas", "ados", "amos", "ando", "aram", "aras", "arei", "arem", "ares", "aria", "arás", "asse", "aste",
			"avam", "avas", "emos", "endo", "eram", "eras", "erei", "erem", "eres", "eria", "erás", "esse", "este",
			"idas", "idos", "imos", "indo", "iram", "iras", "irei", "irem", "ires", "iria", "irás", "isse", "iste",
			"ámos", "íeis", "ada", "ado", "ais", "ara", "ará", "ava", "eis", "era", "erá", "iam", "ias", "ida", "ido",
			"ira", "irá", "am", "ar", "as", "ei", "em", "er", "es", "eu", "ia", "ir", "is", "iu", "ou" };
	private static final String step3_suffixes1 = "i";
	private static final char step3_suffixes1_preceding = 'c';
	private static final String[] step4_suffixes1 = { "os", "a", "i", "o", "á", "í", "ó" };
	private static final String[] step5_suffixes1 = { "e", "é", "ê" };
	private static final String[] step5_suffixes1_preceding1 = { "gu", "ci" };
	private static final String step5_suffixes2_preceding2 = "ç";
	private static final String step5_suffixes2_replacement2 = "c";
	
	private int R1;
	private int R2;
	private int RV;


	private static boolean isVowel(char ch) {
		for (char vowel : VOWELS) {
			if (ch == vowel) {
				return true;
			}
		}
		return false;
	}

	private static boolean isConsonant(char ch) {
		return !isVowel(ch);
	}

	private void markRegions(String word) {
		markRV(word);
		markRNumbers(word);
	}

	private void markRV(String word) {
		RV = word.length();
		char secondLetter = word.charAt(1);
		if (isConsonant(secondLetter)) { // X C
			for (int i = 2; i < word.length(); i++) {
				if (isVowel(word.charAt(i))) {
					RV = i + 1;
					break;
				}
			}
		} else if (isVowel(word.charAt(0))) { // V V
			for (int i = 2; i < word.length(); i++) {
				if (isConsonant(word.charAt(i))) {
					RV = i + 1;
					break;
				}
			}
		} else {
			RV = 3;
		}
	}

	private void markRNumbers(String word) {
		R1 = word.length();
		R2 = R1;

		for (int i = 0; i < word.length() - 2; i++) {
			if (isVowel(word.charAt(i)) && isConsonant(word.charAt(i + 1))) {
				R1 = i + 2; 
				break;
			}
		}

		for (int i = R1; i < word.length() - 2; i++) {
			if (isVowel(word.charAt(i)) && isConsonant(word.charAt(i + 1))) {
				R2 = i + 2; 
				break;
			}
		}
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

		for (String suffix : step1_suffixes1) {
			if (R2String.endsWith(suffix)) {
				return removeEnding(word, suffix.length());
			}
		}

		for (String suffix : step1_suffixes2) {
			if (R2String.endsWith(suffix)) {
				return removeEnding(word, suffix.length()) + step1_suffixes2_replacement;
			}
		}

		for (String suffix : step1_suffixes3) {
			if (R2String.endsWith(suffix)) {
				return removeEnding(word, suffix.length()) + step1_suffixes3_replacement;
			}
		}

		for (String suffix : step1_suffixes4) {
			if (R2String.endsWith(suffix)) {
				return removeEnding(word, suffix.length()) + step1_suffixes4_replacement;
			}
		}

		if (R1String.endsWith(step1_suffixes5)) {
			word = removeEnding(word, step1_suffixes5.length());
			R2String = removeEnding(R2String, step1_suffixes5.length());

			for (String precedingSuffix : step1_suffixes5_preceding1) {
				if (R2String.endsWith(precedingSuffix)) {
					word = removeEnding(word, precedingSuffix.length());
					return word;
				}
			}

			for (String precedingSuffix : step1_suffixes5_preceding2) {
				if (R2String.endsWith(precedingSuffix)) {
					word = removeEnding(word, precedingSuffix.length());
					return word;
				}
			}
			return word;
		}

		if (R2String.endsWith(step1_suffixes6)) {
			word = removeEnding(word, step1_suffixes6.length());
			R2String = removeEnding(R2String, step1_suffixes6.length());
			for (String precedingSuffix : step1_suffixes6_preceding1) {
				if (R2String.endsWith(precedingSuffix)) {
					word = removeEnding(word, precedingSuffix.length());
					break;
				}
			}
			return word;
		}

		for (String suffix : step1_suffixes7) {
			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				R2String = removeEnding(R2String, suffix.length());
				for (String precedingSuffix : step1_suffixes7_preceding1) {
					if (R2String.endsWith(precedingSuffix)) {
						word = removeEnding(word, precedingSuffix.length());
						break;
					}
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes8) {
			if (R2String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				R2String = removeEnding(R2String, suffix.length());
				if (word.endsWith(step1_suffixes8_preceding1)) {
					if (R2String.endsWith(step1_suffixes8_preceding1)) {
						word = removeEnding(word, step1_suffixes8_preceding1.length());
					}
				}
				return word;
			}

		}

		for (String suffix : step1_suffixes9) {
			if (RVString.endsWith(suffix) && word.length() > suffix.length()
					&& word.charAt(word.length() - suffix.length() - 1) == step1_suffixes9_preceding1) {
				word = removeEnding(word, suffix.length()) + step1_suffixes9_replacement;
				break;
			}
		}

		return word;
	}

	private String step2(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : step2_suffixes1) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				break;
			}
		}
		return word;
	}

	private String step3(String word) {
		String RVString = getRegionSubstring(word, RV);
		if (RVString.endsWith(step3_suffixes1) && word.length() > 1
				&& word.charAt(word.length() - 2) == step3_suffixes1_preceding) {
			word = removeEnding(word, 1);
		}
		return word;
	}

	private String step4(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : step4_suffixes1) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				break;
			}
		}
		return word;
	}
	
	private String step5(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : step5_suffixes1) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				RVString = removeEnding(RVString, suffix.length());
				for (String precedSuffix : step5_suffixes1_preceding1) {
					if (word.endsWith(precedSuffix)) {
						if (RVString.endsWith(precedSuffix.substring(precedSuffix.length() - 1))) {
							word = removeEnding(word, 1);
							return word;
						}
						break;
					}
				}
				return word;
			}
		}

		if (word.endsWith(step5_suffixes2_preceding2)) {
			word = removeEnding(word, 1) + step5_suffixes2_replacement2;
		}
		return word;
	}

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
}
