package main.nlp.phonetics;

/**
 * Caverphone 1 for standard English Names
 * 
 * Source: https://en.wikipedia.org/wiki/Soundex
 * 
 * Source Date: January 09, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 * @apiNote UNFINISHED
 */
public class Caverphone1 extends Phonetizer {
	private static final int MAX_LENGTH = 6;
	private static final String SIX_ONES = "111111";

	// {word, replacement}
	private static final String[][] PREFIXES_AND_REPLACEMENTS = { { "cough", "cou2f" }, { "rough", "rou2f" },
			{ "tough", "tou2f" }, { "enough", "enou2f" }, { "gn", "2n" } };
	private static final String[][] SUFFIXES_AND_REPLACEMENTS = { { "mb", "m2" } };

	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u' };
	private static final char[] SPECIAL_VOWELS = { 'æ', 'ā', 'ø' };
	private static final char[] GROUP_LETTERS = { 's', 't', 'p', 'k', 'f', 'm', 'n' };
	private boolean includeSpecialVowels;

	public Caverphone1() {
		this(false);
	}

	public Caverphone1(boolean specialVowels) {
		includeSpecialVowels = specialVowels;
	}

	private boolean isSpecialVowel(char ch) {
		return contains(ch, SPECIAL_VOWELS);
	}

	private boolean isVowel(char ch) {
		return contains(ch, VOWELS) || (isSpecialVowelsIncluded() && contains(ch, SPECIAL_VOWELS));
	}

	private boolean isGroupLetter(char ch) {
		return contains(ch, GROUP_LETTERS);
	}

	public boolean isSpecialVowelsIncluded() {
		return includeSpecialVowels;
	}

	public void setSpecialVowels(boolean shouldInclude) {
		includeSpecialVowels = shouldInclude;
	}

	private String removeNonLettersAndLowercase(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char curr = name.charAt(i);
			if (Character.isLetter(curr) || (isSpecialVowelsIncluded() && isSpecialVowel(curr))) {
				sb.append(Character.toLowerCase(curr));
			}
		}
		return sb.toString();
	}

	private String replacePrefixesAndSuffixes(String name) {
		for (String[] prefixInfo : PREFIXES_AND_REPLACEMENTS) {
			if (name.startsWith(prefixInfo[0])) {
				name = prefixInfo[1] + name.substring(prefixInfo[0].length());
				break;
			}
		}

		for (String[] suffixInfo : SUFFIXES_AND_REPLACEMENTS) {
			if (name.endsWith(suffixInfo[0])) {
				name = name.substring(0, name.length() - suffixInfo[0].length()) + suffixInfo[1];
				break;
			}
		}

		return name;
	}

	private String transform(String name) {
		return name;
	}

	private String removeNumbers(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char curr = name.charAt(i);
			if (curr != '2' && curr != '3') {
				sb.append(curr);
			}
		}
		return sb.toString();
	}

	private String appendSixOnes(String name) {
		return name + SIX_ONES;
	}

	private String limitLength(String name) {
		return name.substring(0, MAX_LENGTH);
	}

	@Override
	public String encode(String name) {
		name = removeNonLettersAndLowercase(name);
		name = replacePrefixesAndSuffixes(name);
		name = transform(name);
		name = removeNumbers(name);
		name = appendSixOnes(name);
		return limitLength(name);
	}

	public static void main(String[] args) {
		Caverphone1 c = new Caverphone1();
		System.out.println(c.encode("T23MPS3N"));
		// T23MPS3N
	}
}
