package main.nlp.stringmatching;
public class KMPStringMatch {
	public static int matches(String pattern, String text) {
		final int LAST_INDEX = pattern.length() - 1;
		int i = 0, j = 0, found = -1;
		int[] prefixTable = prefixTable(pattern);
		while (i < text.length()) {
			if (text.charAt(i) == pattern.charAt(j)) {
				if (j == LAST_INDEX) {
					found = i - j;
					break;
					//j = prefixTable[j-1], if you want all the spots, add found to a list
				} else {
					i++;
					j++;
				}
			} else if (j > 0) {
				j = prefixTable[j-1];
			} else {
				i++;
			}
		}
		return found;
	}

	private static int[] prefixTable(String pattern) {
		int[] failTable = new int[pattern.length()];
		int index = 1, failIndex = 0;
		while (index < pattern.length()) {
			if (pattern.charAt(index) == pattern.charAt(failIndex)) {
				failTable[index++] = ++failIndex;
			} else if (failIndex > 0) {
				failIndex = failTable[failIndex - 1];
			} else {
				index++;
			}
		}
		return failTable;
	}
	
	public static void main(String[] args) {
		System.out.println(KMPStringMatch.matches("bcaab", "abcabdaacba"));
		System.out.println(KMPStringMatch.matches("ababaca", "bacbabababacaca"));
		System.out.println(KMPStringMatch.matches("AAAAB", "AAAAAAAAAAAAAAAAAB"));
		System.out.println(KMPStringMatch.matches("ABABAC", "ABABABCABABABCABABABC"));
		System.out.println(KMPStringMatch.matches("ABABCABAB", "ABABDABACDABABCABAB"));
		System.out.println(KMPStringMatch.matches("TEST", "THIS IS A TEST TEXT"));
		System.out.println(KMPStringMatch.matches("AABA", "AABAACAADAABAABA"));
	}
}
