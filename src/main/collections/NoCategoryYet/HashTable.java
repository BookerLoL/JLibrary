package main.collections.NoCategoryYet;
import java.util.LinkedList;

//Reference: Java 9 Data Structures and Algorithms, Debasish Ray Chawdhuri
public class HashTable<T> {
	private LinkedList<T>[] buckets;
	private double maxLoadFactor;
	private int size;

	public HashTable() {
		this(16, 0.75);
	}

	public HashTable(int initCapacity) {
		this(initCapacity, 0.75);
	}

	@SuppressWarnings("unchecked")
	public HashTable(int initCapacity, double maxLoadFactor) {
		buckets = (LinkedList<T>[]) new Object[initCapacity];
		this.maxLoadFactor = maxLoadFactor;
	}

	public boolean insert(T value) {
		boolean inserted = insert(value, buckets.length, buckets);
		if (inserted) {
			rehash();
		}
		return inserted;
	}

	private boolean insert(T value, int length, LinkedList<T>[] array) {
		int hashCode = value.hashCode();
		int bucketIndex = hashCode % length;

		LinkedList<T> bucket = array[bucketIndex];
		if (bucket == null) {
			bucket = new LinkedList<>();
			array[bucketIndex] = bucket;
		}

		for (T elem : bucket) {
			if (elem.equals(value)) {
				return false;
			}
		}

		bucket.add(value);
		size++;
		return true;
	}

	@SuppressWarnings("unchecked")
	private void rehash() {
		double loadFactor = ((double) (size)) / buckets.length;
		if (loadFactor > maxLoadFactor) {
			LinkedList<T>[] newBuckets = (LinkedList<T>[]) new Object[buckets.length * 2];
			size = 0;
			for (LinkedList<T> bucket : buckets) {
				if (bucket != null) {
					for (T elem : bucket) {
						insert(elem, newBuckets.length, newBuckets);
					}
				}
			}
			this.buckets = newBuckets;
		}
	}

	public boolean contains(T value) {
		int hashCode = value.hashCode();
		int bucketIndex = hashCode % buckets.length;

		if (buckets[bucketIndex] != null) {
			for (T elem : buckets[bucketIndex]) {
				if (elem.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	public T search(T value) {
		int hashCode = value.hashCode();
		int bucketIndex = hashCode % buckets.length;

		if (buckets[bucketIndex] != null) {
			for (T elem : buckets[bucketIndex]) {
				if (elem.equals(value)) {
					return elem;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void clear() {
		size = 0;
		buckets = (LinkedList<T>[]) new Object[16];
	}
}
