package main.util.metrics;
//https://statisticaloddsandends.wordpress.com/2019/09/11/what-is-jaro-jaro-winkler-similarity/
public class JaroWrinklerSimilarity {
	public static double distance(String x, String y, double scalingFactor) {
		return distance(x, y, scalingFactor, 4);
	}
	
	public static double distance(String x, String y, double scalingFactor, int maxPrefixLength) {
		double jd = JaroSimilarity.distance(x, y);
		double maxScalingFactor = 1.0 / maxPrefixLength;
		if (scalingFactor > maxScalingFactor) { 
			//result could be over 1 if not handled
			scalingFactor = maxScalingFactor;
		}
	
		int prefixes = 0;
		int minLength = Math.min(x.length(), y.length());
		for (int i = 0; i < minLength && prefixes < maxPrefixLength; i++) {
			if (x.charAt(i) != y.charAt(i)) {
				break;
			}
			prefixes++;
		}
		
		return jd + (prefixes * scalingFactor) * (1 - jd);
	}
	
	public static double distance(String x, String y) {
		return distance(x, y, 0.1, 4);
	}
	
	public static void main(String[] args) {
		System.out.println(JaroWrinklerSimilarity.distance("TRATE", "TRACE"));
	}
}
