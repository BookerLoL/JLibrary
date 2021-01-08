package main.nlp.stemmers;

/**
 * 
 * Abstract class that all stemmers will extend.
 * 
 * @author Ethan
 * @version 1.0
 */
public abstract class Stemmer {
	protected static final String EMPTY = "";
	protected static final char EMPTY_CH = '\0';

	public abstract String stem(String word);

	public abstract Language getLanguage();

	public static String normalize(String word) {
		return isEmpty(word) ? EMPTY : word.toLowerCase();
	}

	/**
	 * Calculates the position of region 1 by checking the current and next
	 * characters if they are a vowel and consonant respectively starting from 0.
	 * Returns length of the word if region 1 is not found.
	 * 
	 * @param word
	 * @param vowels associated with that language
	 * @return position of region 1
	 */
	protected static int calcR1VC(String word, char[] vowels) {
		int region1 = word.length();

		for (int i = 0; i < word.length() - 2; i++) {
			if (isVowel(word.charAt(i), vowels) && isConsonant(word.charAt(i + 1), vowels)) {
				region1 = i + 2;
				break;
			}
		}

		return region1;
	}

	/**
	 * Calculates the position of region 2 by checking the current and next
	 * characters if they are a vowel and consonant respectively starting at region
	 * 1 position minus 1. Returns length of word if region 2 is not found.
	 * 
	 * @param word
	 * @param region1 the calculated region 1 position
	 * @param vowels  associated with that language
	 * @return position of region 2
	 */
	protected static int calcR2VC(String word, int region1, char[] vowels) {
		int region2 = word.length();

		for (int i = region1 - 1; i < word.length() - 2; i++) {
			if (isVowel(word.charAt(i), vowels) && isConsonant(word.charAt(i + 1), vowels)) {
				region2 = i + 2;
				break;
			}
		}

		return region2;
	}

	
	protected static boolean isEmpty(String str) {
		return str == null || str == EMPTY;
	}

	/**
	 * Checks if one of the optional characters matches the given character.
	 * 
	 * @param ch expected character to find
	 * @param given characters to look through
	 * @return result if found expected char in the options characters
	 */
	protected static boolean contains(char ch, char[] options) {
		for (char option : options) {
			if (ch == option) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Simply a wrapper of contains to make the intention more obvious.
	 * 
	 * @param ch expected character to find.
	 * @param given vowel characters to look through
	 * @return result if found expected char in the vowels. 
	 */
	protected static boolean isVowel(char ch, char[] vowels) {
		return contains(ch, vowels);
	}
	
	/**
	 * Anything that is not a vowel is considered a consonant in this case.
	 * Simply the negation of {@code isVowel}
	 * 
	 * @param ch expected character to not find
	 * @param vowels given vowel characters to look through
	 * @return result if did not find expected char in the vowels.
	 */
	protected static boolean isConsonant(char ch, char[] vowels) {
		return !isVowel(ch, vowels);
	}

	/**
	 * Removes the number of characters from the end of the word.
	 * Removing 0 or less chars results in returning the given word.
	 * Removing more chars than the word has will return {@code EMPTY} word.
	 * 
	 * @param word
	 * @param numEndingChars number of characters to remove from the end.
	 * @return leftover word after removing ending characters.
	 * @throws NullPointerException if word is null
	 */
	protected static String removeEnding(String word, int numEndingChars) {
		if (numEndingChars <= 0) {
			return word;
		}

		int numCharsAfterRemoval = word.length() - numEndingChars;
		return numCharsAfterRemoval <= 0 ? EMPTY : word.substring(0, numCharsAfterRemoval);
	}

	/**
	 * A helper method for safe substring-ing a word.
	 * If the region index is {@code < 0 or >= word.lenght()} then returns {@code EMPTY} word.
	 * 
	 * @param word
	 * @param regionIndex starting point of the region of word to keep
	 * @return substring word starting from the region index
	 * @throws NullPointerException if word is null
	 */
	protected static String getRegionSubstring(String word, int regionIndex) {
		if (regionIndex < 0 || regionIndex >= word.length()) {
			return EMPTY;
		}
		return word.substring(regionIndex);
	}
}
