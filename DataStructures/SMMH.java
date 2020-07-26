import java.util.Arrays;
import java.util.Comparator;

//Symmetric Min-Max Heap
//Reference document: Symmetric Min-Max heap: A simpler data structure for double-ended priority queue 
public class SMMH<T extends Comparable<? super T>> {
	private Comparator<T> comp;
	private T[] heap;
	private int count;
	private boolean isLimited;

	public SMMH(int initialCapacity) {
		this(initialCapacity, Comparator.naturalOrder(), false);
	}

	@SuppressWarnings("unchecked")
	public SMMH(int initialCapacity, Comparator<T> comp, boolean isLimited) {
		initialCapacity = initialCapacity <= 0 ? 11 : initialCapacity;
		heap = (T[]) new Comparable[initialCapacity];
		this.comp = comp;
		this.isLimited = isLimited;
	}

	public void insert(T data) {
		if (isFull()) {
			if (isLimited) {
				throw new IllegalStateException("Queue is full");
			}
			heap = Arrays.copyOf(heap, heap.length * 2);
		}

		count++;
		heap[count] = data;

		int index = count;
		int changed = index;
		do {
			index = changed;
			index = siblingSwap(index);
			changed = grandparentSwap(index);
		} while (changed != index);
	}

	private boolean isLeft(int pos) {
		if (pos < 1) {
			return false;
		}
		return pos % 2 != 0;
	}

	private boolean isRight(int pos) {
		if (pos < 2) {
			return false;
		}
		return pos % 2 == 0;
	}

	private boolean hasLeftSibling(int pos) {
		return pos <= count && isRight(pos) && parent(pos) == parent(pos - 1);
	}

	private boolean hasRightSibling(int pos) {
		return pos + 1 <= count && isLeft(pos) && parent(pos) == parent(pos + 1);
	}

	private int siblingSwap(int index) {
		if (hasLeftSibling(index) && comp.compare(heap[index], heap[index - 1]) < 0) {
			swap(index, index - 1);
			index--;
		} else if (hasRightSibling(index) && comp.compare(heap[index], heap[index + 1]) > 0) {
			swap(index, index + 1);
			index++;
		}
		return index;
	}

	private int grandparentSwap(int index) {
		int lNode = LNode(index);
		int rNode = RNode(index);

		if (index > 2) {
			if (comp.compare(heap[index], heap[lNode]) < 0) {
				swap(index, lNode);
				return lNode;
			} else if (comp.compare(heap[index], heap[rNode]) > 0) {
				swap(index, rNode);
				return rNode;
			}
		}
		return index;
	}

	private boolean isLeaf(int pos) {
		return Math.floor(count / 2.0) + 1 <= pos;
	}

	private int grandchildSwap(int index) {
		if (isLeaf(index)) {
			return index;
		}
		if (isLeft(index)) {
			int leftChild = LChild(index);
			int rightChild = LChild(RChild(parent(index)));
			int c = leftChild;

			if (heap[rightChild] != null && comp.compare(heap[rightChild], heap[leftChild]) < 0) {
				c = rightChild;
			}
			if (heap[c] != null && comp.compare(heap[c], heap[index]) < 0) {
				swap(c, index);
				return c;
			}
		} else {
			int leftChild = RChild(LChild(parent(index)));
			int rightChild = RChild(index);
			int c = rightChild;

			if (heap[rightChild] == null) {
				c = leftChild;
			} else if (comp.compare(heap[leftChild], heap[rightChild]) > 0) {
				c = leftChild;
			}

			if (heap[c] != null && comp.compare(heap[c], heap[index]) > 0) {
				swap(c, index);
				return c;
			}
		}
		return index;
	}

	private int parent(int pos) {
		if (pos <= 0) {
			return 0;
		}

		return (pos - 1) / 2;
	}

	private int LChild(int parentPos) {
		return parentPos * 2 + 1;
	}

	private int RChild(int parentPos) {
		return parentPos * 2 + 2;
	}

	private int LNode(int index) {
		return LChild(parent(parent(index)));
	}

	private int RNode(int index) {
		return RChild(parent(parent(index)));
	}

	private void swap(int pos1, int pos2) {
		T temp = heap[pos1];
		heap[pos1] = heap[pos2];
		heap[pos2] = temp;
	}

	public T getMin() {
		checkEmpty();
		return heap[1];
	}

	public T getMax() {
		checkEmpty();
		return count >= 2 ? heap[2] : heap[1];
	}

	private void checkEmpty() {
		if (count == 0) {
			throw new IllegalStateException("Deque is empty");
		}
	}

	public T removeMin() {
		checkEmpty();

		T min = heap[1];
		heap[1] = heap[count];
		heap[count] = null;
		count--;

		int index1, index2 = 1;
		do {
			index1 = index2;
			siblingSwap(index1);
			index2 = grandchildSwap(index1);
		} while (index1 != index2); // keep going until no change
		return min;
	}

	public T removeMax() {
		checkEmpty();
		if (count == 1) {
			return removeMin();
		}

		T max = heap[2];
		heap[2] = heap[count];
		heap[count] = null;
		count--;
		int index1, index2 = 2;
		do {
			index1 = index2;
			siblingSwap(index1);
			index2 = grandchildSwap(index1);
		} while (index1 != index2);
		return max;
	}

	public int size() {
		return count;
	}

	public boolean isEmpty() {
		return count == 0;
	}

	private boolean isFull() {
		return count == heap.length - 1;
	}

	public String toString() {
		return Arrays.toString(heap);
	}

	@SuppressWarnings("unused")
	private static void testAry2(SMMH<Integer> heap) {
		Integer[] ary = { 2, 80, 8, 60, 4, 50, 12, 20, 10, 16, 14, 30, 6, 40 };
		Arrays.asList(ary).forEach(num -> heap.insert(num));
		while (!heap.isEmpty()) {
			System.out.println(heap.removeMax());
		}
	}

	@SuppressWarnings("unused")
	private static void testAry1(SMMH<Integer> heap) {
		Integer[] ary = { 5, 92, 15, 63, 7, 49, 21, 31, 25, 36, 13, 23, 26, 42 };
		Arrays.asList(ary).forEach(num -> heap.insert(num));
		while (!heap.isEmpty()) {
			System.out.println(heap.removeMin());
			System.out.println(heap.removeMax());
		}
	}
}
