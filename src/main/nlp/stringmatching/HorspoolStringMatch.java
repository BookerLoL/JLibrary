package main.nlp.stringmatching;
import java.util.HashMap;

//reference: https://www.inf.hs-flensburg.de/lang/algorithmen/pattern/horsen.htm
public class HorspoolStringMatch {
	public static int matches(String pattern, String text) {
		HashMap<Character, Integer> fallBack = getLastOccurenceMap(pattern);
		final int TEXT_LENGTH = text.length(), PATTERN_LENGTH_M1 = pattern.length()-1; //M1, minus 1
		int i = PATTERN_LENGTH_M1, found = -1;
		while (i < TEXT_LENGTH) {
			for (int j = i, k = PATTERN_LENGTH_M1; k >= 0; j--, k--) {
				if (text.charAt(j) != pattern.charAt(k)) {
					break;
				} else if (k == 0) {
					found = j;
					return found;
				}
			}

			i -= fallBack.getOrDefault(text.charAt(i), -1);
			i += PATTERN_LENGTH_M1;
		}
		return found;
	}

	private static HashMap<Character, Integer> getLastOccurenceMap(String pattern) {
		HashMap<Character, Integer> occurenceMap = new HashMap<>(pattern.length()); // usually not the case all unique
		for (int i = 0; i < pattern.length()-1; i++) {
			occurenceMap.put(pattern.charAt(i), i);
		}
		return occurenceMap;
	}

	public static void main(String[] args) {
		System.out.println(HorspoolStringMatch.matches("bcaab", "abcabdaacba"));
		System.out.println(HorspoolStringMatch.matches("ababaca", "bacbabababacaca"));
		System.out.println(HorspoolStringMatch.matches("AAAAB", "AAAAAAAAAAAAAAAAAB"));
		System.out.println(HorspoolStringMatch.matches("ABABAC", "ABABABCABABABCABABABC"));
		System.out.println(HorspoolStringMatch.matches("ABABCABAB", "ABABDABACDABABCABAB"));
		System.out.println(HorspoolStringMatch.matches("TEST", "THIS IS A TEST TEXT"));
		System.out.println(HorspoolStringMatch.matches("AABA", "AABAACAADAABAABA"));
	}
}
