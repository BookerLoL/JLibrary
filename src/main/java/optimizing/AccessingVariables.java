package main.optimizing;

/**
 * This class is designed to show you an example of how to optimize code in
 * general by accessing variables.
 * 
 * <p>
 * Accessing variables will be faster than get/set methods
 * 
 * 
 * @author Ethan
 * @version 1.0
 */
public class AccessingVariables {
	public static class A {
		protected final static int VALUE = 10;

		public static int getValue() {
			return VALUE;
		}
	}

	public static class B {
		public static int getAValue() {
			return A.VALUE;
		}
	}

	public static class C {
		public static int getAValue() {
			return A.getValue();
		}
	}

	public static void main(String[] args) {
		System.out.println(B.getAValue() + "\t" + C.getAValue());
	}
}
