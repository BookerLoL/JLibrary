//Longest Common Substring and Longest Common Substring Length
public class LCS {
	public static int lcsLength(String x, String y) {
		int[][] dp = new int[x.length() + 1][y.length() + 1];
		int max = 0;
		for (int i = 1; i <= x.length(); i++) {
			for (int j = 1; j <= y.length(); j++) {
				dp[i][j] = x.charAt(i - 1) == y.charAt(j - 1) ? 1 + dp[i-1][j-1] : 0;
				max = Math.max(max, dp[i][j]);
			}
		}
		return max;
	}
	
	public static String lcsString(String x, String y) {
		int[][] dp = new int[x.length() + 1][y.length() + 1];
		int max = 0;
		int xSubstringIndex = 0;
		for (int i = 1; i <= x.length(); i++) {
			for (int j = 1; j <= y.length(); j++) {
				dp[i][j] = x.charAt(i - 1) == y.charAt(j - 1) ? 1 + dp[i-1][j-1] : 0;
				if (dp[i][j] > max) {
					max = dp[i][j];
					xSubstringIndex = i;
				}
			}
		}
		
		return x.substring(xSubstringIndex-max, xSubstringIndex);
	}
	
	public static void main(String[] args) {
		System.out.println(LCS.lcsString("abcd", "efhg"));
	}
}
