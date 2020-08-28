package utility;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Shuffler {
	//Kind of just moves most of the right half of the array towards the front
	public static void riffleShuffle(Object[] ary) {
		if (ary.length < 2) {
			return;
		}
		for (int i = ary.length / 2, j = ary.length - 1; i > 0; i--, j--) {
			swap(ary, i, j);
		}
	}

	public static void interleavedRiffleShuffle(Object[] ary) {
		if (ary.length < 4) {
			return;
		}

		if (ary.length % 2 != 0) {
			throw new IllegalArgumentException("Ary must be even");
		}

		Queue<Object> q = new LinkedList<>();
		for (int i = 1; i < ary.length / 2; i++) {
			q.add(ary[ary.length - i]);
			q.add(ary[i]);
		}

		for (int i = 1; i < ary.length - 1; i += 2) {
			ary[i] = q.remove();
			ary[i + 1] = q.remove();
		}
	}
	
	public static void fisherYatesShuffle(Object[] ary) {
		Random rand = new Random();
		for (int i = ary.length - 1; i > 0; i--) {
			swap(ary, rand.nextInt(i + 1), i);
		}
	}

	public static void swap(Object[] ary, int i, int j) {
		Object temp = ary[i];
		ary[i] = ary[j];
		ary[j] = temp;
	}
}
