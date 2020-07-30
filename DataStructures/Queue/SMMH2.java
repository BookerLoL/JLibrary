import java.util.Arrays;
import java.util.Comparator;

//Symmetric Min-Max Heap
public class SMMH2<T extends Comparable<T>> {
	Comparator<T> comp;
	T[] heap;
	int count;
	boolean isLimited;

	@SuppressWarnings("unchecked")
	public SMMH2(int initialCapacity) {
		this(initialCapacity, Comparator.naturalOrder(), false);
	}

	@SuppressWarnings("unchecked")
	public SMMH2(int initialCapacity, Comparator<T> comp, boolean isLimited) {
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
			changed = siblingSwap(index);
			index = grandparentSwap(changed);
		} while (changed != index);
	}
	
	private boolean isLeft(int pos) {
		if (pos < 1) {
			return false;
		}
		return pos % 2 == 0;
	}
	
	private boolean isRight(int pos) {
		return !isLeft(pos);
	}

	private boolean hasLeftSibling(int pos) {
		return pos <= count && isRight(pos) && parent(pos) == parent(pos - 1);
	}
	
	private boolean hasRightSibling(int pos) {
		return pos + 1 <= count && isLeft(pos) && parent(pos) == parent(pos + 1);
	}

	private int siblingSwap(int index) {
		if (hasLeftSibling(index) && comp.compare(heap[index - 1], heap[index]) >= 0) {
			swap(index, index - 1);
			index--;
		} else if(hasRightSibling(index) && comp.compare(heap[index + 1], heap[index]) <= 0) {
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
		return (pos * 2) + 1 < count;
	}
	
	private int grandchildSwap(int index) {
		if (isLeaf(index)) {
			return index;
		}
		if (isLeft(index)) {
			int leftChild = LChild(index);
			int rightChild = RChild(index);
			int c = leftChild;
			if (heap[rightChild] != null && comp.compare(heap[rightChild], heap[leftChild]) < 0) {
				c = rightChild;
			}
			if (heap[leftChild] != null && comp.compare(heap[c], heap[index]) < 0) {
				swap(c, index);
				return c;
			}
		} else {
			int leftChild = LChild(index);
			int rightChild = RChild(index);
			int c = rightChild;
			if (heap[leftChild] != null && comp.compare(heap[leftChild], heap[rightChild]) > 0) {
				c = rightChild;
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

	public T removeMin() {
		if (count == 0) {
			return null;
		}
		T min = heap[1];
		heap[1] = heap[count];
		heap[count] = null;
		count--;

		int y = 1;
		int x = -1;
		do {
			siblingSwap(y);
			x = grandchildSwap(y);
		} while (x != y);
		return min;
	}

	public T removeMax() {
		if (count == 0) {
			return null;
		} else if (count == 1) {
			return removeMin();
		}
		
		T max = heap[2];
		heap[2] = heap[count];
		heap[count] = null;
		count--;
		int y = 2;
		int x = -1;
		do {
			siblingSwap(y);
			x = grandchildSwap(y);
		} while (x != y);
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

	public static void main(String[] args) {
		SMMH2<Integer> heap = new SMMH2<Integer>(10);
		System.out.println(11 / 2 / 2);
		Integer[] ary = { 5, 92, 15, 63, 7, 49, 21, 31, 25, 36, 13, 23, 26, 42 };
		Arrays.asList(ary).forEach(num -> heap.insert(num));
		System.out.println(heap);
	}
}
