package main.nlp.stringmatching;
import java.util.HashMap;

//reference: https://www.inf.hs-flensburg.de/lang/algorithmen/pattern/sundayen.htm
public class SundayStringMatch {
	public static int matches(String pattern, String text) {
		HashMap<Character, Integer> fallBack = getLastOccurenceMap(pattern);
		final int TEXT_LENGTH = text.length(), PATTERN_LENGTH = pattern.length();
		int i = PATTERN_LENGTH-1, found = -1;
		while (i < TEXT_LENGTH) {
			for(int j = i, k = PATTERN_LENGTH-1; k >= 0; j--, k--) {
				if (text.charAt(j) != pattern.charAt(k)) {
					break;
				} else if (k == 0) {
					found = j;
					return found; 
				}
			}
			
			if (i + 1 < TEXT_LENGTH) { //check next letter to the right
				i -= fallBack.getOrDefault(text.charAt(i+1), -1);
			}
			i += PATTERN_LENGTH;
		}
		return found;
	}
	
	private static HashMap<Character, Integer> getLastOccurenceMap(String pattern) {
		HashMap<Character, Integer> occurenceMap = new HashMap<>(pattern.length()); //usually not the case all unique
		for (int i = 0; i < pattern.length(); i++) {
			occurenceMap.put(pattern.charAt(i), i);
		}
		return occurenceMap;
	}
	
	public static void main(String[] args) {
		System.out.println(SundayStringMatch.matches("bcaab", "abcabdaacba"));
		System.out.println(SundayStringMatch.matches("ababaca", "bacbabababacaca"));
		System.out.println(SundayStringMatch.matches("AAAAB", "AAAAAAAAAAAAAAAAAB"));
		System.out.println(SundayStringMatch.matches("ABABAC", "ABABABCABABABCABABABC"));
		System.out.println(SundayStringMatch.matches("ABABCABAB", "ABABDABACDABABCABAB"));
		System.out.println(SundayStringMatch.matches("TEST", "THIS IS A TEST TEXT"));
		System.out.println(SundayStringMatch.matches("AABA", "AABAACAADAABAABA"));
	}
}
