package main.nlp.stemmers;

/**
 * 
 * Snowball stemmer for standard German
 * 
 * Source: https://snowballstem.org/algorithms/german/stemmer.html
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan
 * @version 1.0
 */
public class GermanSnowballStemmer extends Stemmer {
	protected static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'y', 'ä', 'ö', 'ü' };
	private static final char[] VALID_S_ENDING = { 'b', 'd', 'f', 'g', 'h', 'k', 'l', 'm', 'n', 'r', 't' };
	private static final char[] VALID_ST_ENDING = { 'b', 'd', 'f', 'g', 'h', 'k', 'l', 'm', 'n', 't' };

	private static final String[] STEP1_SUFFIXES1 = { "ern", "er", "em" };
	private static final String[] STEP1_SUFFIXES2 = { "en", "es", "e" };
	private static final String[] STEP1_SUFFIXES3 = { "s" };
	private static final String[] STEP2_SUFFIXES1 = { "est", "en", "er" };
	private static final String[] STEP2_SUFFIXES2 = { "st" };
	private static final String[] STEP3_SUFFIXES1 = { "end", "ung" };
	private static final String[] STEP3_SUFFIXES1_1 = { "ig" };
	private static final String[] STEP3_SUFFIXES2 = { "isch", "ig", "ik" };
	private static final String[] STEP3_SUFFIXES3 = { "lich", "heit" };
	private static final String[] STEP3_SUFFIXES3_1 = { "er", "en" };
	private static final String[] STEP3_SUFFIXES4 = { "keit" };
	private static final String[] STEP3_SUFFIXES4_1 = { "lich", "ig" };

	private int R1;
	private int R2;

	@Override
	public String stem(String word) {
		word = normalize(word);
		word = rewrite(word);
		markRNumberRegions(word);
		word = step1(word);
		word = step2(word);
		word = step3(word);
		return finalize(word);
	}

	private String rewrite(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (ch == 'ß') {
				sb.append("ss");
			} else if ((ch == 'u' || ch == 'y') && i > 0 && isVowel(word.charAt(i - 1), VOWELS) && i + 1 < word.length()
					&& isVowel(word.charAt(i + 1), VOWELS)) {
				sb.append(Character.toUpperCase(ch));
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private void markRNumberRegions(String word) {
		R1 = calcR1VC(word, VOWELS);
		R1 = R1 < 3 ? 3 : R1;
		R2 = calcR2VC(word, R1, VOWELS);
	}

	private String step1(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : STEP1_SUFFIXES1) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES2) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					if (word.endsWith("niss")) {
						word = removeEnding(word, 1);
					}
				}
				return word;
			}
		}

		for (String suffix : STEP1_SUFFIXES3) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix) && word.length() > 1 && isValidSEnding(word.charAt(word.length() - 2))) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}
		return word;
	}

	private boolean isValidSEnding(char ch) {
		return contains(ch, VALID_S_ENDING);
	}

	private String step2(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : STEP2_SUFFIXES1) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		for (String suffix : STEP2_SUFFIXES2) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix) && word.length() > 5
						&& isValidSTEnding(word.charAt(word.length() - suffix.length() - 1))) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}
		return word;
	}

	private boolean isValidSTEnding(char ch) {
		return contains(ch, VALID_ST_ENDING);
	}

	private String step3(String word) {
		String R2String = getRegionSubstring(word, R2);

		for (String suffix : STEP3_SUFFIXES1) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());
					for (String precedingSuffix : STEP3_SUFFIXES1_1) {
						if (R2String.endsWith(precedingSuffix)) {
							if (word.length() > precedingSuffix.length() + 1) {
								if (word.charAt(word.length() - precedingSuffix.length() - 1) != 'e') {
									word = removeEnding(word, precedingSuffix.length());
								}
							} else {
								word = removeEnding(word, precedingSuffix.length());
							}
						}
					}
				}
				return word;
			}
		}

		for (String suffix : STEP3_SUFFIXES2) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					if (word.length() > suffix.length() + 1) {
						if (word.charAt(word.length() - suffix.length() - 1) != 'e') {
							word = removeEnding(word, suffix.length());
						}
					} else {
						word = removeEnding(word, suffix.length());
					}
				}
				return word;
			}
		}

		for (String suffix : STEP3_SUFFIXES3) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					String R1String = getRegionSubstring(word, R1);
					for (String precedingSuffix : STEP3_SUFFIXES3_1) {
						if (word.endsWith(precedingSuffix)) {
							if (R1String.endsWith(precedingSuffix)) {
								word = removeEnding(word, precedingSuffix.length());
							}
							return word;
						}
					}
				}
				return word;
			}
		}

		for (String suffix : STEP3_SUFFIXES4) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());
					for (String precedingSuffix : STEP3_SUFFIXES4_1) {
						if (word.endsWith(precedingSuffix)) {
							if (R2String.endsWith(precedingSuffix)) {
								word = removeEnding(word, precedingSuffix.length());
							}
							return word;
						}
					}
				}
				return word;
			}
		}
		return word;
	}

	private String finalize(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (ch == 'U' || ch == 'Y') {
				sb.append(Character.toLowerCase(ch));
			} else if (ch == 'ä') {
				sb.append('a');
			} else if (ch == 'ö') {
				sb.append('o');
			} else if (ch == 'ü') {
				sb.append('u');
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	@Override
	public Language getLanguage() {
		return Language.GERMAN;
	}
}
