package main.nlp.stemmers;

/**
 * 
 * Snowball stemmer for standard Dutch
 * 
 * Source: https://snowballstem.org/algorithms/dutch/stemmer.html
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class DutchSnowballStemmer extends Stemmer {
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'y', 'è' };
	private static final char[] SPECIAL_UNDOUBLE_VOWELS = { 'a', 'e', 'o', 'u' };

	private static final String STEP1_SUFFIXES1 = "heden";
	private static final String STEP1_SUFFIXES1_REPLACEMENT = "heid";
	private static final String[] STEP1_SUFFIXES2 = { "ene", "en" };
	private static final String[] STEP1_SUFFIXES3 = { "se", "s" };
	private static final String STEP2_SUFFIXES1 = "e";
	private static final String STEP3A_SUFFIXES1 = "heid";
	private static final char STEP3A_SUFFIXES1_NOT_PRECEDING = 'c';
	private static final String STEP3A_SUFFIXES1_PRECEDING = "en";
	private static final String STEP3B_SUFFIXES1 = "baar";
	private static final String STEP3B_SUFFIXES2 = "bar";
	private static final String STEP3B_SUFFIXES3 = "lijk";
	private static final String STEP3B_SUFFIXES4 = "end";
	private static final String STEP3B_SUFFIXES5 = "ing";
	private static final String STEP3B_SUFFIXES6 = "ig";
	private static final char STEP3B_SUFFIXES_SPECIALCASE = 'e';
	private static final String STEP3B_SUFFIXES_PRECEDING = "ig";
	private static final char STEP4_SUFFIXES_SPECIALCASE = 'I';

	private int R1;
	private int R2;
	private boolean removedEFlag;

	@Override
	public String stem(String word) {
		removedEFlag = false;
		word = normalize(word);
		word = replace(word);
		markRegions(word);
		word = step1(word);
		word = step2(word);
		word = step3a(word);
		word = step3b(word);
		word = step4(word);
		return finalize(word);
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
			} else if (ch == 'y') {
				if (prevIsVowel || i == 0) {
					ch = Character.toUpperCase(ch);
				}
			} else if (ch == 'i' && prevIsVowel && i + 1 < word.length() && isVowel(word.charAt(i + 1), VOWELS)) {
				ch = Character.toUpperCase(ch);
			}

			sb.append(ch);
			prevIsVowel = isVowel(ch, VOWELS);
		}
		return sb.toString();
	}

	private void markRegions(String word) {
		R1 = calcR1VC(word, VOWELS);
		R1 = R1 < 3 ? 3 : R1;
		R2 = calcR2VC(word, R1, VOWELS);
	}

	private String step1(String word) {
		String R1String = getRegionSubstring(word, R1);
		if (word.endsWith(STEP1_SUFFIXES1)) {
			if (R1String.endsWith(STEP1_SUFFIXES1)) {
				word = removeEnding(word, STEP1_SUFFIXES1.length()) + STEP1_SUFFIXES1_REPLACEMENT;
			}
			return word;
		}

		for (String suffix : STEP1_SUFFIXES2) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					String temp = removeEnding(word, suffix.length());
					if (isEnEnding(temp)) {
						word = undouble(temp);
					}
				}
				return word;
			}
		}
		for (String suffix : STEP1_SUFFIXES3) {
			if (word.endsWith(suffix)) {
				if (R1String.endsWith(suffix)) {
					String temp = removeEnding(word, suffix.length());
					if (isSEnding(temp)) {
						word = temp;
					}
				}
				break;
			}
		}
		return word;
	}

	private boolean isSEnding(String word) {
		char lastLetter = word.charAt(word.length() - 1);
		return isConsonant(lastLetter, VOWELS) && lastLetter != 'j';
	}

	private boolean isEnEnding(String word) {
		char lastLetter = word.charAt(word.length() - 1);
		return isConsonant(lastLetter, VOWELS) && !word.endsWith("gem");
	}

	private String undouble(String word) {
		if (word.endsWith("kk") || word.endsWith("dd") || word.endsWith("tt")) {
			word = removeEnding(word, 1);
		}
		return word;
	}

	private String step2(String word) {
		String R1String = getRegionSubstring(word, R1);
		if (R1String.endsWith(STEP2_SUFFIXES1) && !isVowel(word.charAt(word.length() - 2), VOWELS)) {
			removedEFlag = true;
			word = undouble(removeEnding(word, 1));
		}
		return word;
	}

	private String step3a(String word) {
		String R2String = getRegionSubstring(word, R2);
		if (R2String.endsWith(STEP3A_SUFFIXES1) && word.length() > 4
				&& word.charAt(word.length() - 5) != STEP3A_SUFFIXES1_NOT_PRECEDING) {
			word = removeEnding(word, 4);
			String R1String = getRegionSubstring(word, R1);
			if (R1String.endsWith(STEP3A_SUFFIXES1_PRECEDING)) {
				String temp = removeEnding(word, 2);
				if (isEnEnding(temp)) {
					word = undouble(temp);
				}
			}
		}
		return word;
	}

	private String step3b(String word) {
		String R2String = getRegionSubstring(word, R2);

		if (word.endsWith(STEP3B_SUFFIXES1)) {
			if (R2String.endsWith(STEP3B_SUFFIXES1)) {
				word = removeEnding(word, 4);
			}
		} else if (word.endsWith(STEP3B_SUFFIXES2)) {
			if (R2String.endsWith(STEP3B_SUFFIXES2) && removedEFlag) {
				word = removeEnding(word, 3);
			}
		} else if (word.endsWith(STEP3B_SUFFIXES3)) {
			if (R2String.endsWith(STEP3B_SUFFIXES3)) {
				word = removeEnding(word, 4);
				return step2(word);
			}
		} else if (word.endsWith(STEP3B_SUFFIXES4)) {
			if (R2String.endsWith(STEP3B_SUFFIXES4)) {
				word = removeEnding(word, 3);
				R2String = removeEnding(R2String, 3);
				if (R2String.endsWith(STEP3B_SUFFIXES_PRECEDING) && word.length() > 2
						&& word.charAt(word.length() - 3) != STEP3B_SUFFIXES_SPECIALCASE) {
					word = removeEnding(word, 2);
				} else {
					word = undouble(word);
				}
			}
		} else if (word.endsWith(STEP3B_SUFFIXES5)) {
			if (R2String.endsWith(STEP3B_SUFFIXES5)) {
				word = removeEnding(word, 3);
				R2String = removeEnding(R2String, 3);
				if (R2String.endsWith(STEP3B_SUFFIXES_PRECEDING) && word.length() > 2
						&& word.charAt(word.length() - 3) != STEP3B_SUFFIXES_SPECIALCASE) {
					word = removeEnding(word, 2);
				} else {
					word = undouble(word);
				}
			}
		} else if (word.endsWith(STEP3B_SUFFIXES6)) {
			if (R2String.endsWith(STEP3B_SUFFIXES6) && word.length() > 2
					&& word.charAt(word.length() - 3) != STEP3B_SUFFIXES_SPECIALCASE) {
				word = removeEnding(word, 2);
			}
		}
		return word;
	}

	private String step4(String word) {
		if (word.length() > 3) {
			char lastLetter = word.charAt(word.length() - 1);
			char secondLastLetter = word.charAt(word.length() - 2);
			char thirdLastLetter = word.charAt(word.length() - 3);
			char fourthLastLetter = word.charAt(word.length() - 4);
			if (isConsonant(fourthLastLetter, VOWELS) && isSpecialUndoubleVowel(thirdLastLetter)
					&& secondLastLetter == thirdLastLetter && isConsonant(lastLetter, VOWELS)
					&& lastLetter != STEP4_SUFFIXES_SPECIALCASE) {
				word = removeEnding(word, 2) + lastLetter;
			}
		}
		return word;
	}

	private boolean isSpecialUndoubleVowel(char ch) {
		return isVowel(ch, SPECIAL_UNDOUBLE_VOWELS);
	}

	private String finalize(String word) {
		return word.toLowerCase();
	}

	@Override
	public Language getLanguage() {
		return Language.DUTCH;
	}
}
