package similarity_metrics;

import java.util.HashMap;
import java.util.Map;

//The algorithm from wikipedia was weird and didn't make sense
//https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance#:~:text=Informally%2C%20the%20Damerau%E2%80%93Levenshtein%20distance,one%20word%20into%20the%20other.
//https://stackoverflow.com/questions/6033631/levenshtein-to-damerau-levenshtein
public class DamerauDistance {
	public static int distance(String str1, String str2) {
		Map<Character, Integer> da = new HashMap<>();
		for (int i = 0; i < str1.length(); i++) {
			da.put(str1.charAt(i), 0);
		}

		for (int i = 0; i < str2.length(); i++) {
			da.put(str2.charAt(i), 0);
		}

		int[][] mem = new int[str1.length() + 2][str2.length() + 2];
		final int MAX_DIST = str1.length() + str2.length();
		mem[0][0] = MAX_DIST;
		for (int i = 0; i <= str1.length(); i++) {
			mem[i + 1][0] = MAX_DIST;
			mem[i + 1][1] = i;
		}

		for (int j = 0; j <= str2.length(); j++) {
			mem[0][j + 1] = MAX_DIST;
			mem[1][j + 1] = j;
		}

		int cost, i1, j1, db;
		for (int i = 1; i <= str1.length(); i++) {
			db = 0;

			for (int j = 1; j <= str2.length(); j++) {
				i1 = da.get(str2.charAt(j - 1));
				j1 = db;
				if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
					cost = 0;
					db = j;
				} else {
					cost = 1;
				}

				mem[i+1][j+1] = min(
						mem[i][j + 1] + 1, 
						mem[i + 1][j] + 1, 
						mem[i][j] + cost,
						mem[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1));
			}
			da.put(str1.charAt(i - 1), i);
		}

		return mem[str1.length()][str2.length()];
	}

	private static int min(int... nums) {
		int min = Integer.MAX_VALUE;
		for (int num : nums) {
			min = Math.min(min, num);
		}
		return min;
	}

	public static void main(String[] args) {
		String str1 = "acress";
		String str2 = "acres";
		System.out.println(DamerauDistance.distance(str1, str2));
	}
}
