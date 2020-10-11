package main.optimizing;

/**
 * This class is designed to show you an example of how to optimize code in
 * general by using switch cases.
 * 
 * <p>
 * Switch cases are more optimized than if-else statements.
 * 
 * 
 * @author Ethan
 * @version 1.0
 */
public class SwitchCases {
	public static void main(String[] args) {
		String str = "testing";

		switch (str) {
		case "test":
			System.out.println("test is found");
			break;
		case "testing":
			System.out.println("testing is found");
			break;
		default:
			System.out.println("Didn't find what we wanted");
		}
	}
}
