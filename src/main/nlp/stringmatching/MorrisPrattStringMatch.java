package main.nlp.stringmatching;
public class MorrisPrattStringMatch {
	public static int matches(String pattern, String text) {
		final int TEXT_LENGTH = text.length(), PATTERN_LENGTH = pattern.length();
		int pIndex = 0, tIndex = 0;
		int[] MPNext = getMPNextTable(pattern);
		while (tIndex < TEXT_LENGTH) {
			while (pIndex > -1 && pattern.charAt(pIndex) != text.charAt(tIndex)) {
				pIndex = MPNext[pIndex];
			}
			pIndex++;
			tIndex++;
			if (pIndex >= PATTERN_LENGTH) {
				return tIndex - pIndex;
				// i = mpNext[i]
			}
		}
		return -1;
	}

	private static int[] getMPNextTable(String pattern) {
		int[] MPNext = new int[pattern.length() + 1];
		MPNext[0] = -1;
		int i = 0, j = -1;
		while (i < pattern.length()) {
			while (j > -1 && pattern.charAt(i) != pattern.charAt(j)) {
				j = MPNext[j];
			}
			MPNext[++i] = ++j;
		}
		return MPNext;
	}

	public static void main(String[] args) {
		System.out.println(MorrisPrattStringMatch.matches("bcaab", "abcabdaacba"));
		System.out.println(MorrisPrattStringMatch.matches("ababaca", "bacbabababacaca"));
		System.out.println(MorrisPrattStringMatch.matches("AAAAB", "AAAAAAAAAAAAAAAAAB"));
		System.out.println(MorrisPrattStringMatch.matches("ABABAC", "ABABABCABABABCABABABC"));
		System.out.println(MorrisPrattStringMatch.matches("ABABCABAB", "ABABDABACDABABCABAB"));
		System.out.println(MorrisPrattStringMatch.matches("TEST", "THIS IS A TEST TEXT"));
		System.out.println(MorrisPrattStringMatch.matches("AABA", "AABAACAADAABAABA"));
	}
}
