package main.optimizing;

/**
 * This class is designed to show you an example of how to optimize code in
 * general by using iterative functions over recursive functions.
 * 
 * <p>
 * Recursion functions require stack maintanence and possibly stack overflow.
 * Iterative is always faster if written equivalently.
 * 
 * 
 * @author Ethan
 * @version 1.0
 */
public class Iterative {
	public static int addPositiveNumberIter(int start, int add) {
		while (add > 0) {
			add--;
			start++;
		}
		return start;
	}

	public static int addPositiveNumber(int start, int add) {
		if (add <= 0) {
			return start;
		}
		return 1 + addPositiveNumber(start, add - 1);
	}

	public static void main(String[] args) {
		int start = 0, end = 15000;
		System.out.println(addPositiveNumberIter(start, end));
		System.out.println(addPositiveNumber(start, end));
	}
}
