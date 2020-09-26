package main.util.metrics;
/*
 * For Strings
 * 
 * Needleman-Wunch Distance / Sellers Algorithm
 * Optimial matching Algorithm / Global Alighnment Technique
 * 
 * This can be easily modified to print the sequence steps or be more efficient 
 * by turning the method parameters into class fields
 */
public class NeedlemanWunsch {
	// Gives the option to use the max or min value along with giving the match,
	// mismatch, and gap weights
	public static int editDistance(String str1, String str2, int matchWeight, int mismatchWeight, int gapWeight,
			boolean useMax) {
		final int rowLength = str1.length() + 1;
		final int colLength = str2.length() + 1;
		int[][] matrix = new int[rowLength][colLength];

		for (int row = 1; row < rowLength; row++) {
			matrix[row][0] = matrix[row - 1][0] + gapWeight;
		}

		for (int col = 1; col < colLength; col++) {
			matrix[0][col] = matrix[0][col - 1] + gapWeight;
		}

		for (int row = 1; row < rowLength; row++) {
			for (int col = 1; col < colLength; col++) {
				int gValue = str1.charAt(row - 1) == str2.charAt(col - 1) ? matchWeight : mismatchWeight;
				if (useMax) {
					matrix[row][col] = Math.max(matrix[row - 1][col - 1] + gValue,
							Math.max(matrix[row - 1][col] + gapWeight, matrix[row][col - 1] + gapWeight));
				} else {
					matrix[row][col] = Math.min(matrix[row - 1][col - 1] + gValue,
							Math.min(matrix[row - 1][col] + gapWeight, matrix[row][col - 1] + gapWeight));
				}
			}
		}

		return matrix[str1.length()][str2.length()];
	}

	public static int alignmentLength(String str1, String str2, int matchWeight, int mismatchWeight, int gapWeight,
			boolean useMax) {
		final int rowLength = str1.length() + 1;
		final int colLength = str2.length() + 1;
		int[][] matrix = new int[rowLength][colLength];

		for (int row = 1; row < rowLength; row++) {
			matrix[row][0] = matrix[row - 1][0] + gapWeight;
		}

		for (int col = 1; col < colLength; col++) {
			matrix[0][col] = matrix[0][col - 1] + gapWeight;
		}

		for (int row = 1; row < rowLength; row++) {
			for (int col = 1; col < colLength; col++) {
				int gValue = str1.charAt(row - 1) == str2.charAt(col - 1) ? matchWeight : mismatchWeight;
				if (useMax) {
					matrix[row][col] = Math.max(matrix[row - 1][col - 1] + gValue,
							Math.max(matrix[row - 1][col] + gapWeight, matrix[row][col - 1] + gapWeight));
				} else {
					matrix[row][col] = Math.min(matrix[row - 1][col - 1] + gValue,
							Math.min(matrix[row - 1][col] + gapWeight, matrix[row][col - 1] + gapWeight));
				}
			}
		}

		// insert is right
		// delete is down
		// align is diagonal
		// backtrack now
		int row = str1.length();
		int col = str2.length();
		int moves = 0;
		while (row != 0 && col != 0) {
			int diagonal = matrix[row - 1][col - 1];
			int up = matrix[row - 1][col];
			int left = matrix[row][col - 1];

			int best;
			if (useMax) {
				best = Math.max(diagonal, Math.max(up, left));
			} else {
				best = Math.min(diagonal, Math.min(up, left));
			}

			moves++;
			if (best == diagonal) {
				row--;
				col--;
			} else if (best == up) {
				row--;
			} else { // left
				col--;
			}
		}

		// Didn't reach matrix[0][0]
		if (row != 0) {
			moves += row;
		} else if (col != 0) {
			moves += col;
		}

		return moves;
	}
}
