//https://en.wikipedia.org/wiki/Longest_common_subsequence_problem
public class LongestCommonSubsequence {
	public static int lcsLength(String x, String y) {
		int[][] dp = new int[x.length() + 1][y.length() + 1];
		for (int i = 1; i <= x.length(); i++) {
			for (int j = 1; j <= y.length(); j++) {
				if (x.charAt(i - 1) == y.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1] + 1;
				} else {
					dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
				}
			}
		}

		return dp[x.length()][y.length()];
	}

	public static String lcsString(String x, String y) {
		int[][] dp = new int[x.length() + 1][y.length() + 1];
		for (int i = 1; i <= x.length(); i++) {
			for (int j = 1; j <= y.length(); j++) {
				if (x.charAt(i - 1) == y.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1] + 1;
				} else {
					dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
				}
			}
		}
		return backtrackHelper(x, y, x.length(), y.length(), dp);
	}

	private static String backtrackHelper(String x, String y, int row, int col, int[][] dp) {
		if (row == 0 || col == 0) {
			return "";
		}
		if (x.charAt(row - 1) == y.charAt(col - 1)) {
			return backtrackHelper(x, y, row-1, col-1, dp) + x.charAt(row-1);
		}

		return dp[row][col - 1] > dp[row - 1][col] ? backtrackHelper(x, y, row, col - 1, dp)
				: backtrackHelper(x, y, row - 1, col, dp);
	}

	public static void main(String[] args) {
		System.out.println(LongestCommonSubsequence.lcsString("abcd", "acbad"));
	}
}
