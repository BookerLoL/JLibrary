package main.collections.Tree;
import java.util.AbstractList;

public class HashArrayTree<T> extends AbstractList<T> {
	private static final int LOW_SPACE_MULTIPLIER = 8;
	private static final int MIN_ARRAY_SIZE = 2;
	private T[][] hashAry;
	private int size;
	private int currMaxSpace;

	@SuppressWarnings("unchecked")
	public HashArrayTree() {
		hashAry = (T[][]) new Object[MIN_ARRAY_SIZE][];
		size = 0;
		currMaxSpace = hashAry.length * hashAry.length;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	private boolean isFull() {
		return size() == currMaxSpace;
	}

	/*
	 * 1/LOW_SPACE_MULTIPLIER total capacity
	 */
	private boolean isLow() {
		return size() * LOW_SPACE_MULTIPLIER <= currMaxSpace;
	}

	@SuppressWarnings("unchecked")
	private void increaseSpace() {
		T[][] newHashAry = (T[][]) new Object[hashAry.length * 2][];

		for (int i = 0; i < hashAry.length; i += 2) {
			newHashAry[i / 2] = (T[]) new Object[newHashAry.length];

			System.arraycopy(hashAry[i], 0, newHashAry[i / 2], 0, hashAry.length);
			System.arraycopy(hashAry[i + 1], 0, newHashAry[i / 2], hashAry.length, hashAry.length);

			hashAry[i] = null;
			hashAry[i + 1] = null;
		}

		hashAry = newHashAry;
		currMaxSpace = hashAry.length * hashAry.length;
	}

	private int calcAryIndexOffset(int index) {
		return index / hashAry.length;
	}

	private int calcSubAryIndex(int index) {
		return index % hashAry.length;
	}

	@SuppressWarnings("unchecked")
	private void decreaseSpace() {
		if (hashAry.length == MIN_ARRAY_SIZE) {
			return;
		}

		T[][] newHashAry = (T[][]) new Object[hashAry.length / 2][];

		for (int i = 0; i < hashAry.length / 2; i++) {
			newHashAry[i] = (T[]) new Object[newHashAry.length];

			boolean upperHalf = i % 2 == 0;

			System.arraycopy(hashAry[i / 2], upperHalf ? 0 : newHashAry.length, newHashAry[i], 0, newHashAry.length);

			if (!upperHalf) {
				hashAry[i / 2] = null;
			}
		}

		hashAry = newHashAry;
		currMaxSpace = hashAry.length * hashAry.length;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean add(T e) {
		if (isFull()) {
			increaseSpace();
		}

		int topAryOffset = this.calcAryIndexOffset(size());
		int subAryIndex = this.calcSubAryIndex(size());

		if (hashAry[topAryOffset] == null) {
			hashAry[topAryOffset] = (T[]) new Object[hashAry.length];
		}

		hashAry[topAryOffset][subAryIndex] = e;
		size++;

		return true;
	}

	private boolean isInvalidIndex(int index) {
		return index < 0 || index >= size();
	}

	@Override
	public T get(int index) {
		return hashAry[calcAryIndexOffset(index)][calcSubAryIndex(index)];
	}

	@Override
	public T set(int index, T element) {
		if (isInvalidIndex(index)) {
			throw new IndexOutOfBoundsException();
		}
		int topAryOffset = this.calcAryIndexOffset(index);
		int subAryIndex = this.calcSubAryIndex(index);

		T elem = hashAry[topAryOffset][subAryIndex];
		hashAry[topAryOffset][subAryIndex] = element;
		return elem;
	}

	@Override
	public void add(int index, T element) {
		if (isInvalidArrayIndex(index)) {
			throw new IndexOutOfBoundsException();
		}

		add(null);

		for (int i = size(); i > index; i--) {
			set(i, get(i - 1));
		}

		set(index, element);
	}

	private boolean isInvalidArrayIndex(int index) {
		return index < 0 || index > size;
	}

	@Override
	public T remove(int index) {
		if (isInvalidIndex(index)) {
			throw new IndexOutOfBoundsException();
		}
		T elem = get(index);

		for (int i = index + 1; i < size(); i++) {
			set(i - 1, get(i));
		}
		set(size() - 1, null);

		size--;

		if (isLow()) {
			decreaseSpace();
		} else if (size() % hashAry.length == 0) {
			hashAry[calcAryIndexOffset(size())] = null;
		}

		return elem;
	}

	@Override
	public int indexOf(Object o) {
		for (int i = 0; i < size(); i++) {
			if (get(i).equals(o)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		for (int i = size() - 1; i >= 0; i--) {
			if (get(i).equals(o)) {
				return i;
			}
		}
		return -1;
	}

	public void printArray() {
		int i = 0, j;
		while (i < hashAry.length && hashAry[i] != null) {
			j = 0;

			while (j < hashAry[i].length) {
				System.out.print(hashAry[i][j] + " ");
				j++;
			}

			i++;
		}
		System.out.println();
	}
}
