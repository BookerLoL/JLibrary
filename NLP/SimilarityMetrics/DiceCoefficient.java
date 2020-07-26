package similarity_metrics;

import java.util.HashMap;
import java.util.Map;

//https://en.wikipedia.org/wiki/S%C3%B8rensen%E2%80%93Dice_coefficient
//Uses bigrams
public class DiceCoefficient {
	public static double similarity(String x, String y) {
		return similarity(x, y, false);
	}
	public static double similarity(String x, String y, boolean includeDuplicates) {
		if (x.isEmpty() && y.isEmpty()) {
			return 1.0;
		} else if (x.isEmpty() || y.isEmpty()) {
			return 0.0;
		}
		
		// Could just subtract by 2 in the (nx + ny)
		int totalBigramsX = x.length() - 1;
		int totalBigramsY = y.length() - 1;

		Map<String, Integer> bigramCount = new HashMap<>();
		for (int i = 0; i <= x.length() - 2; i++) {
			if (includeDuplicates) {
				bigramCount.compute(x.substring(i, i + 2), (k, v) -> v == null ? 1 : v + 1);
			} else {
				bigramCount.computeIfAbsent(x.substring(i, i + 2), k -> 1);
			}
		}

		int intersection = 0;
		for (int i = 0; i <= y.length() - 2; i++) {
			String bigram = y.substring(i, i + 2);
			Integer count = bigramCount.get(bigram);

			if (count != null && count > 0) {
				bigramCount.put(bigram, count - 1);
				intersection++;
			}
		}

		return (2.0 * intersection) / (totalBigramsX + totalBigramsY);
	}
}
