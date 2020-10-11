package main.optimizing;

/**
 * This class is designed to show you an example of how to optimize code in
 * general by eliminating common subexpressions.
 * 
 * <p>
 * Why do the same work when you can cache that saved work.
 * 
 * 
 * @author Ethan
 * @version 1.0
 */
public class CommonSubexpressionExlimination {
	public static void main(String[] args) {
		double limit = 10.0, max = 30.0, scalar = 2.54, value1 = 3, value2 = 7;

		System.out.println(slowExample(limit, max, scalar, value1, value2));
		System.out.println(fastExample(limit, max, scalar, value1, value2));
	}

	private static double slowExample(double limit, double max, double scalar, double value1, double value2) {
		double val1 = scalar * (limit / max) * value1;
		double val2 = scalar * (limit / max) * value2;
		return val1 + val2;
	}

	/*
	 * As you can see, we cached the common work between the two. We also simplified
	 * the expression using basic math rules.
	 */
	private static double fastExample(double limit, double max, double scalar, double value1, double value2) {
		double scaled = scalar * (limit / max);
		return scaled * (value1 + value2);
	}
}
