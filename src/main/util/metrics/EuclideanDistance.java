package similarity_metrics;

/*
 * For Numbers
 * 
 * Euclidean distance / L2 Distance
 */
public class EuclideanDistance {
	//example: p = (x, y, z...), q = (x, y, z...)
	public static double nDimensiondistance(int[] p, int[] q) {
		if (p.length != q.length) {
			throw new IllegalArgumentException("Lengths should be the same for the parameters");
		}
		
		double distance = 0.0d;
		
		for (int i = 0; i < p.length; i++) {
			distance += Math.pow((p[i] - q[i]), 2);
		}
		
		return Math.sqrt(distance);
	}
}
