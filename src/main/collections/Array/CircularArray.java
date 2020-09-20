package main.collections.Array;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/* 
 * Working CircularArray Implementation
 * 
 * You must explicitly indicate that you want a limited queue capacity using the two argument constructor with limitedCapacity argument as true
 * 
 * Uses a naive approach to increasing the capacity and the program does not check if the capacity will cause a memory issue due to increasing the capacity beyond the array limit length.
 * 
 * Modifying the CircularArray will cause the iterator to have side effects
 */
public class CircularArray<E> extends AbstractQueue<E> implements List<E>, RandomAccess, Cloneable {
	protected static final int DEFAULT_CAPACITY = 10;
	private static final int CAPACITY_MULTIPLIER = 2;
	private E[] ary;
	private int capacity;
	private boolean isLimited;
	private int count;
	private int front;
	private int end;

	public CircularArray() {
		this(DEFAULT_CAPACITY, false);
	}

	public CircularArray(int initialCapacity) {
		this(initialCapacity, false);
	}

	@SuppressWarnings("unchecked")
	public CircularArray(int initialCapacity, boolean limitCapacity) {
		super();
		if (initialCapacity <= 0) {
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		}
		isLimited = limitCapacity;
		capacity = initialCapacity;
		ary = (E[]) new Object[capacity];
	}

	@Override
	public boolean offer(E e) {
		if (isFull()) {
			if (isLimited) {
				return false;
			}
		}

		count++;
		ary[end] = e;

		if (!isLimited) {
			ensureCapacity(count + 1);
		}

		end = (end + 1) % capacity;
		return true;
	}

	@Override
	public boolean add(E e) {
		if (isFull()) {
			throw new IllegalStateException("Queue is full");
		}

		count++;
		ary[end] = e;

		if (!isLimited) {
			ensureCapacity(count + 1);
		}
		end = (end + 1) % capacity;
		return true;
	}

	@SuppressWarnings("unchecked")
	private void ensureCapacity(int newSize) {
		if (newSize > capacity) {
			E[] newAry = (E[]) new Object[capacity * CAPACITY_MULTIPLIER];
			if (front <= end) {
				System.arraycopy(ary, front, newAry, front, count);
			} else { // End comes before the start, need to reorganize the position so end can
						// continue to grow and not run into start prematurely
				for (int index = front, indicesLeft = count; indicesLeft != 0; indicesLeft--, index = (index + 1)
						% capacity) {
					newAry[count - indicesLeft] = ary[index];
				}
				front = 0;
				end = count - 1;
			}
			ary = newAry;
			capacity = ary.length;
		}
	}

	@Override
	public E poll() {
		if (isEmpty()) {
			return null;
		}
		E data = ary[front];
		ary[front] = null;
		front = (front + 1) % capacity;
		count--;
		return data;
	}

	@Override
	public E remove() {
		if (isEmpty()) {
			throw new IllegalStateException("Queue is empty");
		}
		E data = ary[front];
		ary[front] = null;
		front = (front + 1) % capacity;
		count--;
		return data;
	}

	@Override
	public E peek() {
		if (isEmpty()) {
			return null;
		}
		return ary[front];
	}

	@Override
	public E element() {
		if (isEmpty()) {
			throw new IllegalStateException("Queue is empty");
		}
		return ary[front];

	}

	protected E peekLast() {
		if (isEmpty()) {
			return null;
		}
		return ary[end];
	}

	protected E getLast() {
		if (isEmpty()) {
			throw new IllegalStateException("Queue is empty");
		}
		return ary[end];
	}

	protected E removeLast() {
		if (isEmpty()) {
			throw new IllegalStateException("Queue is empty");
		}
		E data = ary[end];
		ary[end] = null;
		end = end == 0 ? capacity - 1 : end - 1;
		count--;
		return data;
	}

	protected E pollLast() {
		if (isEmpty()) {
			return null;
		}
		E data = ary[end];
		ary[end] = null;
		end = end == 0 ? capacity - 1 : end - 1;
		count--;
		return data;
	}

	public boolean isEmpty() {
		return count == 0;
	}

	protected boolean isFull() {
		return count == capacity;
	}

	public int capacity() {
		return capacity;
	}
	
	@Override
	public void clear() {
		for (; count != 0; front = (front + 1) % capacity, count--) {
			ary[front] = null;
		}
		front = 0;
		end = 0;
	}

	/*
	 * Although you can compute size, it's not time efficient and many of the
	 * methods rely on an efficient implementation using the size
	 */
	@Override
	public int size() {
		return count;
	}

	public int indexOf(Object o) {
		if (!isEmpty()) {
			if (o == null) {
				for (int index = front, indicesLeft = count; indicesLeft != 0; indicesLeft--, index = (index + 1)
						% capacity) {
					if (ary[index] == o) {
						return index;
					}
				}
			} else {
				for (int index = front, indicesLeft = count; indicesLeft != 0; indicesLeft--, index = (index + 1)
						% capacity) {
					if (o.equals(ary[index])) {
						return index;
					}
				}
			}
		}
		return -1;
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	protected class CircularArrayIterator implements Iterator<E> {
		private int index;
		private int leftAmount;

		public CircularArrayIterator(final int positionOffset, int total) {
			if (total > 0) {
				if (positionOffset < 0 || positionOffset > count) {
					throw new IllegalArgumentException(
							"Illegal offset: " + positionOffset + " . Should be between 0 and size() - 1");
				}

				index = (front + positionOffset) % capacity;
				leftAmount = total >= count ? count : total;
			}
		}

		public boolean hasNext() {
			return leftAmount > 0;
		}

		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			leftAmount--;
			E data = ary[index];
			index = (index + 1) % capacity;
			return data;
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new CircularArrayIterator(0, count);
	}

	public Iterator<E> iterator(int positionOffset) {
		return new CircularArrayIterator(positionOffset, count);
	}

	public Iterator<E> iterator(int positionOffset, int total) {
		return new CircularArrayIterator(positionOffset, total);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean changed = false;
		for (E item : c) {
			if (addAt(index, item)) {
				changed = true;
			} else {
				break;
			}
		}
		return changed;
	}

	private boolean addAt(int index, E element) {
		if (isFull()) {
			if (isLimited) {
				return false;
			}
		}

		checkExclusiveBounds(index);
		count++;
		int trueIndex = getTrueIndex(index);
		if (trueIndex != end) { // No need to shift if inserting at the end of the list
			shiftEndElementsByOne(trueIndex, true);
		}
		ary[trueIndex] = element;

		if (!isLimited) {
			ensureCapacity(count + 1);
		}

		end = (end + 1) % capacity;
		return true;
	}

	private void checkInclusiveBounds(int index) {
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException("Not within 0 and size() - 1");
		}
	}

	private void checkExclusiveBounds(int index) {
		if (index < 0 || index > count) {
			throw new IndexOutOfBoundsException("Not within 0 and size()");
		}
	}

	private int getTrueIndex(int index) {
		return front <= end ? front + index : (front + index) % capacity;
	}

	private void shiftEndElementsByOne(int startIndex, boolean rightDirection) {
		if (rightDirection) {
			int index = end == 0 ? capacity - 1 : end - 1;
			int afterIndex = (index + 1) % capacity;
			for (; index != startIndex; afterIndex = index, index = index == 0 ? capacity - 1 : index - 1) {
				ary[afterIndex] = ary[index];
			}
			ary[afterIndex] = ary[index];
		} else {
			int index = startIndex;
			int afterIndex = (index + 1) % capacity;
			int beforeBoundary = capacity - 1;
			for (; index != end; index = afterIndex, afterIndex = afterIndex == beforeBoundary ? 0 : afterIndex + 1) {
				ary[index] = ary[afterIndex];
			}
		}
	}

	@Override
	public E get(int index) {
		checkInclusiveBounds(index);
		return ary[getTrueIndex(index)];
	}

	@Override
	public E set(int index, E element) {
		checkInclusiveBounds(index);
		int trueIndex = getTrueIndex(index);
		E prev = ary[trueIndex];
		ary[trueIndex] = element;
		return prev;
	}

	@Override
	public void add(int index, E element) {
		addAt(index, element);
	}

	@Override
	public E remove(int index) {
		if (isEmpty()) {
			return null;
		}

		checkInclusiveBounds(index);
		int trueIndex = getTrueIndex(index);
		E prev = null;
		if (trueIndex == front) {
			// Avoid shifting everything, move front instead
			// Could be more efficient by checking half the length to see which side to
			// move, would need to implement a shift right from start
			prev = ary[front];
			ary[front] = null;
			front = (front + 1) % capacity;
		} else {
			prev = ary[trueIndex];
			if (trueIndex != end) {
				shiftEndElementsByOne(trueIndex, false);
			}
			ary[end] = null;
			end = end == 0 ? capacity - 1 : end - 1;
		}
		count--;
		return prev;
	}

	@Override
	public int lastIndexOf(Object o) {
		if (isEmpty()) {
			return -1;
		}

		if (o == null) {
			for (int i = end, leftAmount = count; leftAmount != 0; i = i == 0 ? capacity - 1 : i - 1) {
				if (ary[i] == o) {
					return i;
				}
			}
		} else {
			for (int i = end, leftAmount = count; leftAmount != 0; i = i == 0 ? capacity - 1 : i - 1) {
				if (o.equals(ary[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		if (fromIndex < 0 || toIndex > count || fromIndex > toIndex) {
			throw new IndexOutOfBoundsException();
		}
		List<E> list = new ArrayList<>((fromIndex - toIndex) + 1);
		for (; fromIndex <= toIndex; fromIndex++) {
			list.add(get(fromIndex));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public Object clone() {
		CircularArray<E> clone = null;
		try {
			clone = (CircularArray<E>) super.clone();
			clone.ary = Arrays.copyOf(ary, capacity);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
}
