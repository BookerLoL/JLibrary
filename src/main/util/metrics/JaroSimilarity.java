
public class JaroSimilarity {
	public static double distance(String x, String y) {
		if (x.equals(y)) {
			return 1.0;
		}

		final int xLength = x.length();
		final int yLength = y.length();

		int maxLength = Math.max(xLength, yLength);
		
		double matches = 0.0d;
		int matchDistance = (int)(Math.floor(maxLength / 2) - 1);

		boolean[] xMatches = new boolean[xLength];
		boolean[] yMatches = new boolean[yLength];

		for (int i = 0; i < xLength; i++) {
			int start = Math.max(0, i - matchDistance);
			int end = Math.min(i + matchDistance + 1, yLength);

			for (int j = start; j < end; j++) {
				if (x.charAt(i) == y.charAt(j) && !yMatches[j]) {
					xMatches[i] = true;
					yMatches[i] = true;
					matches++;
					break;
				}
			}
		}

		if (matches == 0) {
			return 0;
		}

		double transpositions = 0.0;
		for (int i = 0, k = 0; i < xLength; i++) {
			if (!xMatches[i]) {
				continue;
			}
			while (!yMatches[k]) {
				k++;
			}
			if (x.charAt(i) != y.charAt(k++)) {
				transpositions++;
			}
		}
		
		transpositions /= 2.0;
		return ((matches / xLength) + (matches / yLength) + ((matches - transpositions) / matches)) / 3.0;
	}
}
