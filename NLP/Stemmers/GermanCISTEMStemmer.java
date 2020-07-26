package stemmers;

/*
 * CISTEM
 * 
 * For Standard German
 * 
 * https://www.cis.uni-muenchen.de/~weissweiler/cistem/
 * 
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
	
	

	private String initialReplace(String word) {
		StringBuilder sb = new StringBuilder(word.length());
		char prev = '\0';
		for (int i = 0; i < word.length(); i++) {
			char curr = word.charAt(i);

			if (curr == 'ß') {
				sb.append("s*"); // ß -> ss -> s*
			} else if (prev == 's' && curr == 'c' && i + 1 < word.length() && word.charAt(i + 1) == 'h') {
				sb.setCharAt(sb.length() - 1, '$'); // replace s -> $
				i++; // skip h next iteration
				prev = '$';
			} else if (prev == curr) { // double of the same characters
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
			} else if (curr == 'e' && i == 1 && word.length() > 6 && prev == 'g') { // special case at the start of the
																					// word
				sb.deleteCharAt(0);
				prev = '\0';
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
		char prev = '\0';
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
	public String stem(String word) {
		if (word.length() > 0) {
			isUppercase = Character.isUpperCase(word.charAt(0));
			System.out.println(isUppercase);
		}
		
		word = normalize(word);
		toStrip = true;
		word = initialReplace(word);
		System.out.println("After init replace: " + word);
		while (word.length() > 3 && toStrip) {
			word = stripSuffix(word);
			System.out.println("After strip suffix: " + word);
		}
		return finalReplace(word);
	}
}
