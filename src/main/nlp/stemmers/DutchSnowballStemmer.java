package main.nlp.stemmers;
/*
 * Snowball stemmer
 * 
 * For Standard Dutch
 * 
 * https://snowballstem.org/algorithms/dutch/stemmer.html
 * 
 */
public class DutchSnowballStemmer extends Stemmer {
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'y', 'è' };
	private static final String step1_suffixes1 = "heden";
	private static final String step1_suffixes1_replacement = "heid";
	private static final String[] step1_suffixes2 = { "ene", "en" };
	private static final String[] step1_suffixes3 = { "se", "s" };
	private static final String step2_suffixes1 = "e";
	private static final String step3a_suffixes1 = "heid";
	private static final char step3a_suffixes1_not_preceding = 'c';
	private static final String step3a_suffixes1_preceding = "en";
	private static final String step3b_suffixes1 = "baar";
	private static final String step3b_suffixes2 = "bar";
	private static final String step3b_suffixes3 = "lijk";
	private static final String step3b_suffixes4 = "end";
	private static final String step3b_suffixes5 = "ing";
	private static final String step3b_suffixes6 = "ig";
	private static final char step3b_suffixes_specialCase = 'e';
	private static final String step3b_suffixes_preceding = "ig";
	private static final char step4_suffixes_specialCase = 'I';

	private int R1;
	private int R2;
	private boolean removedEFlag;

	private boolean isVowel(char ch) {
		for (char vowel : VOWELS) {
			if (ch == vowel) {
				return true;
			}
		}
		return false;
	}

	private boolean isValidSEnding(String word) {
		char last = word.charAt(word.length() - 1);
		return !isVowel(last) && last != 'j';
	}

	private boolean isValidEnEnding(String word) {
		char last = word.charAt(word.length() - 1);
		return !isVowel(last) && !word.endsWith("gem");
	}

	private String undouble(String word) {
		if (word.endsWith("kk") || word.endsWith("dd") || word.endsWith("tt")) {
			word = removeEnding(word, 1);
		}
		return word;
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

	private String replace(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		char ch;
		boolean prevIsVowel = false;
		for (int i = 0; i < word.length(); i++) {
			ch = word.charAt(i);
			if (ch == 'ä' || ch == 'á') {
				ch = 'a';
			} else if (ch == 'ë' || ch == 'é') {
				ch = 'e';
			} else if (ch == 'ï' || ch == 'í') {
				ch = 'i';
			} else if (ch == 'ö' || ch == 'ó') {
				ch = 'o';
			} else if (ch == 'ü' || ch == 'ú') {
				ch = 'u';
			}

			if (ch == 'y') {
				if (prevIsVowel || i == 0) {
					ch = Character.toUpperCase(ch);
				}
			} else if (ch == 'i' && prevIsVowel && i + 1 < word.length() && isVowel(word.charAt(i + 1))) {
				ch = Character.toUpperCase(ch);
			}

			sb.append(ch);
			prevIsVowel = isVowel(ch);
		}
		return sb.toString();
	}

	private String step1(String word) {
		String R1String = getRegionSubstring(word, R1);
		if (word.endsWith(step1_suffixes1)) {
			if (R1String.endsWith(step1_suffixes1)) {
				word = removeEnding(word, step1_suffixes1.length()) + step1_suffixes1_replacement;
			}
			return word;
		}

		for (String suffix : step1_suffixes2) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					String temp = removeEnding(word, suffix.length());
					if (isValidEnEnding(temp)) {
						word = undouble(temp);
					}
				}
				return word;
			}
		}
		for (String suffix : step1_suffixes3) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					String temp = removeEnding(word, suffix.length());
					if (isValidSEnding(temp)) {
						word = temp;
					}
				}
				break;
			}
		}
		return word;
	}

	private String step2(String word) {
		String R1String = getRegionSubstring(word, R1);
		if (R1String.endsWith(step2_suffixes1) && !isVowel(word.charAt(word.length() - 2))) {
			removedEFlag = true;
			word = undouble(removeEnding(word, 1));
		}
		return word;
	}

	private String step3a(String word) {
		String R2String = getRegionSubstring(word, R2);
		if (R2String.endsWith(step3a_suffixes1) && word.length() > 4
				&& word.charAt(word.length() - 5) != step3a_suffixes1_not_preceding) {
			word = removeEnding(word, 4);
			String R1String = getRegionSubstring(word, R1);
			if (R1String.endsWith(step3a_suffixes1_preceding)) {
				String temp = removeEnding(word, 2);
				if (isValidEnEnding(temp)) {
					word = undouble(temp);
				}
			}
		}
		return word;
	}

	private String step3b(String word) {
		String R2String = getRegionSubstring(word, R2);

		if (word.endsWith(step3b_suffixes1)) {
			if (R2String.endsWith(step3b_suffixes1)) {
				word = removeEnding(word, 4);
			}
		} else if (word.endsWith(step3b_suffixes2)) {
			if (R2String.endsWith(step3b_suffixes2) && removedEFlag) {
				word = removeEnding(word, 3);
			}
		} else if (word.endsWith(step3b_suffixes3)) {
			if (R2String.endsWith(step3b_suffixes3)) {
				word = removeEnding(word, 4);
				return step2(word);
			}
		} else if (word.endsWith(step3b_suffixes4)) {
			if (R2String.endsWith(step3b_suffixes4)) {
				word = removeEnding(word, 3);
				R2String = removeEnding(R2String, 3);
				if (R2String.endsWith(step3b_suffixes_preceding) && word.length() > 2
						&& word.charAt(word.length() - 3) != step3b_suffixes_specialCase) {
					word = removeEnding(word, 2);
				} else {
					word = undouble(word);
				}
			}
		} else if (word.endsWith(step3b_suffixes5)) {
			if (R2String.endsWith(step3b_suffixes5)) {
				word = removeEnding(word, 3);
				R2String = removeEnding(R2String, 3);
				if (R2String.endsWith(step3b_suffixes_preceding) && word.length() > 2
						&& word.charAt(word.length() - 3) != step3b_suffixes_specialCase) {
					word = removeEnding(word, 2);
				} else {
					word = undouble(word);
				}
			}
		} else if (word.endsWith(step3b_suffixes6)) {
			if (R2String.endsWith(step3b_suffixes6) && word.length() > 2
					&& word.charAt(word.length() - 3) != step3b_suffixes_specialCase) {
				word = removeEnding(word, 2);
			}
		}
		return word;
	}

	private boolean isSpecialUndoubleVowel(char ch) {
		return ch == 'a' || ch == 'e' || ch == 'o' || ch == 'u';
	}

	private String step4(String word) {
		if (word.length() > 3) {
			char last = word.charAt(word.length() - 1);
			char secondLast = word.charAt(word.length() - 2);
			char thirdLast = word.charAt(word.length() - 3);
			char fourthLast = word.charAt(word.length() - 4);
			if (!isVowel(fourthLast) && isSpecialUndoubleVowel(thirdLast) && secondLast == thirdLast && !isVowel(last)
					&& last != step4_suffixes_specialCase) {
				word = removeEnding(word, 2) + last; 
			}
		}
		return word;
	}

	private String finalize(String word) {
		return word.toLowerCase();
	}

	@Override
	public String stem(String word) {
		removedEFlag = false;
		word = normalize(word);
		word = replace(word);
		markRNumberRegions(word);
		word = step1(word);
		word = step2(word);
		word = step3a(word);
		word = step3b(word);
		word = step4(word);
		return finalize(word);
	}

}
