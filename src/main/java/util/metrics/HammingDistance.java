package main.util.metrics;
/*
 * for Numbers or Strings
 * 
 * Hamming Distance 
 */
public class HammingDistance {
	public static int distance(int num1, int num2) {
		int bits = num1 ^ num2; //xor, find values different 
		int count = 0;
		while (bits > 0) {
			count += (bits & 1); //check if the current bits have the 1 bit 
			bits >>= 1; 
		}
		return count;
	}
	
	//pads extra count if sizes are difference
	public static int distance(String str1, String str2)  {
		final int length = Math.min(str1.length(), str2.length());
		int count = 0;
		
		for (int i = 0; i < length; i ++) {
			if (str1.charAt(i) != str2.charAt(i)) {
				count++;
			}
		}
		
		if (str1.length() != str2.length()) {
			count += Math.abs(str1.length() - str2.length());
		}
		return count;
	}
}
