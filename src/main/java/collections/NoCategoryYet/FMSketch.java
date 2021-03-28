package main.collections.NoCategoryYet;

import java.util.Arrays;
import java.util.Collection;

//Also known as a Simple Counter
public class FMSketch<T> {
	public interface Hasher<T> {
		long hash(T obj);
	}

	private int[] bits;
	private Hasher<T> hasher;

	public FMSketch(Hasher<T> hasher) {
		this(32, hasher); // 32 for long
	}

	private FMSketch(int numBits, Hasher<T> hasher) {
		bits = new int[numBits];
		this.hasher = hasher;
	}

	private static int rank(long hashcode) {
		return findLeftmostOneBit(addPadding(Long.toBinaryString(hashcode)));
	}

	private static String addPadding(String binaryRepresentation) {
		return "0".repeat(32 - binaryRepresentation.length()) + binaryRepresentation;
	}

	private static int findLeftmostOneBit(String binaryStr) {
		return binaryStr.indexOf('1');
	}

	private int findLeftmostZeroBit() {
		for (int i = 0; i < bits.length; i++) {
			if (bits[i] == 0) {
				return i;
			}
		}
		return 32;
	}

	public void add(T object) {
		String bitStr = addPadding(Long.toBinaryString(hasher.hash(object)));
		int rank = findLeftmostOneBit(bitStr);

		if (rank == -1) { // zero case
			rank = bits.length - 1;
		}

		if (bits[rank] == 0) {
			bits[rank] = 1;
		}
	}

	public void add(T object, long value) {
		int rank = rank(value);
		System.out.println(rank);
		if (rank == -1) { // zero case
			rank = bits.length - 1;
		}

		if (bits[rank] == 0) {
			bits[rank] = 1;
		}
	}

	public double cardinality() {
		return (1 / 0.77351) * Math.pow(2, findLeftmostZeroBit());
	}

	// Assert that they use the same hash function
	// There are corrected formulas such as Bjorn Scheurermann 2007
	public static <T> double pcsaCardinality(Collection<T> dataset, FMSketch<T>... counters) {
		Hasher<T> hasher = counters[0].hasher;
		int totalCounters = counters.length;
		for (T data : dataset) {
			int counterIndex = (int) hasher.hash(data) % totalCounters;
			int boundedHash = (int) hasher.hash(data) / totalCounters;
			int rank = rank(boundedHash);

			if (counters[counterIndex].bits[rank] == 0) {
				counters[counterIndex].bits[rank] = 1;
			}
		}

		int leftMostZeroSum = 0;
		for (FMSketch<T> counter : counters) {
			leftMostZeroSum += counter.findLeftmostZeroBit();
		}

		return (totalCounters / 0.77351) * Math.pow(2, (leftMostZeroSum) / (double) totalCounters);
	}

	public int[] getBits() {
		return bits;
	}

	public String toString() {
		return Arrays.toString(bits);
	}
}
