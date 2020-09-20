package main.nlp.stemmers;
/*
 * S Stemmer by Harman
 * 
 * For English
 * 
 * https://asistdl.onlinelibrary.wiley.com/doi/epdf/10.1002/%28SICI%291097-4571%28199101%2942%3A1%3C7%3A%3AAID-ASI2%3E3.0.CO%3B2-P
 * 
 * "How Effective is Suffixing?" Donna Harman
 * 
 */
public class SStemmer extends Stemmer {
	@Override
	public String stem(String word) {
		word = normalize(word);
		
		if (word.endsWith("ies")) {
			if (word.length() > 3) {
				char prev = word.charAt(word.length()-4);
				if ((prev != 'a') && (prev != 'e')) {
					word = removeEnding(word, 3) + "y";
				}
			}
		} else if (word.endsWith("es")) {
			if (word.length() > 2) {
				char prev = word.charAt(word.length()-3);
				if ((prev != 'a') && (prev != 'e') && (prev != 'o')) {
					word = removeEnding(word, 1);
				}
			}
		} else if (word.endsWith("s")) {
			if (word.length() > 1) {
				char prev = word.charAt(word.length()-2);
				if ((prev != 'u') && (prev != 's')) {
					word = ""; //in the paper it says null, but changing it to ""
				}
			}
		}
		
		return word;
	}

}
