package main.optimizing;

import java.math.BigInteger;

/**
 * This class is designed to show you an example of how to optimize code in
 * general by using primivites over the wrapper classes.
 * 
 * <p>
 * Wrapper classes uses boxing mechanics, so primitive is much better to reduce
 * memory and improve speed.
 * 
 * Avoid using BitInteger and BitDecimal unless you have to use them due to
 * range of values. They require a lot more time to perform calculations and
 * require more memory.
 * 
 * 
 * @author Ethan
 * @version 1.0
 */
public class Primitives {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// Should just use a long if the value won't exceed long limit
		long x = 10;
		BigInteger bigX = BigInteger.valueOf(10);

		int fasterInt = 1;
		Integer slowInt = 1;
	}

}
