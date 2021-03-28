package main.util.metrics;

public class ChebyshevDistance {
	public static int distance(int[] x, int[] y) {
		int dist = 0;
		for (int i = 0; i < x.length; i++) {
			int distance = Math.abs(x[i] - y[i]);
			if (distance > dist) {
				dist = distance;
			}
		}
		return dist;
	}
}
