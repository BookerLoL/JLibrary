package similarity_metrics;

/*
 * For Strings
 * 
 * Levenshtein Distance / Edit Distance
 */
public class LevenshteinDistance {
	public static int distance(String str1, String str2) {
		final int rowLength = str1.length() + 1;
		final int colLength = str2.length() + 1;
		int[][] mem = new int[rowLength][colLength];
		
		for (int row = 1; row < rowLength; row++) {
			mem[row][0] = row;
		}
		
		for (int col = 1; col < colLength; col++) {
			mem[0][col] = col;
		}
		
		for (int row = 1; row < rowLength; row++) {
			for (int col = 1; col < colLength; col++) {
				int d = str1.charAt(row-1) == str2.charAt(col-1) ? 0 : 1;
				mem[row][col] = Math.min(mem[row][col-1] + 1, Math.min(mem[row-1][col] + 1, mem[row-1][col-1] + d));
			}
		}
		
		return mem[str1.length()][str2.length()];
	}
}
