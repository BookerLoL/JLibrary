package segmenters;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/*
 * Maximal Matching Algorithm / Longest Matching Algorithm
 * 
 * Not good with English that has no spaces
 * 
 * Good for Chinese
 */
public class MaxMatch {

	public static List<String> segment(String runningText, Set<String> dictionary) {
		List<String> segmentedWords = new LinkedList<>();
		if (runningText.isEmpty()) {
			return segmentedWords;
		}

		for (int i = 0; i < runningText.length();) {
			StringBuilder sb = new StringBuilder(runningText.substring(i));
			while (sb.length() > 1) { //Creates 1 letter word if true
				if (dictionary.contains(sb.toString())) {
					break;
				}
				sb.setLength(sb.length() - 1);
			}

			segmentedWords.add(sb.toString());
			i += sb.length();
		}
		return segmentedWords;
	}
}
