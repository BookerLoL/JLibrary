package main.collections.NoCategoryYet;

//https://www.interviewcake.com/concept/java/bloom-filter
//https://en.wikipedia.org/wiki/Counting_Bloom_filter
/**
 * A bloom filter data structure that allows threshold check
 * 
 * @author Ethan
 * @since 1.0
 */
public class BloomFilter<T> {
	public interface Hasher<T> {
		int hash(T item);
	}

	private int capacity;
	private int acceptThreshold;
	private byte[] set;
	private Hasher<T>[] hashers;

	@SafeVarargs
	public BloomFilter(int size, Hasher<T>... hashers) {
		this(size, 1, hashers);
	}

	@SafeVarargs
	public BloomFilter(int size, int acceptThreshold, Hasher<T>... hashers) {
		capacity = size;
		set = new byte[capacity];
		this.acceptThreshold = acceptThreshold;
		this.hashers = hashers;
	}

	public void insert(T item) {
		for (Hasher<T> hasher : hashers) {
			set[hasher.hash(item) % capacity]++;
		}
	}

	public boolean contains(T item) {
		return contains(item, false);
	}

	public boolean contains(T item, boolean allLessThanThreshold) {
		if (allLessThanThreshold) {
			for (Hasher<T> hasher : hashers) {
				if (set[hasher.hash(item) % capacity] > acceptThreshold) {
					return false;
				}
			}
		} else {
			for (Hasher<T> hasher : hashers) {
				if (set[hasher.hash(item) % capacity] < acceptThreshold) {
					return false;
				}
			}
		}
		return true;
	}
}
