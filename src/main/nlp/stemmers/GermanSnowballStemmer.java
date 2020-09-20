package main.nlp.stemmers;/*
 * Snowball stemmer
 * 
 * For Standard German
 * 
 * https://snowballstem.org/algorithms/german/stemmer.html
 * 
 */
public class GermanSnowballStemmer extends Stemmer {
	protected static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'y', 'ä', 'ö', 'ü' };
	private static final char[] VALID_S_ENDING = { 'b', 'd', 'f', 'g', 'h', 'k', 'l', 'm', 'n', 'r', 't' };
	private static final char[] VALID_ST_ENDING = { 'b', 'd', 'f', 'g', 'h', 'k', 'l', 'm', 'n', 't' };
	private static final String[] step1_suffixes1 = { "ern", "er", "em" };
	private static final String[] step1_suffixes2 = { "en", "es", "e" };
	private static final String[] step1_suffixes3 = { "s" };
	private static final String[] step2_suffixes1 = { "est", "en", "er" };
	private static final String[] step2_suffixes2 = { "st" };
	private static final String[] step3_suffixes1 = { "end", "ung" };
	private static final String[] step3_suffixes1_1 = { "ig" };
	private static final String[] step3_suffixes2 = { "isch", "ig", "ik" };
	private static final String[] step3_suffixes3 = { "lich", "heit" };
	private static final String[] step3_suffixes3_1 = { "er", "en" };
	private static final String[] step3_suffixes4 = { "keit" };
	private static final String[] step3_suffixes4_1 = { "lich", "ig" };

	private int R1;
	private int R2;

	protected boolean isVowel(char ch) {
		for (char vowel : VOWELS) {
			if (ch == vowel) {
				return true;
			}
		}
		return false;
	}

	private boolean isValidSEnding(char ch) {
		for (char sEnding : VALID_S_ENDING) {
			if (ch == sEnding) {
				return true;
			}
		}
		return false;
	}

	private boolean isValidSTEnding(char ch) {
		for (char stEnding : VALID_ST_ENDING) {
			if (ch == stEnding) {
				return true;
			}
		}
		return false;
	}

	private String rewrite(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		boolean prevCharIsVowel = false;

		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (ch == 'ß') {
				sb.append("ss");
			} else if ((ch == 'u' || ch == 'y') && prevCharIsVowel && i + 1 < word.length()
					&& isVowel(word.charAt(i + 1))) {
				sb.append(Character.toUpperCase(ch));
			} else {
				sb.append(ch);
				prevCharIsVowel = isVowel(ch);
			}
		}
		return sb.toString();
	}

	private void markRNumberRegions(String word) {
		R1 = word.length();
		R2 = R1;

		for (int i = 0; i < word.length() - 1; i++) {
			if (isVowel(word.charAt(i)) && !isVowel(word.charAt(i + 1))) {
				R1 = i + 2;
				break;
			}
		}
		if (R1 < 3) {
			R1 = 3;
		}

		for (int i = R1 - 1; i < word.length() - 1; i++) {
			if (isVowel(word.charAt(i)) && !isVowel(word.charAt(i + 1))) {
				R2 = i + 2;
				break;
			}
		}
	}
	
	private String step1(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : step1_suffixes1) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes2) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					if (word.endsWith("niss")) { // might need to be R1String
						word = removeEnding(word, 1);
					}
				}
				return word;
			}
		}

		for (String suffix : step1_suffixes3) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix) && word.length() > 1 && isValidSEnding(word.charAt(word.length() - 2))) {
					word = removeEnding(word, suffix.length());
				}
				break;
			}
		}
		return word;
	}

	private String step2(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : step2_suffixes1) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}

		for (String suffix : step2_suffixes2) {
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

	private String step3(String word) {
		String R2String = getRegionSubstring(word, R2);

		for (String suffix : step3_suffixes1) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());
					for (String precedingSuffix : step3_suffixes1_1) {
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

		for (String suffix : step3_suffixes2) {
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

		for (String suffix : step3_suffixes3) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					String R1String = getRegionSubstring(word, R1);
					for (String precedingSuffix : step3_suffixes3_1) {
						if (word.endsWith(precedingSuffix)) {
							if (R1String.endsWith(precedingSuffix)) {
								word = removeEnding(word, precedingSuffix.length());
							}
							break;
						}
					}
				}
				return word;
			}
		}

		for (String suffix : step3_suffixes4) {
			if (word.endsWith(suffix)) {
				if (R2String.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					R2String = removeEnding(R2String, suffix.length());
					for (String precedingSuffix : step3_suffixes4_1) {
						if (word.endsWith(precedingSuffix)) {
							if (R2String.endsWith(precedingSuffix)) {
								word = removeEnding(word, precedingSuffix.length());
							}
							break;
						}
					}
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
	public String stem(String word) {
		word = normalize(word);
		word = rewrite(word);
		markRNumberRegions(word);
		word = step1(word);
		word = step2(word);
		word = step3(word);
		return finalize(word);
	}
}
