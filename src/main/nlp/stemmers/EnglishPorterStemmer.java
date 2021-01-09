package main.nlp.stemmers;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Porter1 stemmer for standard English
 * 
 * Source: http://snowball.tartarus.org/algorithms/porter/stemmer.html
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class EnglishPorterStemmer extends Stemmer {
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u' };
	private static final char CONSONANT = 'C';
	private static final char VOWEL = 'V';

	private Map<String, String> formDict;
	private Map<String, Integer> mDict;
	private boolean oldEnglishFlag;

	public EnglishPorterStemmer() {
		this(false);
	}

	public EnglishPorterStemmer(boolean oldEnglishIncluded) {
		oldEnglishFlag = oldEnglishIncluded;
		formDict = new HashMap<>();
		mDict = new HashMap<>();
		init();
	}

	private void init() {
		formDict.put(EMPTY, EMPTY);
		mDict.put(EMPTY, 0);
		mDict.put("C", 0);
		mDict.put("V", 0);
		mDict.put("CV", 0);
		mDict.put("VC", 1);
	}

	@Override
	public String stem(String word) {
		word = normalize(word);
		word = step1a(word);
		word = step1b(word);
		word = step1c(word);
		word = step2(word);
		word = step3(word);
		word = step4(word);
		word = step5a(word);
		word = step5b(word);
		return word;
	}

	private String step1a(String word) {
		if (word.endsWith("sses")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("ies")) {
			word = removeEnding(word, 2);
		} else if (word.endsWith("s") && !word.endsWith("ss")) {
			word = removeEnding(word, 1);
		}
		return word;
	}

	private String step1b(String word) {
		if (word.endsWith("eed")) {
			String base = removeEnding(word, 3);
			if (calcM(base) > 0) {
				word = removeEnding(word, 1);
			}
		} else if (word.endsWith("ed")) {
			String base = removeEnding(word, 2);
			if (containsVowel(base)) {
				word = step1bSpecialCase(base);
			}
		} else if (word.endsWith("ing")) {
			String base = removeEnding(word, 3);
			if (containsVowel(base)) {
				word = step1bSpecialCase(base);
			}
		} else if (oldEnglishFlag) {
			if (word.endsWith("est")) {
				String base = removeEnding(word, 3);
				if (containsVowel(base)) {
					word = step1bSpecialCase(base);
				}
			} else if (word.endsWith("eth")) {
				String base = removeEnding(word, 3);
				if (containsVowel(base)) {
					word = step1bSpecialCase(base);
				}
			}
		}
		return word;
	}

	private boolean containsVowel(String word) {
		for (int i = 0; i < word.length(); i++) {
			if (!isConsonant(word, i)) {
				return true;
			}
		}
		return false;
	}

	private boolean isConsonant(String word, int index) {
		return !(isVowel(word.charAt(index), VOWELS) || isSpecialVowel(word, index));
	}

	private boolean isSpecialVowel(String word, int index) {
		if (index < 1 || word.length() < 2) {
			return false;
		}

		char ch = word.charAt(index);
		char chBefore = word.charAt(index - 1);
		return ch == 'y' && isConsonant(chBefore, VOWELS) && !isSpecialVowel(word, index - 1);
	}

	private int calcM(String word) {
		if (mDict.containsKey(word)) {
			return mDict.get(word);
		}

		String form = generateForm(word);
		int countedM = countM(form);

		mDict.put(form, countedM);
		return countedM;
	}

	private int countM(String form) {
		int m = 0;
		for (int i = 0; i < form.length(); i++) {
			if (form.charAt(i) == VOWEL && i + 1 < form.length() && form.charAt(i + 1) == CONSONANT) {
				m++;
				i++;
			}
		}
		return m;
	}

	private String generateForm(String word) {
		if (formDict.containsKey(word)) {
			return formDict.get(word);
		}

		boolean isRecentAddedConsonant;
		StringBuilder sb = new StringBuilder(word.length());

		if (isConsonant(word, 0)) {
			sb.append(CONSONANT);
			isRecentAddedConsonant = true;
		} else {
			sb.append(VOWEL);
			isRecentAddedConsonant = false;
		}

		for (int i = 1; i < word.length(); i++) {
			if (isConsonant(word, i)) {
				if (!isRecentAddedConsonant) {
					sb.append(CONSONANT);
					isRecentAddedConsonant = true;
				}

			} else if (isRecentAddedConsonant) {
				sb.append(VOWEL);
				isRecentAddedConsonant = false;
			}
		}

		String result = sb.toString();
		formDict.put(word, result);
		return result;
	}

	private String step1bSpecialCase(String word) {
		if (word.endsWith("at") || word.endsWith("bl") || word.endsWith("iz")) {
			word += "e";
		} else if (endsWithDoubleConsonant(word) && !(word.endsWith("l") || word.endsWith("s") || word.endsWith("z"))) {
			word = removeEnding(word, 1);
		} else if (calcM(word) == 1 && cvc(word)) {
			word += "e";
		}
		return word;
	}

	private boolean cvc(String word) {
		final int length = word.length();
		if (length < 3) {
			return false;
		}
		char secondC = word.charAt(length - 1);
		return isConsonant(word, length - 3) && !isConsonant(word, length - 2) && isConsonant(word, length - 1)
				&& !(secondC == 'w' || secondC == 'x' || secondC == 'y');
	}

	private boolean endsWithDoubleConsonant(String word) {
		int length = word.length();
		if (length < 2) {
			return false;
		}
		return isConsonant(word, length - 1) && isConsonant(word, length - 2)
				&& word.charAt(length - 1) == word.charAt(length - 2);
	}

	private String step1c(String word) {
		if (word.endsWith("y")) {
			String base = removeEnding(word, 1);
			if (containsVowel(base)) {
				word = base + "i";
			}
		}
		return word;
	}

	private String replaceM(String word, int endsWithEndingLength, String newEnding, int minM) {
		String base = removeEnding(word, endsWithEndingLength);
		if (calcM(base) > minM) {
			word = base += newEnding;
		}
		return word;
	}

	private String step2(String word) {
		if (word.endsWith("ational")) {
			word = replaceM(word, 7, "ate", 0);
		} else if (word.endsWith("tional")) {
			word = replaceM(word, 6, "tion", 0);
		} else if (word.endsWith("enci")) {
			word = replaceM(word, 4, "ence", 0);
		} else if (word.endsWith("anci")) {
			word = replaceM(word, 4, "ance", 0);
		} else if (word.endsWith("izer")) {
			word = replaceM(word, 4, "ize", 0);
		} else if (word.endsWith("abli")) {
			word = replaceM(word, 4, "able", 0);
		} else if (word.endsWith("alli")) {
			word = replaceM(word, 4, "al", 0);
		} else if (word.endsWith("entli")) {
			word = replaceM(word, 5, "ent", 0);
		} else if (word.endsWith("eli")) {
			word = replaceM(word, 3, "e", 0);
		} else if (word.endsWith("ousli")) {
			word = replaceM(word, 5, "ous", 0);
		} else if (word.endsWith("ization")) {
			word = replaceM(word, 7, "ize", 0);
		} else if (word.endsWith("ation")) {
			word = replaceM(word, 5, "ate", 0);
		} else if (word.endsWith("ator")) {
			word = replaceM(word, 4, "ate", 0);
		} else if (word.endsWith("alism")) {
			word = replaceM(word, 5, "al", 0);
		} else if (word.endsWith("iveness")) {
			word = replaceM(word, 7, "ive", 0);
		} else if (word.endsWith("fulness")) {
			word = replaceM(word, 7, "ful", 0);
		} else if (word.endsWith("ousness")) {
			word = replaceM(word, 7, "ous", 0);
		} else if (word.endsWith("aliti")) {
			word = replaceM(word, 5, "al", 0);
		} else if (word.endsWith("iviti")) {
			word = replaceM(word, 5, "ive", 0);
		} else if (word.endsWith("biliti")) {
			word = replaceM(word, 6, "ble", 0);
		}
		return word;
	}

	private String step3(String word) {
		if (word.endsWith("icate")) {
			word = replaceM(word, 5, "ic", 0);
		} else if (word.endsWith("ative")) {
			word = replaceM(word, 5, EMPTY, 0);
		} else if (word.endsWith("alize")) {
			word = replaceM(word, 5, "al", 0);
		} else if (word.endsWith("iciti")) {
			word = replaceM(word, 5, "ic", 0);
		} else if (word.endsWith("ical")) {
			word = replaceM(word, 4, "ic", 0);
		} else if (word.endsWith("ful")) {
			word = replaceM(word, 3, EMPTY, 0);
		} else if (word.endsWith("ness")) {
			word = replaceM(word, 4, EMPTY, 0);
		}
		return word;
	}

	private String step4(String word) {
		if (word.endsWith("al")) {
			word = replaceM(word, 2, EMPTY, 1);
		} else if (word.endsWith("ance")) {
			word = replaceM(word, 4, EMPTY, 1);
		} else if (word.endsWith("ence")) {
			word = replaceM(word, 4, EMPTY, 1);
		} else if (word.endsWith("er")) {
			word = replaceM(word, 2, EMPTY, 1);
		} else if (word.endsWith("ic")) {
			word = replaceM(word, 2, EMPTY, 1);
		} else if (word.endsWith("able")) {
			word = replaceM(word, 4, EMPTY, 1);
		} else if (word.endsWith("ible")) {
			word = replaceM(word, 4, EMPTY, 1);
		} else if (word.endsWith("ant")) {
			word = replaceM(word, 3, EMPTY, 1);
		} else if (word.endsWith("ement")) {
			word = replaceM(word, 5, EMPTY, 1);
		} else if (word.endsWith("ment")) {
			word = replaceM(word, 4, EMPTY, 1);
		} else if (word.endsWith("ent")) {
			word = replaceM(word, 3, EMPTY, 1);
		} else if (word.endsWith("ion")) {
			String base = removeEnding(word, 3);
			if ((base.endsWith("s") || base.endsWith("t")) && calcM(base) > 1) {
				word = base;
			}
		} else if (word.endsWith("ou")) {
			word = replaceM(word, 2, EMPTY, 1);
		} else if (word.endsWith("ism") || word.endsWith("ate") || word.endsWith("iti") || word.endsWith("ous")
				|| word.endsWith("ive") || word.endsWith("ize")) {
			word = replaceM(word, 3, EMPTY, 1);
		}
		return word;
	}

	private String step5a(String word) {
		if (word.endsWith("e")) {
			String base = removeEnding(word, 1);
			int m = calcM(base);
			if (m > 1) {
				word = base;
			} else if (m == 1 && !cvc(base)) {
				word = base;
			}
		}
		return word;
	}

	private String step5b(String word) {
		if (word.endsWith("l") && endsWithDoubleConsonant(word) && calcM(word) > 1) {
			word = removeEnding(word, 1);
		}
		return word;
	}

	@Override
	public Language getLanguage() {
		return Language.ENGLISH;
	}
}
