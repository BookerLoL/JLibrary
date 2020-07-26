package stemmers;

/*
 * Snowball stemmer
 * 
 * For Standard Spanish
 * 
 * https://snowballstem.org/algorithms/spanish/stemmer.html
 * 
 */
public class SpanishSnowballStemmer extends Stemmer {
	private static final int MIN_LENGTH = 2;
	private static final String[] step0_suffixes = { "selos", "selas", "selo", "sela", "las", "les", "los", "nos", "la",
			"le", "lo", "me", "se" };
	// keep step0_precedingSuffix1 & step0_precedingSuffix2 in same order, otherwise code logic is incorrect
	private static final String[] step0_precedingSuffix1 = { "iéndo", "ándo", "ár", "ér", "ír" };
	private static final String[] step0_precedingSuffix2 = { "iendo", "ando", "ar", "er", "ir" };
	private static final String step0_precedingSuffix3 = "yendo";
	private static final String[] step2a_suffixes1 = { "yeron", "yendo", "yamos", "yais", "yan", "yen", "yas", "yes",
			"ya", "ye", "yo", "yó", };
	private static final String[] step1_suffixes1 = { "amientos", "imientos", "amiento", "imiento", "anzas", "ismos",
			"ables", "ibles", "istas", "anza", "icos", "icas", "ismo", "able", "ible", "ista", "osos", "osas", "ico",
			"ica", "oso", "osa" };
	private static final String[] step1_suffixes2 = { "aciones", "adoras", "adores", "ancias", "adora", "ación",
			"antes", "ancia", "ador", "ante" };
	private static final String[] step1_suffixes3 = { "logías", "logía" };
	private static final String[] step1_suffixes4 = { "uciones", "ución" };
	private static final String[] step1_suffixes5 = { "encias", "encia" };
	private static final String[] step1_suffixes6_1 = { "ativ", "iv", "os", "ic", "ad" };
	private static final String[] step1_suffixes8 = { "idades", "idad" };
	private static final String[] step1_suffixes8_2 = { "abil", "ic", "iv" };
	private static final String[] step1_suffixes7_1 = { "ante", "able", "ible" };
	private static final String[] step1_suffixes9 = { "ivos", "ivas", "iva", "ivo" };
	private static final String[] step2b_suffixes1 = { "aríamos", "eríamos", "iríamos", "iéramos", "iésemos", "ierais",
			"aríais", "aremos", "eríais", "eremos", "iríais", "iremos", "ieseis", "asteis", "isteis", "ábamos",
			"áramos", "ásemos", "arían", "arías", "aréis", "erían", "erías", "eréis", "irían", "irías", "iréis",
			"ieran", "iesen", "ieron", "iendo", "ieras", "ieses", "abais", "arais", "aseis", "íamos", "arán", "arás",
			"aría", "erán", "erás", "ería", "irán", "irás", "iría", "iera", "iese", "aste", "iste", "aban", "aran",
			"asen", "aron", "ando", "abas", "adas", "idas", "aras", "ases", "íais", "ados", "idos", "amos", "imos",
			"ará", "aré", "erá", "eré", "irá", "iré", "aba", "ada", "ida", "ara", "ase", "ían", "ado", "ido", "ías",
			"áis", "ía", "ad", "ed", "id", "an", "ió", "ar", "er", "ir", "as", "ís" };
	private static final String[] step2b_suffixes2 = { "emos", "éis", "es", "en" };
	private static final String[] step3_suffixes1 = { "os", "a", "o", "á", "í", "ó" };
	private static final String[] step3_suffixes2 = { "e", "é" };
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'á', 'é', 'í', 'ó', 'ú', 'ü' };
	
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
				R1 = i + 2; // Get area after combination
				break;
			}
		}

		for (int i = R1; i < word.length() - 2; i++) {
			if (isVowel(word.charAt(i)) && isConsonant(word.charAt(i + 1))) {
				R2 = i + 2; // Get area after combination
				break;
			}
		}
	}
	
	private String step0(String word) {
		for (String suffix : step0_suffixes) {
			if (word.endsWith(suffix)) {
				boolean skip = false;
				String temp = removeEnding(word, suffix.length());
				String RVtemp = getRegionSubstring(temp, RV);

				for (int i = 0; i < step0_precedingSuffix1.length; i++) {
					if (RVtemp.endsWith(step0_precedingSuffix1[i])) {
						RVtemp = removeEnding(temp, step0_precedingSuffix1[i].length()) + step0_precedingSuffix2[i];
						temp = removeEnding(temp, step0_precedingSuffix1[i].length()) + step0_precedingSuffix2[i];
					}

					if (RVtemp.endsWith(step0_precedingSuffix2[i])) {
						word = temp;
						skip = true;
						break;
					}
				}

				if (!skip) {
					if (RVtemp.endsWith(step0_precedingSuffix3) && temp.length() > step0_precedingSuffix3.length()
							&& temp.charAt(temp.length() - step0_precedingSuffix3.length() - 1) == 'u') {
						word = temp;
					}
				}
				break;
			}
		}
		return word;
	}

	private String step1(String word) {
		String R2String = getRegionSubstring(word, R2);

		for (String suffix : step1_suffixes1) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes2) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());
					if (R2String.endsWith("ic")) {
						word = removeEnding(word, 2);
					}
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes3) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + "log";
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes4) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + "u";
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes5) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + "ente";
				}
				return word;
			}
		}

		String R1String = getRegionSubstring(word, R1);
		if (R1String.endsWith("amente")) {
			word = removeEnding(word, 6);
			R2String = getRegionSubstring(word, R2);

			for (String suffix : step1_suffixes6_1) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					break;
				}
			}
			return word;
		} else if (R2String.endsWith("mente")) {
			word = removeEnding(word, 5);
			R2String = getRegionSubstring(word, R2);

			for (String suffix : step1_suffixes7_1) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					break;
				}
			}
			return word;
		}

		for (String suffix : step1_suffixes8) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());
					for (String suffix_2 : step1_suffixes8_2) {
						if (R2String.endsWith(suffix_2)) {
							word = removeEnding(word, suffix_2.length());
							break;
						}
					}
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes9) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());
					if (R2String.endsWith("at")) {
						word = removeEnding(word, 2);
					}
				}
				break;
			}
		}
		return word;
	}

	private String step2a(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : step2a_suffixes1) {
			if (RVString.endsWith(suffix)) {
				if (word.length() > suffix.length() && word.charAt(word.length() - suffix.length() - 1) == 'u') {
					word = removeEnding(word, suffix.length());
				}
				break;
			}
		}
		return word;
	}

	private String step2b(String word) {
		String RVString = getRegionSubstring(word, RV);

		for (String suffix : step2b_suffixes1) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				return word;
			}
		}

		for (String suffix : step2b_suffixes2) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				if (word.endsWith("gu")) {
					word = removeEnding(word, 1);
				}
				break;
			}
		}
		return word;
	}

	private String step3(String word) {
		String RVString = getRegionSubstring(word, RV);

		for (String suffix : step3_suffixes1) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				return word;
			}
		}

		for (String suffix : step3_suffixes2) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				if (RVString.length() > 1 && RVString.charAt(RVString.length() - 2) == 'u' && word.endsWith("gu")) {
					word = removeEnding(word, 1);
				}
				break;
			}
		}

		return word;
	}

	private String finalize(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (ch == 'á') {
				sb.append('a');
			} else if (ch == 'é') {
				sb.append('e');
			} else if (ch == 'í') {
				sb.append('i');
			} else if (ch == 'ó') {
				sb.append('o');
			} else if (ch == 'ú') {
				sb.append('u');
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
	
	@Override
	public String stem(String word) {
		word = normalize(word);
		
		if (word.length() < MIN_LENGTH) {
			return finalize(word);
		}

		markRegions(word);
		word = step0(word);
		String didntChange = word;
		word = step1(word);

		if (didntChange.equals(word)) {
			word = step2a(word);
		}
		if (didntChange.equals(word)) {
			word = step2b(word);
		}
		
		word = step3(word);
		return finalize(word);
	}
}
