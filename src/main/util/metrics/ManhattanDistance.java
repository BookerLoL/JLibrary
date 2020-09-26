package main.util.metrics;
/*
 * For Numbers
 * 
 * Manhattan Distance / Taxicab norm / 
 * L1 Norm / Block Distance / City Block Distance
 */
public class ManhattanDistance {
	public static int distance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
	
	//asserts all same length
	public static long distance(int[] x1, int[] y1, int[] x2, int[] y2) {
		if (x1.length != y1.length || y1.length != x2.length || x2.length != y2.length) {
			throw new IllegalArgumentException("All parameters must have equal length");
		}
		
		long summation = 0;
		for (int i = 0; i < x1.length; i++) {
			summation += distance(x1[i], y1[i], x2[i], y2[i]);
		}
		
		return summation;
	}
}
