package main.nlp.phonetics;

public abstract class Phonetizer {
	public abstract String encode(String name);

	/**
	 * Checks if one of the optional characters matches the given character.
	 * 
	 * @param ch    expected character to find
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
	 * @param ch    expected character to find.
	 * @param given vowel characters to look through
	 * @return result if found expected char in the vowels.
	 */
	protected static boolean isVowel(char ch, char[] vowels) {
		return contains(ch, vowels);
	}

	/**
	 * Pads a StringBuilder up to maxLength with the given padding letter.
	 * 
	 * @param sb        StringBuilder to pad
	 * @param maxLength max length the StringBuilder to be padded
	 * @param padLetter padding letter
	 * @return padded StringBuilder
	 * @throws NullPointerException if sb is null
	 */
	protected static StringBuilder pad(StringBuilder sb, int maxLength, char padLetter) {
		while (sb.length() < maxLength) {
			sb.append(padLetter);
		}
		return sb;
	}

	protected static String limitLength(String name, int length) {
		return name.length() <= length ? name : name.substring(0, length);
	}

}
