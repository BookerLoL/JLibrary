import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;

/* 
 * Working ArrayList Implementation
 * 
 * Modifying the list will cause the iterator to have side effects, better to have a list to collect and then remove after exhausting the iterator or use streams
 */
public class ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int CAPACITY_MULTIPLIER = 2;
	private E[] ary;
	private int size;

	public ArrayList() {
		this(DEFAULT_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	public ArrayList(int initialCapacity) {
		super();
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		}

		ary = (E[]) new Object[initialCapacity];
	}

	public ArrayList(Collection<? extends E> c) {
		this(c.size());
		addAll(c);
	}

	public boolean add(E data) {
		ensureCapacity(size + 1);
		ary[size++] = data;
		return true;
	}

	public void add(int index, E data) {
		checkBoundsExclusive(index);
		ensureCapacity(size + 1);
		System.arraycopy(ary, index, ary, index + 1, size - index);
		ary[index] = data;
		size++;
	}

	private void ensureCapacity(int newSize) {
		if (newSize > ary.length) {
			ary = Arrays.copyOf(ary, size * CAPACITY_MULTIPLIER);
		}
	}

	public E remove(int index) {
		checkBoundsInclusive(index);
		E prev = ary[index];

		int itemsToMove = size - index - 1;
		if (itemsToMove > 0) {
			System.arraycopy(ary, index + 1, ary, index, itemsToMove);
		}

		ary[--size] = null;
		return prev;
	}

	public int indexOf(Object data) {
		if (data == null) {
			for (int i = 0; i < size; i++) {
				if (ary[i] == data) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				if (data.equals(ary[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	public E get(int index) {
		checkBoundsInclusive(index);
		return ary[index];
	}

	public E set(int index, E newData) {
		checkBoundsInclusive(index);
		E prev = ary[index];
		ary[index] = newData;
		return prev;
	}

	private void checkBoundsInclusive(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
	}

	private void checkBoundsExclusive(int index) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
	}

	public void clear() {
		for (int i = 0; i < size; i++) {
			ary[i] = null;
		}
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new ALIter(0);
	}

	public Iterator<E> iterator(int index) {
		checkBoundsExclusive(index);
		return new ALIter(index);
	}

	private class ALIter implements Iterator<E> {
		int index;
		int prevRet = -1;

		public ALIter(int startIndex) {
			if (startIndex < 0 || startIndex >= size) {
				throw new IllegalArgumentException("Index should be betweeen 0 and size() -1");
			}
			index = startIndex;
		}

		public boolean hasNext() {
			return index != size;
		}

		public E next() {
			if (index >= size) {
				throw new NoSuchElementException();
			}

			prevRet = index;
			return ary[index++];
		}

		public void remove() {
			if (prevRet < 0) {
				throw new IllegalStateException();
			}

			ArrayList.this.remove(prevRet);
			index = prevRet;
			prevRet = -1;
		}
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(ary, size);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		if (a.length < size) {
			return (T[]) Arrays.copyOf(ary, size, a.getClass());
		}
		System.arraycopy(ary, 0, a, 0, size);
		return a;
	}

	@Override
	public boolean remove(Object o) {
		int index = indexOf(o);
		if (index >= 0) {
			ignoreChecksRemove(index);
			return true;
		}
		return false;
	}

	private E ignoreChecksRemove(int index) {
		E prev = ary[index];
		int itemsToMove = size - index - 1;
		if (itemsToMove > 0) {
			System.arraycopy(ary, index + 1, ary, index, itemsToMove);
		}

		ary[--size] = null;
		return prev;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object obj : c) {
			if (!contains(obj)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		Object[] a = c.toArray();
		int otherLength = a.length;
		ensureCapacity(otherLength + size);
		System.arraycopy(a, 0, ary, size, otherLength);
		size += otherLength;
		return otherLength != 0;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		Objects.requireNonNull(c);
		return removeHelper(c, true);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		Objects.requireNonNull(c);
		return removeHelper(c, false);
	}

	private boolean removeHelper(Collection<?> c, boolean containFlag) {
		boolean changed = false;
		for (int i = size - 1; i >= 0; i--) {
			if (c.contains(get(i)) == containFlag) {
				remove(i);
				changed = true;
			}
		}
		return changed;
	}

	@SuppressWarnings("unchecked")
	public Object clone() {
		ArrayList<E> clone = null;
		try {
			clone = (ArrayList<E>) super.clone();
			clone.ary = Arrays.copyOf(ary, size);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof List)) {
			return false;
		}

		return o.getClass() == ArrayList.class ? equalArraysList((ArrayList<E>) o) : equalContentList((List<?>) o);
	}

	protected boolean equalArraysList(ArrayList<E> o) {
		if (o.size != size) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			if (!Objects.equals(ary[i], o.ary[i])) {
				return false;
			}
		}
		return true;
	}

	private boolean equalContentList(List<?> o) {
		if (o.size() != size) {
			return false;
		}
		Iterator<?> iter = o.iterator();
		for (int curr = 0; curr != size; curr++) {
			if (!iter.hasNext() || !Objects.equals(ary[curr], iter.next())) {
				return false;
			}
		}
		return !iter.hasNext();
	}
}
