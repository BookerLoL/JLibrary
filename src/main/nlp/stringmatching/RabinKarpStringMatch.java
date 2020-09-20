package main.nlp.stringmatching;
//reference: https://en.wikipedia.org/wiki/Rabin%E2%80%93Karp_algorithm
public class RabinKarpStringMatch {
	public static int matches(String pattern, String text) {		
		int hashCode = pattern.hashCode(), found = -1;
		final int PATTERN_LENGTH = pattern.length();
		for (int i = 0; i < text.length() - pattern.length() + 1; i++) {
			String sub = text.substring(i, i+pattern.length());
			if (hashCode == sub.hashCode()) {
				for (int j = 0; j < PATTERN_LENGTH; j++) {
					if (pattern.charAt(j) != text.charAt(i + j)) {
						break;
					} else if (j == PATTERN_LENGTH-1) {
						found = i;
						return found;
					}
				}
			}
		}
		return found;
	}
	
	public static void main(String[] args) {
		System.out.println(RabinKarpStringMatch.matches("bcaab", "abcabdaacba"));
		System.out.println(RabinKarpStringMatch.matches("ababaca", "bacbabababacaca"));
		System.out.println(RabinKarpStringMatch.matches("AAAAB", "AAAAAAAAAAAAAAAAAB"));
		System.out.println(RabinKarpStringMatch.matches("ABABAC", "ABABABCABABABCABABABC"));
		System.out.println(RabinKarpStringMatch.matches("ABABCABAB", "ABABDABACDABABCABAB"));
		System.out.println(RabinKarpStringMatch.matches("TEST", "THIS IS A TEST TEXT"));
		System.out.println(RabinKarpStringMatch.matches("AABA", "AABAACAADAABAABA"));
	}
}
