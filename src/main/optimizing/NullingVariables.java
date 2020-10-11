package main.optimizing;

import java.util.Arrays;

/**
 * This class is designed to show you an example of how to optimize code in
 * general by nulling variables.
 * 
 * <p>
 * Nulling variables will help the GC detect whether to remove an object from
 * memory. This technique is found often in arrays, such as
 * {@link Java.util.ArrayList}
 * 
 * 
 * @author Ethan
 * @version 1.0
 */
public class NullingVariables {

	public static void main(String[] args) {
		Integer[] numbers = new Integer[10];
		Arrays.fill(numbers, 10);

		numbers[numbers.length - 1] = null;
	}

}
