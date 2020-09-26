package main.util.metrics;
/*
 * For Strings
 * 
 * Smith Waterman 
 */
public class SmithWaterman {

	public static int score(String str1, String str2, int matchWeight, int mismatchWeight, int gapWeight) {
		final int rowLength = str1.length() + 1;
		final int colLength = str2.length() + 1;
		int[][] matrix = new int[rowLength][colLength];

		int max = Integer.MIN_VALUE;
		for (int row = 1; row < rowLength; row++) {
			for (int col = 1; col < colLength; col++) {
				int gValue = str1.charAt(row - 1) == str2.charAt(col - 1) ? matchWeight : mismatchWeight;

				matrix[row][col] = Math.max(matrix[row - 1][col - 1] + gValue,
						Math.max(matrix[row - 1][col] + gapWeight, matrix[row][col - 1] + gapWeight));
				if (max < matrix[row][col]) {
					max = matrix[row][col];
				}

			}
		}

		if (max < 0) { // matrix[0][0] is 0
			max = 0;
		}

		return max;
	}

	//Just finds one of the possible alignment lengths
	public static int alignmentLength(String str1, String str2, int matchWeight, int mismatchWeight, int gapWeight) throws IllegalStateException {
		final int rowLength = str1.length() + 1;
		final int colLength = str2.length() + 1;
		int[][] matrix = new int[rowLength][colLength];

		for (int row = 1; row < rowLength; row++) {
			matrix[row][0] = matrix[row - 1][0] + gapWeight;
		}

		for (int col = 1; col < colLength; col++) {
			matrix[0][col] = matrix[0][col - 1] + gapWeight;
		}

		int max = Integer.MIN_VALUE;
		int maxRow = 0;
		int maxCol = 0;
		for (int row = 1; row < rowLength; row++) {
			for (int col = 1; col < colLength; col++) {
				int gValue = str1.charAt(row - 1) == str2.charAt(col - 1) ? matchWeight : mismatchWeight;

				matrix[row][col] = Math.max(matrix[row - 1][col - 1] + gValue,
						Math.max(matrix[row - 1][col] + gapWeight, matrix[row][col - 1] + gapWeight));
				if (matrix[row][col] > max) {
					maxRow = row;
					maxCol = col;
				}
			}
		}

		if (max <= 0) {
			throw new IllegalStateException("Your best score was less than or equal to 0, consider better weights");
		}
		
		int row = maxRow;
		int col = maxCol;
		int moves = 0;
		while (matrix[maxRow][maxCol] != 0) {
			int diagonal = matrix[row - 1][col - 1];
			int up = matrix[row - 1][col];
			int left = matrix[row][col - 1];

			int best = Math.max(diagonal, Math.max(up, left));

			moves++;
			if (best == diagonal) {
				row--;
				col--;
			} else if (best == up) {
				row--;
			} else {
				col--;
			}
		}

		return moves;
	}
}
