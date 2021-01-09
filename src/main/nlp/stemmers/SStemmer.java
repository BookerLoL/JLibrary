package main.nlp.stemmers;

/**
 * 
 * S Stemmer for standard English
 * 
 * Source: https://asistdl.onlinelibrary.wiley.com/doi/epdf/10.1002/%28SICI%291097-4571%28199101%2942%3A1%3C7%3A%3AAID-ASI2%3E3.0.CO%3B2-P
 * 
 * Source Date: January 6, 2021
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class SStemmer extends Stemmer {
	@Override
	public String stem(String word) {
		word = normalize(word);

		if (word.endsWith("ies")) {
			if (word.length() > 3) {
				char prev = word.charAt(word.length() - 4);
				if ((prev != 'a') && (prev != 'e')) {
					word = removeEnding(word, 3) + "y";
				}
			}
		} else if (word.endsWith("es")) {
			if (word.length() > 2) {
				char prev = word.charAt(word.length() - 3);
				if ((prev != 'a') && (prev != 'e') && (prev != 'o')) {
					word = removeEnding(word, 1);
				}
			}
		} else if (word.endsWith("s")) {
			if (word.length() > 1) {
				char prev = word.charAt(word.length() - 2);
				if ((prev != 'u') && (prev != 's')) {
					word = EMPTY;
				}
			}
		}

		return word;
	}
	
	@Override
	public Language getLanguage() {
		return Language.ENGLISH;
	}

}
