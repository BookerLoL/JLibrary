package main.util.metrics;

//https://en.wikipedia.org/wiki/Lee_distance
public class LeeDistance {
	public static int distance(int[] x, int[] y, int q) {
		int dist = 0;
		for (int i = 0; i < x.length; i++) {
			int d = Math.abs(x[i] - y[i]);
			dist += Math.min(dist, q - d);
		}
		return dist;
	}
}
