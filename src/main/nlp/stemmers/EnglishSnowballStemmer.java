package main.nlp.stemmers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * Porter2 stemmer for standard English
 * 
 * Source: http://snowball.tartarus.org/algorithms/english/stemmer.html
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan
 * @version 1.0
 */
public class EnglishSnowballStemmer extends Stemmer {
	private static final int MIN_LENGTH = 3;
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'y' };
	private static final String[] DOUBLE_CONSONANTS = { "bb", "dd", "ff", "gg", "mm", "nn", "pp", "rr", "tt" };
	private static final char Y_CONSONANT = 'Y';
	private static final char[] LI_ENDINGS = { 'c', 'd', 'e', 'g', 'h', 'k', 'm', 'n', 'r', 't' };

	private static final String[] STEP0_SUFFIXES = { "\'s\'", "\'s", "\'" };
	private static final String[] STEP1B_R1_SUFFIXES = { "eedly", "eed" };
	private static final String[] STEP1B_SUFFIXES = { "ingly", "edly", "ing", "ed" };
	private static final List<String> STEP1B_SUFFIXES_LIST = Arrays.asList(STEP1B_SUFFIXES);
	private static final String[] STEP1B_OLD_ENGLISH_SUFFIXES = { "est", "eth" };
	private static final String[] STEP1B_SPECIALCASE_SUFFIXES = { "at", "bl", "iz" };

	private static final String[][] STEP2_MAPPINGS = { { "ization", "ize" }, { "iveness", "ive" }, { "fulness", "ful" },
			{ "ational", "ate" }, { "ousness", "ous" }, { "biliti", "ble" }, { "tional", "tion" }, { "lessli", "less" },
			{ "fulli", "ful" }, { "entli", "ent" }, { "ation", "ate" }, { "aliti", "al" }, { "iviti", "ive" },
			{ "ousli", "ous" }, { "alism", "al" }, { "abli", "able" }, { "anci", "ance" }, { "alli", "al" },
			{ "izer", "ize" }, { "enci", "ence" }, { "ator", "ate" }, { "bli", "ble" } };

	private static final String[][] STEP3_MAPPINGS = { { "ational", "ate" }, { "tional", "tion" }, { "alize", "al" },
			{ "icate", "ic" }, { "iciti", "ic" }, { "ical", "ic" }, { "ness", EMPTY }, { "ful", EMPTY } };

	private static final String[] STEP4_SUFFIXES = { "ement", "ment", "ance", "ence", "able", "ible", "ant", "ent",
			"ism", "ate", "iti", "ous", "ive", "ize", "al", "er", "ic" };

	private Map<String, String> exceptions1;
	private Set<String> exceptions2;
	private int R1, R2;
	private boolean oldEnglishFlag;

	public EnglishSnowballStemmer() {
		this(false);
	}

	public EnglishSnowballStemmer(boolean oldEnglishIncluded) {
		oldEnglishFlag = oldEnglishIncluded;
		exceptions1 = new HashMap<>();
		exceptions2 = new HashSet<>();
		initExceptions1();
		initExceptions2();
	}

	private void initExceptions1() {
		exceptions1.put("skis", "ski");
		exceptions1.put("skies", "sky");
		exceptions1.put("dying", "die");
		exceptions1.put("lying", "lie");
		exceptions1.put("tying", "tie");

		exceptions1.put("idly", "idl");
		exceptions1.put("gently", "gentl");
		exceptions1.put("ugly", "ugli");
		exceptions1.put("early", "earli");
		exceptions1.put("only", "onli");
		exceptions1.put("singly", "singl");

		exceptions1.put("sky", "sky");
		exceptions1.put("news", "news");
		exceptions1.put("howe", "howe");

		exceptions1.put("atlas", "atlas");
		exceptions1.put("cosmos", "cosmos");
		exceptions1.put("bias", "bias");
		exceptions1.put("andes", "andes");
	}

	private void initExceptions2() {
		exceptions2.add("inning");
		exceptions2.add("outing");
		exceptions2.add("canning");
		exceptions2.add("herring");
		exceptions2.add("earring");
		exceptions2.add("proceed");
		exceptions2.add("exceed");
		exceptions2.add("succeed");
	}

	@Override
	public String stem(String word) {
		word = normalize(word);

		if (word.length() < MIN_LENGTH) {
			return word;
		}
		if (exceptions1.containsKey(word)) {
			return exceptions1.get(word);
		}

		word = prepare(word);
		word = step0(word);
		word = step1a(word);
		word = step1b(word);
		word = step1c(word);
		word = step2(word);
		word = step3(word);
		word = step4(word);
		word = step5(word);
		return finalize(word);
	}

	private String prepare(String word) {
		if (word.charAt(0) == '\'') {
			word = word.substring(1);
		}
		word = markYs(word);
		markRegions(word);
		return word;
	}

	private String markYs(String word) {
		final int length = word.length();
		StringBuilder sb = new StringBuilder(length);
		char ch = word.charAt(0);
		if (ch == 'y') {
			sb.append(Y_CONSONANT);
		} else {
			sb.append(ch);
		}

		for (int i = 1; i < length; i++) {
			ch = word.charAt(i);
			if (ch == 'y' && isVowel(sb.charAt(i - 1), VOWELS)) {
				sb.append(Y_CONSONANT);
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private void markRegions(String word) {
		R1 = word.length();
		R2 = R1;

		if (word.startsWith("gener") || word.startsWith("arsen")) {
			R1 = 5;
		}
		if (word.startsWith("commun")) {
			R1 = 6;
		}

		R1 = R1 == R2 ? calcR1VC(word, VOWELS) : R1;
		R2 = calcR2VC(word, R1, VOWELS);
	}

	private String step0(String word) {
		for (String suffix : STEP0_SUFFIXES) {
			if (word.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				break;
			}
		}
		return word;
	}

	private boolean containsVowel(String word) {
		for (int i = 0; i < word.length(); i++) {
			if (isVowel(word.charAt(i), VOWELS)) {
				return true;
			}
		}
		return false;
	}

	private boolean isDoubleCons(String twoChars) {
		if (twoChars.length() != 2) {
			return false;
		}
		for (String doubleCons : DOUBLE_CONSONANTS) {
			if (twoChars.equals(doubleCons)) {
				return true;
			}
		}
		return false;
	}

	private String step1a(String word) {
		if (word.endsWith("sses")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("ied") || word.endsWith("ies")) {
			word = removeEnding(word, 3);
			if (word.length() > 1) {
				word += "i";
			} else {
				word += "ie";
			}
		} else if (word.endsWith("us") || word.endsWith("ss")) { // do nothing
		} else if (word.endsWith("s") && (word.length() > 1 && containsVowel(word.substring(0, word.length() - 2)))) {
			word = removeEnding(word, 1);
		}
		return word;
	}

	private String step1b(String word) {
		if (exceptions2.contains(word)) {
			return word;
		}

		String R1Str = getRegionSubstring(word, R1);
		for (String R1_suffix : STEP1B_R1_SUFFIXES) {
			if (word.endsWith(R1_suffix)) {
				if (R1Str.endsWith(R1_suffix)) {
					word = removeEnding(word, R1_suffix.length());
					word += "ee";
				}
				return word;
			}
		}

		if (oldEnglishFlag) {
			STEP1B_SUFFIXES_LIST.addAll(Arrays.asList(STEP1B_OLD_ENGLISH_SUFFIXES));
			oldEnglishFlag = false;
		}
		for (String suffix : STEP1B_SUFFIXES_LIST) {
			if (word.endsWith(suffix) && containsVowel(word.substring(0, word.length() - suffix.length()))) {
				word = removeEnding(word, suffix.length());
				for (String secondEnding : STEP1B_SPECIALCASE_SUFFIXES) {
					if (word.endsWith(secondEnding)) {
						return word + "e";
					}
				}

				if (word.length() >= 2 && this.isDoubleCons(word.substring(word.length() - 2))) {
					word = removeEnding(word, 1);
				} else if (isShort(word)) {
					word += "e";
				}
				return word;
			}
		}

		return word;
	}

	private String step1c(String word) {
		if ((word.endsWith("y") || word.endsWith("Y")) && word.length() >= 3
				&& isConsonant(word.charAt(word.length() - 2), VOWELS)) {
			word = removeEnding(word, 1);
			word += "i";
		}
		return word;
	}

	private boolean isValidLIending(char endingChar) {
		for (char ending : LI_ENDINGS) {
			if (endingChar == ending) {
				return true;
			}
		}
		return false;
	}

	private boolean isShortSyllable(String word) {
		return isShortSyllableCaseA(word) || isShortSyllableCaseB(word);
	}

	private boolean isShortSyllableCaseA(String word) {
		if (word.length() != 2) {
			return false;
		}
		return isVowel(word.charAt(0), VOWELS) && isConsonant(word.charAt(1), VOWELS);
	}

	private boolean isShortSyllableCaseB(String word) {
		final int length = word.length();
		if (length < 3) {
			return false;
		}

		int thirdCharIndex = word.length() - 1;
		char thirdCh = word.charAt(thirdCharIndex);
		char secondCh = word.charAt(thirdCharIndex - 1);
		char firstCh = word.charAt(thirdCharIndex - 2);
		return isConsonant(firstCh, VOWELS) && isVowel(secondCh, VOWELS) && isConsonant(thirdCh, VOWELS)
				&& !(thirdCh == 'w' || thirdCh == 'x' || thirdCh == Y_CONSONANT);
	}

	private boolean isShort(String word) {
		return isShortSyllable(word) && R1 == word.length();
	}

	private String step2(String word) {
		String R1Sub = getRegionSubstring(word, R1);

		for (String[] mapping : STEP2_MAPPINGS) {
			String suffix = mapping[0];
			if (word.endsWith(suffix)) {
				if (R1Sub.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
					word += mapping[1];
				}
				return word;
			}
		}

		if (R1Sub.endsWith("ogi")) {
			String temp = removeEnding(word, 3);
			if (temp.endsWith("l")) {
				word = temp + "og";
			}
		} else if (R1Sub.endsWith("li") && word.length() >= 3 && isValidLIending(word.charAt(word.length() - 3))) {
			word = removeEnding(word, 2);
		}
		return word;
	}

	private String step3(String word) {
		String R1Sub = getRegionSubstring(word, R1);
		for (String[] mapping : STEP3_MAPPINGS) {
			String suffix = mapping[0];
			if (R1Sub.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				word += mapping[1];
				return word;
			}
		}

		if (getRegionSubstring(word, R2).endsWith("ative")) {
			word = removeEnding(word, 5);
		}

		return word;
	}

	private String step4(String word) {
		String R2Sub = getRegionSubstring(word, R2);
		for (String suffix : STEP4_SUFFIXES) {
			if (word.endsWith(suffix)) {
				if (R2Sub.endsWith(suffix)) {
					word = removeEnding(word, suffix.length());
				}
				return word;
			}
		}
		if (R2Sub.endsWith("ion")) {
			String temp = removeEnding(word, 3);
			if (temp.endsWith("s") || temp.endsWith("t")) {
				word = temp;
			}
		}
		return word;
	}

	private String step5(String word) {
		String R2Sub = getRegionSubstring(word, R2);

		if (R2Sub.endsWith("e") || (getRegionSubstring(word, R1).endsWith("e")
				&& !isShortSyllable(word.substring(0, word.length() - 1)))) {
			word = removeEnding(word, 1);
		} else if (R2Sub.endsWith("l")) {
			if (word.charAt(word.length() - 2) == 'l') {
				word = removeEnding(word, 1);
			}
		}
		return word;
	}

	private String finalize(String word) {
		return word.toLowerCase();
	}

	@Override
	public Language getLanguage() {
		return Language.ENGLISH;
	}
}
