import java.util.Arrays;
import java.util.Comparator;

/*
 * Min or Max Heap useful for priority queue
 * 
 * Just modify comparator for min or max
 */
public class MH<T extends Comparable<? super T>> {
	private static final int CAPACITY_MULTIPLIER = 2;
	private static final int DEFAULT_CAPACITY = 10;
	private Comparator<T> comp;
	private int size;
	private boolean canGrow;
	private T[] heap;

	public MH() {
		this(DEFAULT_CAPACITY, Comparator.naturalOrder(), true);
	}

	public MH(int initialCapacity) {
		this(initialCapacity, Comparator.naturalOrder(), true);
	}

	public MH(boolean increaseCapacity) {
		this(DEFAULT_CAPACITY, Comparator.naturalOrder(), increaseCapacity);
	}

	public MH(Comparator<T> comp) {
		this(DEFAULT_CAPACITY, comp, true);
	}

	public MH(int initialCapacity, boolean increaseCapacity) {
		this(initialCapacity, Comparator.naturalOrder(), increaseCapacity);
	}

	public MH(Comparator<T> comp, boolean increaseCapacity) {
		this(DEFAULT_CAPACITY, comp, increaseCapacity);
	}

	@SuppressWarnings("unchecked")
	public MH(int initialCapacity, Comparator<T> comp, boolean increaseCapacity) {
		initialCapacity = initialCapacity <= 0 ? DEFAULT_CAPACITY : initialCapacity;
		this.comp = comp == null ? Comparator.naturalOrder() : comp;
		heap = (T[]) new Comparable[initialCapacity];
		canGrow = increaseCapacity;
		size = 0;
	}

	private int parent(int pos) {
		if (pos == 0) {
			return 0;
		}
		return (pos - 1) / 2;
	}

	private int leftChild(int pos) {
		return (2 * pos) + 1;
	}

	private int rightChild(int pos) {
		return (2 * pos) + 2;
	}

	private void swap(int fromSpot, int toSpot) {
		T tmp = heap[fromSpot];
		heap[fromSpot] = heap[toSpot];
		heap[toSpot] = tmp;
	}

	private void heapifyUp(int pos) {
		int parent = parent(pos);
		if (pos > 0 && comp.compare(heap[parent], heap[pos]) < 0) {
			swap(pos, parent);
			heapifyUp(parent);
		}
	}

	private void heapifyDown(int pos) {
		int left = leftChild(pos);
		int right = rightChild(pos);
		int currLargest = pos;

		if (left < size && comp.compare(heap[left], heap[currLargest]) > 0) {
			currLargest = left;
		}

		if (right < size && comp.compare(heap[right], heap[currLargest]) > 0) {
			currLargest = right;
		}

		if (currLargest != pos) {
			swap(pos, currLargest);
			heapifyDown(currLargest);
		}
	}

	public void add(T data) {
		if (isFull()) {
			if (!isLimitedSize()) {
				increaseCapacity();
			} else {
				return;
			}
		}

		heap[size] = data;
		size++;
		heapifyUp(size - 1);
	}

	public T remove() {
		return removeHelper(0);
	}

	public boolean remove(T item) {
		int index = firstIndexOf(item);
		if (index != -1) {
			removeHelper(index);
			return true;
		}
		return false;
	}

	private int firstIndexOf(T item) {
		int found = -1;
		for (int i = 0; i < size; i++) {
			if (comp.compare(heap[i], item) == 0) {
				found = i;
				break;
			}
		}
		return found;
	}

	private T removeHelper(int index) {
		T val = heap[index];
		size--;
		heap[index] = heap[size];
		heap[size] = null;
		heapifyDown(index);
		return val;
	}

	private void increaseCapacity() {
		heap = Arrays.copyOf(heap, (heap.length * CAPACITY_MULTIPLIER) - 1);
	}

	public int size() {
		return size;
	}

	public boolean isFull() {
		return size == heap.length - 1;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean isLimitedSize() {
		return !canGrow;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < size; i++) {
			sb.append(heap[i] + " ");
		}
		if (sb.length() != 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(']');
		return sb.toString();
	}
}
