package string_searching;

import java.util.LinkedList;
import java.util.List;

/*
 * Bitap / Baeza-Yates-Gonnet / Shift-AND matching Algorithm
 * 
 * https://en.wikipedia.org/wiki/Bitap_algorithm
 */
public class Bitap extends Matcher {

	@Override
	public List<Integer> matches(String text, String pattern) {
		List<Integer> indices = new LinkedList<>();
		int m = pattern.length();
		byte pattern_mask[] = new byte[Character.MAX_VALUE + 1];
		byte R = ~1;
		
		for (int i = 0; i <= Character.MAX_VALUE; i++) {
			pattern_mask[i] = ~0;
		}
		
		for (int i = 0; i < m; i++) {
			pattern_mask[pattern.charAt(i)] &= ~(1 << i);
		}
			
		for (int i = 0; i < text.length(); i++) {
			R |= pattern_mask[text.charAt(i)];
			R <<= 1;
			if ((R & (1 << m)) == 0) {
				indices.add(i - m + 1);
			}
		}

		return indices;
	}

	@Override
	public boolean contains(String text, String pattern) {
		int m = pattern.length();
		byte pattern_mask[] = new byte[Character.MAX_VALUE + 1];
		byte R = ~1;
		
		for (int i = 0; i <= Character.MAX_VALUE; i++) {
			pattern_mask[i] = ~0;
		}
		
		for (int i = 0; i < m; i++) {
			pattern_mask[pattern.charAt(i)] &= ~(1L << i);
		}
			
		for (int i = 0; i < text.length(); i++) {
			R |= pattern_mask[text.charAt(i)];
			R <<= 1;
			if ((R & (1 << m)) == 0) {
				return true;
			}
		}
		return false;
	}

}
