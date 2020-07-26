package stemmers;

/*
 * Snowball stemmer
 * 
 * For Standard Swedish
 * 
 * https://snowballstem.org/algorithms/swedish/stemmer.html
 * 
 */
public class SwedishSnowballStemmer extends Stemmer {
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'y', 'ä', 'å', 'ö' };
	private static final char[] VALID_S_ENDING = { 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'r',
			't', 'v', 'y' };
	private static final String[] step1_suffixes1 = { "heterna", "hetens", "anden", "heten", "heter", "arnas", "ernas",
			"ornas", "andes", "arens", "andet", "arna", "erna", "orna", "ande", "arne", "aste", "aren", "ades", "erns",
			"ade", "are", "ern", "ens", "het", "ast", "ad", "en", "ar", "er", "or", "as", "es", "at", "a", "e" };
	private static final String step1_suffixes2 = "s";
	private static final String[] step2_suffixes1 = { "dd", "gd", "nn", "dt", "gt", "kt", "tt" };
	private static final String[] step3_suffixes1 = { "lig", "els", "ig" };
	private static final String step3_suffixes2 = "löst";
	private static final String step3_suffixes2_replacement = "lös";
	private static final String step3_suffixes3 = "fullt";
	private static final String step3_suffixes3_replacement = "full";

	private int R1;

	private boolean isVowel(char ch) {
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

	private void markRNumberRegion(String word) {
		R1 = word.length();

		for (int i = 0; i < word.length() - 1; i++) {
			if (isVowel(word.charAt(i)) && !isVowel(word.charAt(i + 1))) {
				R1 = i + 2;
				break;
			}
		}
		if (R1 < 3) {
			R1 = 3;
		}
	}

	private String step1(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : step1_suffixes1) {
			if (R1String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				return word;
			}
		}

		if (R1String.endsWith(step1_suffixes2) && isValidSEnding(word.charAt(word.length() - 2))) {
			word = removeEnding(word, 1);
		}
		return word;
	}

	private String step2(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : step2_suffixes1) {
			if (R1String.endsWith(suffix)) {
				word = removeEnding(word, 1);
				return word;
			}
		}
		return word;
	}

	private String step3(String word) {
		String R1String = getRegionSubstring(word, R1);
		for (String suffix : step3_suffixes1) {
			if (R1String.endsWith(suffix)) {
				word = removeEnding(word, suffix.length());
				return word;
			}
		}

		if (R1String.endsWith(step3_suffixes2)) {
			word = removeEnding(word, step3_suffixes2.length()) + step3_suffixes2_replacement;
		} else if (R1String.endsWith(step3_suffixes3)) {
			word = removeEnding(word, step3_suffixes3.length()) + step3_suffixes3_replacement;
		}
		return word;
	}

	@Override
	public String stem(String word) {
		word = normalize(word);
		markRNumberRegion(word);
		word = step1(word);
		word = step2(word);
		word = step3(word);
		return word;
	}
}
