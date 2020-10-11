package main.optimizing;

/**
 * This class is designed to show you an example of how to optimize code in
 * general by unrolling loops.
 * 
 * <p>
 * Do more work within one iteration.
 * 
 * 
 * @author Ethan
 * @version 1.0
 */
public class UnrollingLoops {

	public static void main(String[] args) {
		int length = 300;
		int[] numbers = new int[length];

		if (length % 5 == 0) {
			for (int i = 0; i < length; i += 5) {
				numbers[i] = i;
				numbers[i + 1] = i + 1;
				numbers[i + 2] = i + 2;
				numbers[i + 3] = i + 3;
				numbers[i + 4] = i + 4;
			}
		} else if (length % 3 == 0) {
			for (int i = 0; i < length; i += 3) {
				numbers[i] = i;
				numbers[i + 1] = i + 1;
				numbers[i + 2] = i + 2;
			}
		} else if (length % 2 == 0) {
			for (int i = 0; i < length; i += 2) {
				numbers[i] = i;
				numbers[i + 1] = i + 1;
			}
		} else {
			for (int i = 0; i < length; i++) {
				numbers[i] = i;
			}
		}
	}

}
