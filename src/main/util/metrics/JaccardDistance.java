package main.util.metrics;
import java.util.HashMap;
import java.util.Map;

public class JaccardDistance {
	public static double similarity(String x, String y) {
		return similarity(x, y, 2, false);
	}

	public static double similarity(String x, String y, int ngram) {
		return similarity(x, y, ngram, false);
	}

	public static double similarity(String x, String y, boolean includeDuplicates) {
		return similarity(x, y, 2, true);
	}

	public static double similarity(String x, String y, int ngram, boolean includeDuplicates) {
		if (x.isEmpty() && y.isEmpty()) {
			return 1.0;
		} else if (x.isEmpty() || y.isEmpty()) {
			return 0.0;
		} else if (ngram <= 0) {
			return Integer.MAX_VALUE;
		}

		Map<String, Integer> countFreq = new HashMap<>();

		for (int i = 0; i <= x.length() - ngram; i++) {
			if (includeDuplicates) {
				countFreq.compute(x.substring(i, i + 2), (k, v) -> v == null ? 1 : v + 1);
			} else {
				String subSeq = x.substring(i, i + 2);
				if (!countFreq.containsKey(subSeq)) {
					countFreq.put(subSeq, 1);
				}
			}
		}
		int xSetSize = includeDuplicates ? x.length() - (ngram - 1) : countFreq.size();

		int intersections = 0;
		int ySetSize = 0;
		for (int i = 0; i <= y.length() - 2; i++) {
			String bigram = y.substring(i, i + 2);
			Integer count = countFreq.get(bigram);

			if (!includeDuplicates && count == null) { // first time seeing it
				countFreq.put(bigram, -1);
				ySetSize++;
			}

			if (count != null && count > 0) {
				if (!includeDuplicates && count == 1) { // other string had it, only count this once
					ySetSize++;
				}

				countFreq.put(bigram, count - 1);
				intersections++;
			}
		}

		ySetSize = includeDuplicates ? y.length() - (ngram - 1) : ySetSize;
		System.out.println(ySetSize + "\t" + xSetSize + "\t" + intersections);
		return 1.0 - (double) intersections / (xSetSize + ySetSize - intersections);
	}
}
