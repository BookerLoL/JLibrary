import java.util.AbstractList;
import java.util.Arrays;

/**
 * 
 * @author Ethan 
 *
 * @param <T>
 * 
 * 
 * This class does not check bounds or have proper documentation
 
 */
public class Vector<T> extends AbstractList<T> implements Cloneable {
	public static final int DEFAULT_INITIAL_CAPACITY = 10;
	private T[] elements;
	private int elementsCount;

	public Vector() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	public Vector(int initialCapacity) {
		elements = (T[]) new Object[initialCapacity];
		elementsCount = 0;
	}

	@Override
	public int size() {
		return elementsCount;
	}

	public boolean isEmpty() {
		return elementsCount == 0;
	}
	
	private boolean isFull() {
		return elementsCount == elements.length;
	}
	
	private boolean isLow() {
		return elements.length / 4 > elementsCount;
	}

	public int indexOf(Object obj) {
		for (int i = 0; i < elementsCount; i++) {
			if (elements[i].equals(obj)) {
				return i;
			}
		}
		return -1;
	}
	
	@SuppressWarnings("unchecked")
	private void increaseCapacity() {
		T[] doubleStorage = (T[]) new Object[elements.length * 2];
		System.arraycopy(elements, 0, doubleStorage, 0, elementsCount);
		elements = doubleStorage;
	}
	
	@SuppressWarnings("unchecked")
	private void decreaseCapacity() {
		T[] halfStorage = (T[]) new Object[elements.length / 2];
		System.arraycopy(elements, 0, halfStorage, 0, elementsCount);
		elements = halfStorage;
	}

	@Override
	public T get(int index) {
		return elements[index];
	}

	public boolean add(T obj) {
		add(elementsCount, obj);
		return true;
	}

	public void add(int index, T obj) {
		if (isFull()) {
			increaseCapacity();
		}
		shiftElementsRight(index);
		elements[index] = obj;
		elementsCount++;
	}
	
	private void shiftElementsRight(int lastShiftAtIndex) {
		for (int i = elementsCount; i > lastShiftAtIndex; i--) {
			elements[i] = elements[i - 1];
		}
	}
	
	private void shiftElementsLeft(int lastShiftAtIndex) {
		for (int i = lastShiftAtIndex; i < elementsCount - 1; i++) {
			elements[i] = elements[i + 1];
		}
		elements[elementsCount] = null;
	}

	public boolean remove(Object obj) {
		int idx = indexOf(obj);
		remove(idx);
		return true;
	}

	public T remove(int index) {
		T prev = elements[index];
		elements[index] = null;
		shiftElementsLeft(index);
		elementsCount--;
		
		if (isLow()) {
			decreaseCapacity();
		}
		return prev;
	}

	public T set(int index, T obj) {
		T prev = elements[index];
		elements[index] = obj;
		return prev;
	}
	
	public boolean contains(Object obj) {
		return indexOf(obj) != -1;
	}
	
	public String toString() {
		return Arrays.toString(elements);
	}
}
