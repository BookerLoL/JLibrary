package main.nlp.segmenters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Maximal Matching ALgorithm implementation
 * 
 * @author Ethan Booker
 * @version 1.0
 */
public class MaxMatch {
	public static List<String> segment(String runningText, Set<String> dictionary) {
		List<String> segmentedWords = new ArrayList<>();
		
		if (runningText != null && !runningText.isEmpty()) {
			for (int i = 0; i < runningText.length();) {
				StringBuilder sb = new StringBuilder(runningText.substring(i));
				while (sb.length() > 1) {
					if (dictionary.contains(sb.toString())) {
						break;
					}
					sb.setLength(sb.length() - 1);
				}

				segmentedWords.add(sb.toString());
				i += sb.length();
			}
		}

		return segmentedWords;
	}
}
