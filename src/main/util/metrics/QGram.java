package main.util.metrics;

import java.util.HashMap;
import java.util.Map;

//https://pdfs.semanticscholar.org/ca84/b9f92f4cb21af00176a8a0ef887e9a5e6bc1.pdf
//http://profs.scienze.univr.it/~liptak/FundBA/slides/StringDistance2_6up.pdf, this is another version but didn't implement
public class QGram {
	public static int distance(String x, String y, int ngram) {
		if (x.isEmpty() && y.isEmpty()) {
			return 0;
		}
		if (ngram <= 0 || Math.min(x.length(), y.length()) < ngram) {
			return Integer.MIN_VALUE;
		}

		Map<String, Integer> ngramCount = new HashMap<>();

		for (int i = 0; i <= x.length() - ngram; i++) {
			ngramCount.compute(x.substring(i, i + ngram), (k, v) -> v == null ? 1 : v + 1);
		}

		for (int i = 0; i <= y.length() - ngram; i++) {
			ngramCount.compute(y.substring(i, i + ngram), (k, v) -> v == null ? -1 : v - 1);
		}

		int totalDiff = ngramCount.values().stream().mapToInt(count -> Math.abs(count)).sum();
		return totalDiff;
	}
}
