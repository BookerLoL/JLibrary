package main.collections.Queue;
import java.util.Arrays;
import java.util.Comparator;

/*
 * Min-Max Heap that uses alternating layers
 * 
 * Reference document: https://en.wikipedia.org/wiki/Min-max_heap
 * 
 */
public class MMH<T extends Comparable<? super T>> {
	private Comparator<T> comp;
	private T[] heap;
	private int size;

	public MMH() {
		this(10, Comparator.naturalOrder());
	}

	public MMH(int initialCapacity) {
		this(initialCapacity, Comparator.naturalOrder());
	}

	public MMH(Comparator<T> comp) {
		this(10, comp);
	}

	@SuppressWarnings("unchecked")
	public MMH(int initialCapacity, Comparator<T> comp) {
		heap = (T[]) new Comparable[initialCapacity + 1];
		this.comp = comp != null ? comp : Comparator.naturalOrder();
	}

	public void checkEmpty() {
		if (isEmpty()) {
			throw new IllegalStateException("Deque is empty");
		}
	}

	public T getMin() {
		checkEmpty();
		return heap[1];
	}

	public T getMax() {
		checkEmpty();
		if (size <= 2) {
			return size == 1 ? heap[1] : heap[2];
		}
		return comp.compare(heap[2], heap[3]) > 0 ? heap[2] : heap[3];
	}

	public void insert(T data) {
		if (size == heap.length - 1) {
			heap = Arrays.copyOf(heap, heap.length * 2);
		}
		
		heap[++size] = data;

		if (size < 2) {
			return;
		}

		int current = size;
		int parent = getParent(current);
		int compValue = comp.compare(heap[current], heap[parent]);

		if (compValue < 0) {
			if (isMinNode(parent)) {
				swap(current, parent);
				trickleUpMinPath(parent);
			} else {
				trickleUpMinPath(current);
			}
		} else if (compValue > 0) {
			if (isMaxNode(parent)) {
				swap(current, parent);
				trickleUpMaxPath(parent);
			} else {
				trickleUpMaxPath(current);
			}
		}
	}

	private void trickleUpMinPath(int currMin) {
		while (currMin > 1) {
			int grandparent = getParent(getParent(currMin));

			if (comp.compare(heap[currMin], heap[grandparent]) >= 0) {
				break;
			}

			swap(currMin, grandparent);
			currMin = grandparent;
		}
	}

	private void trickleUpMaxPath(int currMax) {
		while (currMax > 3) {
			int grandparent = getParent(getParent(currMax));

			if (comp.compare(heap[currMax], heap[grandparent]) <= 0) {
				break;
			}
			swap(currMax, grandparent);
			currMax = grandparent;
		}
	}

	private boolean isMinNode(int index) {
		int powerOfTwo = 2;
		boolean isMin = true;
		while (powerOfTwo <= index) {
			powerOfTwo <<= 1; // shift bit to the left for a new power of 2
			isMin = !isMin;
		}
		return isMin;
	}

	private boolean isMaxNode(int index) {
		return !isMinNode(index);
	}

	public T removeMin() {
		T minData = getMin();

		heap[1] = heap[size];
		heap[size--] = null;

		trickleDownMin(1);
		return minData;
	}

	private void trickleDownMin(int currentRoot) {
		while (hasChild(currentRoot)) {
			int minIndex = getMinIndexFromChildren(currentRoot, currentRoot * 8);

			if (minIndex == currentRoot) {
				break;
			}

			if (comp.compare(heap[currentRoot], heap[minIndex]) > 0) {
				if (isChild(minIndex, currentRoot)) {
					swap(minIndex, currentRoot);
					break;
				} else {
					int parentOfGrandChild = getParent(minIndex);
					swap(currentRoot, minIndex);
					if (comp.compare(heap[minIndex], heap[parentOfGrandChild]) > 0) {
						swap(parentOfGrandChild, minIndex);
					}
					currentRoot = minIndex;
				}
			}
		}
	}

	private int getMinIndexFromChildren(int current, int maxLimit) {
		if (current > size) {
			return -1;
		}

		int leftChild = getLChild(current);
		int rightChild = getRChild(current);

		// prevent addition method calls unless possible to call
		leftChild = leftChild < maxLimit ? getMinIndexFromChildren(leftChild, maxLimit) : -1;
		rightChild = rightChild < maxLimit ? getMinIndexFromChildren(rightChild, maxLimit) : -1;

		if (rightChild != -1) {
			int best = comp.compare(heap[leftChild], heap[rightChild]) < 0 ? leftChild : rightChild;
			return comp.compare(heap[best], heap[current]) < 0 ? best : current;
		} else if (leftChild != -1) {
			return comp.compare(heap[leftChild], heap[current]) < 0 ? leftChild : current;
		}
		return current;
	}

	public T removeMax() {
		checkEmpty();
		if (size == 1) {
			return removeMin();
		} else if (size == 2) {
			T maxData = heap[2];
			heap[size--] = null;
			return maxData;
		}

		int currentRoot = comp.compare(heap[2], heap[3]) > 0 ? 2 : 3;
		T maxData = heap[currentRoot];
		heap[currentRoot] = heap[size];
		heap[size--] = null;

		trickleDownMax(currentRoot);
		return maxData;
	}

	private void trickleDownMax(int currentRoot) {
		while (hasChild(currentRoot)) {
			int maxIndex = getMaxIndexFromChildren(currentRoot, currentRoot * 8);

			if (maxIndex == currentRoot) {
				break;
			}

			if (comp.compare(heap[currentRoot], heap[maxIndex]) < 0) {
				if (isChild(maxIndex, currentRoot)) {
					swap(maxIndex, currentRoot);
					break;
				} else {
					int parentOfGrandChild = getParent(maxIndex);
					swap(currentRoot, maxIndex);
					if (comp.compare(heap[maxIndex], heap[parentOfGrandChild]) > 0) {
						swap(parentOfGrandChild, maxIndex);
					}
					currentRoot = maxIndex;
				}
			}
		}
	}

	private int getMaxIndexFromChildren(int current, int maxLimit) {
		if (current > size) {
			return -1;
		}

		int leftChild = getLChild(current);
		int rightChild = getRChild(current);

		// prevent addition method calls unless possible to call
		leftChild = leftChild < maxLimit ? getMaxIndexFromChildren(leftChild, maxLimit) : -1;
		rightChild = rightChild < maxLimit ? getMaxIndexFromChildren(rightChild, maxLimit) : -1;

		if (rightChild != -1) {
			int best = comp.compare(heap[leftChild], heap[rightChild]) > 0 ? leftChild : rightChild;
			return comp.compare(heap[best], heap[current]) > 0 ? best : current;
		} else if (leftChild != -1) {
			return comp.compare(heap[leftChild], heap[current]) > 0 ? leftChild : current;
		}
		return current;
	}

	private void swap(int index1, int index2) {
		T temp = heap[index1];
		heap[index1] = heap[index2];
		heap[index2] = temp;
	}

	private int getLChild(int index) {
		return index * 2;
	}

	private int getRChild(int index) {
		return index * 2 + 1;
	}

	private boolean hasChild(int index) {
		return index * 2 <= size;
	}

	private boolean isChild(int index, int currentNode) {
		return index < currentNode * 4;
	}

	private int getParent(int index) {
		return index / 2;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public String toString() {
		return Arrays.toString(heap);
	}

	@SuppressWarnings("unused")
	private static void test1() {
		MMH<Integer> heap = new MMH<>(20);
		Integer[] nums = { 5, 30, 26, 10, 16, 20, 18, 12, 14, 25, 21, 22, 2 };
		Arrays.asList(nums).forEach(item -> heap.insert(item));
		System.out.println(heap);
		while (!heap.isEmpty()) {
			System.out.println(heap.removeMax());
		}
	}
	
	@SuppressWarnings("unused")
	private static void test2() {
		MMH<Integer> heap = new MMH<>(20);
		Integer[] nums = { 8, 71, 41, 31, 10, 11, 16, 46, 51, 31, 21, 13};
		Arrays.asList(nums).forEach(item -> heap.insert(item));
		System.out.println(heap);
		while (!heap.isEmpty()) {
			System.out.println(heap.removeMin());
		}
	}
}
