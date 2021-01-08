package main.nlp.stemmers;

/**
 * 
 * Snowball stemmer for standard Spanish
 * 
 * Source: https://snowballstem.org/algorithms/spanish/stemmer.html
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan
 * @version 1.0
 */
public class SpanishSnowballStemmer extends Stemmer {
	private static final int MIN_LENGTH = 2;
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'á', 'é', 'í', 'ó', 'ú', 'ü' };

	private static final String[] STEP0_SUFFIXES = { "selos", "selas", "selo", "sela", "las", "les", "los", "nos", "la",
			"le", "lo", "me", "se" };
	private static final String[] STEP0_SUFFIXES_PRECEDING1 = { "iéndo", "ándo", "ár", "ér", "ír" };
	private static final String[] STEP0_SUFFIXES_PRECEDING2 = { "iendo", "ando", "ar", "er", "ir" };
	private static final String STEP0_SUFFIXES_PRECEDING3 = "yendo";
	private static final String[] STEP2A_SUFFIXES1 = { "yeron", "yendo", "yamos", "yais", "yan", "yen", "yas", "yes",
			"ya", "ye", "yo", "yó", };
	private static final String[] STEP1_SUFFIXES1 = { "amientos", "imientos", "amiento", "imiento", "anzas", "ismos",
			"ables", "ibles", "istas", "anza", "icos", "icas", "ismo", "able", "ible", "ista", "osos", "osas", "ico",
			"ica", "oso", "osa" };
	private static final String[] STEP1_SUFFIXES2 = { "aciones", "adoras", "adores", "ancias", "adora", "ación",
			"antes", "ancia", "ador", "ante" };
	private static final String[] STEP1_SUFFIXES3 = { "logías", "logía" };
	private static final String[] STEP1_SUFFIXES4 = { "uciones", "ución" };
	private static final String[] STEP1_SUFFIXES5 = { "encias", "encia" };
	private static final String[] STEP1_SUFFIXES6_1 = { "ativ", "iv", "os", "ic", "ad" };
	private static final String[] STEP1_SUFFIXES8 = { "idades", "idad" };
	private static final String[] STEP1_SUFFIXES8_2 = { "abil", "ic", "iv" };
	private static final String[] STEP1_SUFFIXES7_1 = { "ante", "able", "ible" };
	private static final String[] STEP1_SUFFIXES9 = { "ivos", "ivas", "iva", "ivo" };
	private static final String[] STEP2B_SUFFIXES1 = { "aríamos", "eríamos", "iríamos", "iéramos", "iésemos", "ierais",
			"aríais", "aremos", "eríais", "eremos", "iríais", "iremos", "ieseis", "asteis", "isteis", "ábamos",
			"áramos", "ásemos", "arían", "arías", "aréis", "erían", "erías", "eréis", "irían", "irías", "iréis",
			"ieran", "iesen", "ieron", "iendo", "ieras", "ieses", "abais", "arais", "aseis", "íamos", "arán", "arás",
			"aría", "erán", "erás", "ería", "irán", "irás", "iría", "iera", "iese", "aste", "iste", "aban", "aran",
			"asen", "aron", "ando", "abas", "adas", "idas", "aras", "ases", "íais", "ados", "idos", "amos", "imos",
			"ará", "aré", "erá", "eré", "irá", "iré", "aba", "ada", "ida", "ara", "ase", "ían", "ado", "ido", "ías",
			"áis", "ía", "ad", "ed", "id", "an", "ió", "ar", "er", "ir", "as", "ís" };
	private static final String[] STEP2B_SUFFIXES2 = { "emos", "éis", "es", "en" };
	private static final String[] STEP3_SUFFIXES1 = { "os", "a", "o", "á", "í", "ó" };
	private static final String[] STEP3_SUFFIXES2 = { "e", "é" };

	private int R1;
	private int R2;
	private int RV;

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
			word = STEP2A(word);
		}
		if (didntChange.equals(word)) {
			word = STEP2B(word);
		}

		word = step3(word);
		return finalize(word);
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

	private String step0(String word) {
		for (String suffix : STEP0_SUFFIXES) {
			if (word.endsWith(suffix)) {
				boolean skip = false;
				String temp = removeEnding(word, suffix.length());
				String RVtemp = getRegionSubstring(temp, RV);

				for (int i = 0; i < STEP0_SUFFIXES_PRECEDING1.length; i++) {
					if (RVtemp.endsWith(STEP0_SUFFIXES_PRECEDING1[i])) {
						RVtemp = removeEnding(temp, STEP0_SUFFIXES_PRECEDING1[i].length())
								+ STEP0_SUFFIXES_PRECEDING2[i];
						temp = removeEnding(temp, STEP0_SUFFIXES_PRECEDING1[i].length()) + STEP0_SUFFIXES_PRECEDING2[i];
					}

					if (RVtemp.endsWith(STEP0_SUFFIXES_PRECEDING2[i])) {
						word = temp;
						skip = true;
						break;
					}
				}

				if (!skip) {
					if (RVtemp.endsWith(STEP0_SUFFIXES_PRECEDING3) && temp.length() > STEP0_SUFFIXES_PRECEDING3.length()
							&& temp.charAt(temp.length() - STEP0_SUFFIXES_PRECEDING3.length() - 1) == 'u') {
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

		for (String suffix : STEP1_SUFFIXES1) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES2) {
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

		for (String suffix : STEP1_SUFFIXES3) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + "log";
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES4) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length()) + "u";
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES5) {
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

			for (String suffix : STEP1_SUFFIXES6_1) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					break;
				}
			}
			return word;
		} else if (R2String.endsWith("mente")) {
			word = removeEnding(word, 5);
			R2String = getRegionSubstring(word, R2);

			for (String suffix : STEP1_SUFFIXES7_1) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					break;
				}
			}
			return word;
		}

		for (String suffix : STEP1_SUFFIXES8) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());
					for (String suffix_2 : STEP1_SUFFIXES8_2) {
						if (R2String.endsWith(suffix_2)) {
							word = removeEnding(word, suffix_2.length());
							break;
						}
					}
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES9) {
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

	private String STEP2A(String word) {
		String RVString = getRegionSubstring(word, RV);
		for (String suffix : STEP2A_SUFFIXES1) {
			if (RVString.endsWith(suffix)) {
				if (word.length() > suffix.length() && word.charAt(word.length() - suffix.length() - 1) == 'u') {
					word = removeEnding(word, suffix.length());
				}
				break;
			}
		}
		return word;
	}

	private String STEP2B(String word) {
		String RVString = getRegionSubstring(word, RV);

		for (String suffix : STEP2B_SUFFIXES1) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				return word;
			}
		}

		for (String suffix : STEP2B_SUFFIXES2) {
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

		for (String suffix : STEP3_SUFFIXES1) {
			if (RVString.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				return word;
			}
		}

		for (String suffix : STEP3_SUFFIXES2) {
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
	public Language getLanguage() {
		return Language.SPANISH;
	}
}
