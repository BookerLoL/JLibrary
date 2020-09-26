package main.collections.NoCategoryYet;

import java.util.HashMap;
import java.util.Random;

public class CountMinSketch<T> {
	public static interface Hasher<T> {
		public int hash(T obj);
	}

	private int depth;
	private int width;
	private Hasher<T>[] hashers;
	private int[][] frequencyMatrix;

	@SafeVarargs
	public CountMinSketch(int width, Hasher<T>... hashers) {
		this.width = width;
		this.hashers = hashers;
		depth = hashers.length;
		frequencyMatrix = new int[depth][width];
	}

	public void update(T item, int count) {
		for (int row = 0; row < depth; row++) {
			frequencyMatrix[row][boundHashCode(hashers[row].hash(item))] += count;
		}
	}

	public int estimate(T item) {
		int count = Integer.MAX_VALUE;
		for (int row = 0; row < depth; row++) {
			count = Math.min(count, frequencyMatrix[row][boundHashCode(hashers[row].hash(item))]);
		}
		return count;
	}

	private int boundHashCode(int hashCode) {
		return hashCode % width;
	}

	public static void main(String[] args) {
		CountMinSketch.Hasher<Integer> hasher1 = number -> number;
		CountMinSketch.Hasher<Integer> hasher2 = number -> {
			String strForm = String.valueOf(number);
			int hashVal = 0;
			for (int i = 0; i < strForm.length(); i++) {
				hashVal = strForm.charAt(i) + (31 * hashVal);
			}
			return hashVal;
		};
		CountMinSketch.Hasher<Integer> hasher3 = number -> {
			number ^= (number << 13);
			number ^= (number >> 17);
			number ^= (number << 5);
			return Math.abs(number);
		};
		CountMinSketch.Hasher<Integer> hasher4 = number -> String.valueOf(number).hashCode();

		int numberOfBuckets = 16;
		CountMinSketch<Integer> cms = new CountMinSketch<>(numberOfBuckets, hasher1, hasher2, hasher3, hasher4);
		Random rand = new Random();
		HashMap<Integer, Integer> freqCount = new HashMap<>();
		int maxIncrement = 10;
		int maxNumber = 1000;
		int iterations = 50;
		for (int i = 0; i < iterations; i++) {
			int increment = rand.nextInt(maxIncrement) + 1;
			int number = rand.nextInt(maxNumber);

			freqCount.compute(number, (k, v) -> v == null ? increment : v + increment);
			cms.update(number, increment);
		}

		for (Integer key : freqCount.keySet()) {
			System.out.println("For key: " + key + "\t real count: " + freqCount.get(key) + "\t estimated count: "
					+ cms.estimate(key));
		}
	}
}
