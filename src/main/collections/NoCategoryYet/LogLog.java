package main.collections.NoCategoryYet;

import java.util.Arrays;
import java.util.Collection;

public class LogLog<T> {
	public interface Hasher<T> {
		long hash(T obj);
	}

	private int[] counter;
	private Hasher<T> hasher;
	private int neededBucketBits;
	private static final double GAMMA_VALUE = 0.39701;

	public LogLog(Hasher<T> hasher, int numberOfCounters) {
		this.hasher = hasher;
		counter = new int[numberOfCounters];
		neededBucketBits = Integer.toString(numberOfCounters, 2).length();
	}

	@SuppressWarnings("unchecked")
	public void add(T... dataset) {
		add(Arrays.asList(dataset));
	}

	public void add(Collection<T> dataset) {
		for (T data : dataset) {
			String binaryForm = getBinaryForm(hasher.hash(data));
			int bucketIndex = Integer.parseInt(binaryForm.substring(0, neededBucketBits), 2) % counter.length;
			int rank = rank(binaryForm.substring(neededBucketBits));
			counter[bucketIndex] = Math.max(counter[bucketIndex], rank);
		}
	}

	private static int rank(String binaryStr) {
		int rank = findLeftmostOneBit(binaryStr);
		return rank != -1 ? rank : binaryStr.length();
	}

	private static String getBinaryForm(long hashcode) {
		return addPadding(Long.toString(hashcode, 2));
	}

	private static String addPadding(String binaryRepresentation) {
		return "0".repeat(64 - binaryRepresentation.length()) + binaryRepresentation;
	}

	private static int findLeftmostOneBit(String binaryStr) {
		return binaryStr.indexOf('1');
	}

	private double averageCount() {
		int sum = 0;
		for (int count : counter) {
			sum += count;
		}
		return sum / (double) counter.length;
	}

	public double cardinality() {
		return GAMMA_VALUE * counter.length * Math.pow(2, averageCount());
	}
	
	public static void main(String[] arsgs) {
		System.out.println(Long.toString(Long.MAX_VALUE, 2));
	}
}
