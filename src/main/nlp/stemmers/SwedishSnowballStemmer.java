package main.nlp.stemmers;

/**
 * 
 * Snowball stemmer for standard Swedish
 * 
 * Source: https://snowballstem.org/algorithms/swedish/stemmer.html
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan
 * @version 1.0
 */
public class SwedishSnowballStemmer extends Stemmer {
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'y', 'ä', 'å', 'ö' };
	private static final char[] VALID_S_ENDING = { 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'r',
			't', 'v', 'y' };

	private static final String[] STEP1_SUFFIXES1 = { "heterna", "hetens", "anden", "heten", "heter", "arnas", "ernas",
			"ornas", "andes", "arens", "andet", "arna", "erna", "orna", "ande", "arne", "aste", "aren", "ades", "erns",
			"ade", "are", "ern", "ens", "het", "ast", "ad", "en", "ar", "er", "or", "as", "es", "at", "a", "e" };
	private static final String STEP1_SUFFIXES2 = "s";
	private static final String[] STEP2_SUFFIXES1 = { "dd", "gd", "nn", "dt", "gt", "kt", "tt" };
	private static final String[] STEP3_SUFFIXES1 = { "lig", "els", "ig" };
	private static final String STEP3_SUFFIXES2 = "löst";
	private static final String STEP3_SUFFIXES2_replacement = "lös";
	private static final String STEP3_SUFFIXES3 = "fullt";
	private static final String STEP3_SUFFIXES3_replacement = "full";

	private int R1;

	@Override
	public String stem(String word) {
		word = normalize(word);
		markRNumberRegion(word);
		word = step1(word);
		word = step2(word);
		word = step3(word);
		return word;
	}

	private void markRNumberRegion(String word) {
		R1 = calcR1VC(word, VOWELS);
		R1 = R1 < 3 ? 3 : R1;
	}

	private String step1(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : STEP1_SUFFIXES1) {
			if (R1String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				return word;
			}
		}

		if (R1String.endsWith(STEP1_SUFFIXES2) && isValidSEnding(word.charAt(word.length() - 2))) {
			word = removeEnding(word, 1);
		}
		return word;
	}

	private boolean isValidSEnding(char ch) {
		return contains(ch, VALID_S_ENDING);
	}

	private String step2(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : STEP2_SUFFIXES1) {
			if (R1String.endsWith(suffix)) {
				word = removeEnding(word, 1);
				return word;
			}
		}
		return word;
	}

	private String step3(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : STEP3_SUFFIXES1) {
			if (R1String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				return word;
			}
		}

		if (R1String.endsWith(STEP3_SUFFIXES2)) {
			word = removeEnding(word, STEP3_SUFFIXES2.length()) + STEP3_SUFFIXES2_replacement;
		} else if (R1String.endsWith(STEP3_SUFFIXES3)) {
			word = removeEnding(word, STEP3_SUFFIXES3.length()) + STEP3_SUFFIXES3_replacement;
		}
		return word;
	}

	@Override
	public Language getLanguage() {
		return Language.SWEDISH;
	}
}
