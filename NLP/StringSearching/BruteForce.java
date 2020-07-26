package string_searching;

import java.util.LinkedList;
import java.util.List;

/*
 * Standard Naive Brute Force 
 */
public class BruteForce extends Matcher {
	@Override
	public List<Integer> matches(String text, String pattern) {
		List<Integer> matchIndices = new LinkedList<>();
		for (int i = 0; i <= text.length() - pattern.length(); i++) {
			int j;
			for (j = 0; j < pattern.length(); j++) {
				if (pattern.charAt(j) != text.charAt(i + j)) {
					break;
				}
			}

			if (j == pattern.length()) {
				matchIndices.add(i);
			}
		}
		return matchIndices;
	}

	@Override
	public boolean contains(String text, String pattern) {
		for (int i = 0; i <= text.length() - pattern.length(); i++) {
			int j;
			for (j = 0; j < pattern.length(); j++) {
				if (pattern.charAt(j) != text.charAt(i + j)) {
					break;
				}
			}

			if (j == pattern.length()) {
				return true;
			}
		}
		return false;
	}
}
