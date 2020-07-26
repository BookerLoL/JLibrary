package similarity_metrics;
import java.util.HashMap;
import java.util.Map;

//https://pdfs.semanticscholar.org/ca84/b9f92f4cb21af00176a8a0ef887e9a5e6bc1.pdf
public class QGram {
	public static int distance(String x, String y, int ngram) {
		if (ngram <= 0 || Math.min(x.length(), y.length()) < ngram) {
			return Integer.MAX_VALUE;
		}
		
		Map<String, Integer> ngramCount = new HashMap<>();
		
		for (int i = 0; i + ngram <= x.length(); i++) {
			ngramCount.compute(x.substring(i, i + ngram), (k, v) -> v == null ? 1 : v + 1);
		}
		
		for (int i = 0; i + ngram <= y.length(); i++) {
			ngramCount.compute(y.substring(i, i + ngram), (k, v) -> v == null ? -1 : v - 1);
		}
		
		int totalDiff = 0;
		for (int diff : ngramCount.values()) {
			totalDiff += Math.abs(diff);
		}
		return totalDiff;
	}
	
	public static void main(String[] args) {
		System.out.println(QGram.distance("leia", "leela", 1));
	}
}
