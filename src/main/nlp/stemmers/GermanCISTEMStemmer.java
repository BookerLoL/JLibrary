package main.nlp.stemmers;

/**
 * 
 * CISTEM stemmer for standard German
 * 
 * Source: https://www.cis.uni-muenchen.de/~weissweiler/cistem/
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class GermanCISTEMStemmer extends Stemmer {
	boolean toStrip;
	boolean isUppercase;
	boolean isCaseInsensitive;

	public GermanCISTEMStemmer() {
		this(false);
	}

	public GermanCISTEMStemmer(boolean caseInsensitive) {
		this.isCaseInsensitive = caseInsensitive;
	}

	@Override
	public String stem(String word) {
		if (word.length() > 0) {
			isUppercase = Character.isUpperCase(word.charAt(0));
		}

		word = normalize(word);
		toStrip = true;
		word = initialReplace(word);
		while (word.length() > 3 && toStrip) {
			word = stripSuffix(word);
		}
		return finalReplace(word);
	}

	private String initialReplace(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		char prev = EMPTY_CH;
		for (int i = 0; i < word.length(); i++) {
			char curr = word.charAt(i);

			if (curr == 'ß') {
				sb.append("s*"); // ß -> ss -> s*
			} else if (prev == 's' && curr == 'c' && i + 1 < word.length() && word.charAt(i + 1) == 'h') {
				sb.setCharAt(sb.length() - 1, '$');
				i++; // skip h next iteration
				prev = '$';
			} else if (prev == curr) {
				sb.append("*");
				prev = '*';
			} else if (curr == 'ü') {
				sb.append('u');
				prev = 'u';
			} else if (curr == 'ö') {
				sb.append('o');
				prev = 'o';
			} else if (curr == 'ä') {
				sb.append('a');
				prev = 'a';
			} else if (prev == 'e' && curr == 'i') {
				sb.setCharAt(sb.length() - 1, '%');
				prev = '%';
			} else if (prev == 'i' && curr == 'e') {
				sb.setCharAt(sb.length() - 1, '&');
				prev = '&';
			} else if (i == 1 && curr == 'e' && word.length() > 6 && prev == 'g') {
				sb.deleteCharAt(0);
				prev = EMPTY_CH;
			} else {
				sb.append(curr);
				prev = curr;
			}
		}
		return sb.toString();
	}

	private String stripSuffix(String word) {
		if (word.length() > 5) {
			if (word.endsWith("em") || word.endsWith("er") || word.endsWith("nd")) {
				toStrip = true;
				return removeEnding(word, 2);
			}
		}

		if ((!isUppercase || isCaseInsensitive) && word.endsWith("t")) {
			toStrip = true;
			return removeEnding(word, 1);
		}

		if (word.endsWith("e") || word.endsWith("s") || word.endsWith("n")) {
			toStrip = true;
			return removeEnding(word, 1);
		}

		toStrip = false;
		return word;
	}

	private String finalReplace(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		char prev = EMPTY_CH;
		for (int i = 0; i < word.length(); i++) {
			char curr = word.charAt(i);

			if (curr == '*') {
				sb.append(prev);
			} else if (curr == '$') {
				sb.append("sch");
			} else if (curr == '%') {
				sb.append("ei");
			} else if (curr == '&') {
				sb.append("ie");
			} else {
				sb.append(curr);
			}
			prev = curr;
		}
		return sb.toString();
	}

	@Override
	public Language getLanguage() {
		return Language.GERMAN;
	}
}
