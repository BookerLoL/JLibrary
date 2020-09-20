package main.nlp.stemmers;
/*
 * Improvements:
 * 
 * Instead of passing Strings, should have a char buffer and manually manipulate the chars with bit manipulations
 * 
 * Could have a class that maintains information about the String to avoid excess calls on Java functions
 * 
 * Could cache results for later use
 * 
 * Many other optimizations that could be implemented
 * 
 */
public abstract class Stemmer {
	protected static final String EMPTY = "";
	
	protected static String normalize(String word) {
		return word.toLowerCase();
	}
	
	protected static String removeEnding(String word, int numEndingChars) {
		if (numEndingChars == 0) {
			return word;
		}
		
		int diff = word.length() - numEndingChars;
		
		if (diff < 1) {
			return EMPTY;
		} else if (diff > word.length()) {
			return word;
		}	
		return word.substring(0, diff);
	}
	
	protected static String getRegionSubstring(String word, int regionIndex) {
		if (regionIndex < 0 || regionIndex >= word.length()) {
			return EMPTY;
		}
		return word.substring(regionIndex);
	}
	
	protected boolean isEmpty(String str) {
		return str == "";
	}
	
	public abstract String stem(String word);
}
