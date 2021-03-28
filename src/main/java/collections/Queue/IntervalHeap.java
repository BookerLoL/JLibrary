package main.collections.Queue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/*
 * Interval Heap
 */
public class IntervalHeap<T extends Comparable<? super T>> {
	private static class Node<T> {
		T min;
		T max;

		public Node(T min, T max) {
			this.min = min;
			this.max = max;
		}

		public String toString() {
			return "[" + min + "," + max + "]";
		}
	}

	private Comparator<T> comp;
	private ArrayList<Node<T>> heap;
	private int size;

	public IntervalHeap() {
		this(10, Comparator.naturalOrder());
	}

	public IntervalHeap(Comparator<T> comp) {
		this(10, comp);
	}

	public IntervalHeap(int initialCapacity, Comparator<T> comp) {
		heap = new ArrayList<>(initialCapacity / 2);
		heap.add(new Node<>(null, null)); // Adding a dummy node to make math more simple
		this.comp = comp != null ? comp : Comparator.naturalOrder();
	}

	public void insert(T data) {
		if (size % 2 == 0) {
			heap.add(new Node<>(data, null));
		} else {
			Node<T> lastNode = heap.get(getLast());
			if (comp.compare(data, lastNode.min) < 0) {
				lastNode.max = lastNode.min;
				lastNode.min = data;
			} else {
				lastNode.max = data;
			}
		}
		size++;

		if (size > 2) {
			Node<T> parent = heap.get(getParent(getLast()));
			if (comp.compare(data, parent.min) < 0) {
				insertMinHeap();
			} else if (comp.compare(data, parent.max) > 0) {
				insertMaxHeap();
			}
		}
	}

	private void insertMinHeap() {
		int index = getLast();
		Node<T> child = heap.get(index);

		do {
			index = getParent(index);
			Node<T> parent = heap.get(index);
			if (comp.compare(parent.min, child.min) <= 0) { // [A, _] <= [C, _]
				break;
			}

			swapMins(child, parent);
			child = parent;
		} while (index > 1);
	}

	private void insertMaxHeap() {
		int index = getLast();
		Node<T> parent, child = heap.get(index);
		do {
			index = getParent(index);
			parent = heap.get(index);

			if (child.max == null) { // [_ , B] >= [C, null]
				if (comp.compare(parent.max, child.min) >= 0) {
					break;
				}
				swapMinHigh(child, parent);
			} else {
				if (comp.compare(parent.max, child.max) >= 0) { // [_, B] >= [_, D]
					break;
				}
				swapMaxs(child, parent);
			}

			child = parent;
		} while (index > 1);
	}

	public T getMin() {
		if (isEmpty()) {
			throw new IllegalStateException("Deque is empty");
		}

		return heap.get(getFirst()).min;
	}

	public T getMax() {
		if (isEmpty()) {
			throw new IllegalStateException("Deque is empty");
		}

		return size != 1 ? heap.get(getFirst()).max : heap.get(getFirst()).min;
	}

	public T removeMin() {
		T minData = getMin();

		if (size == 1) {
			heap.remove(getFirst());
			size--;
			return minData;
		}

		Node<T> first = heap.get(getFirst());
		Node<T> last = heap.get(getLast());
		first.min = last.min;
		size--;
		if (size % 2 == 0) { // [null, null]
			heap.remove(getLast());
		} else { // [removedValue, B] -> [B, null]
			last.min = last.max;
			last.max = last.min;
		}

		int index = getFirst();
		Node<T> current = first;

		while (hasChild(index)) {
			if (hasChildren(index)) {
				index = comp.compare(heap.get(getLeftChild(index)).min, heap.get(getRightChild(index)).min) < 0
						? getLeftChild(index)
						: getRightChild(index);
			} else {
				index = getLeftChild(index);
			}

			Node<T> child = heap.get(index);
			if (comp.compare(current.min, child.min) < 0) {
				break;
			}

			swapMins(current, child);

			if (child.max != null && comp.compare(child.min, child.max) > 0) {
				swapMinHigh(child, child);
			}

			current = child;
		}
		return minData;
	}

	public T removeMax() {
		T maxData = getMax();
		System.out.println(maxData);

		if (size == 1) {
			heap.remove(getFirst());
			size--;
			return maxData;
		}

		Node<T> first = heap.get(getFirst());
		Node<T> last = heap.get(getLast());
		size--;
		if (size % 2 == 0) {
			first.max = last.min;
			heap.remove(getLast());
		} else {
			first.max = last.max;
			last.max = null;
		}

		int index = getFirst();
		Node<T> current = first;

		while (hasChild(index)) {

			if (hasChildren(index)) {
				Node<T> leftChild = heap.get(getLeftChild(index));
				Node<T> rightChild = heap.get(getRightChild(index));
				if (size % 2 == 1 && getRightChild(index) == getLast()) {
					index = comp.compare(leftChild.max, rightChild.min) > 0 ? getLeftChild(index)
							: getRightChild(index);
				} else {
					index = comp.compare(leftChild.max, rightChild.max) > 0 ? getLeftChild(index)
							: getRightChild(index);
				}
			} else {
				index = getLeftChild(index);
			}

			Node<T> child = heap.get(index);
			if (child.max == null) {
				if (comp.compare(current.max, child.min) >= 0) {
					break;
				}
				swapMinHigh(child, current);
			} else {
				if (comp.compare(current.max, child.max) >= 0) {
					break;
				}
				swapMaxs(child, current);

				if (comp.compare(child.min, child.max) > 0) {
					swapMinHigh(child, child);
				}
			}

			current = child;
		}

		return maxData;
	}

	private boolean hasChild(int parentIndex) { // 1 child
		return parentIndex * 2 < heap.size();
	}

	private boolean hasChildren(int parentIndex) { // 2 children
		return parentIndex * 2 + 1 < heap.size();
	}

	private void swapMins(Node<T> n1, Node<T> n2) {
		T tempMin = n1.min;
		n1.min = n2.min;
		n2.min = tempMin;
	}

	private void swapMaxs(Node<T> n1, Node<T> n2) {
		T tempMax = n1.max;
		n1.max = n2.max;
		n2.max = tempMax;
	}

	private void swapMinHigh(Node<T> minNode, Node<T> maxNode) {
		T temp = minNode.min;
		minNode.min = maxNode.max;
		maxNode.max = temp;
	}

	private int getFirst() {
		return 1;
	}

	private int getLast() {
		return heap.size() - 1;
	}

	private int getParent(int nodeIndex) {
		return nodeIndex / 2;
	}

	private int getLeftChild(int parentIndex) {
		return parentIndex * 2;
	}

	private int getRightChild(int parentIndex) {
		return parentIndex * 2 + 1;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < heap.size(); i++) {
			sb.append("Node: " + i + ", data: " + heap.get(i) + "\n");
		}
		return sb.toString();
	}

	/*
	 * Given an array of numbers, and a min and max range to search for. Count how
	 * many points are not within the range
	 */
	public static int complementaryRangeSearch(int[] numbers, int minRange, int maxRange) {
		IntervalHeap<Integer> interval = new IntervalHeap<>();
		for (int i = 0; i < numbers.length; i++) {
			interval.insert(numbers[i]);
		}

		if (interval.isEmpty()) {
			return 0;
		}
		return checkWithinRange(minRange, maxRange, interval, interval.getFirst());
	}

	private static int checkWithinRange(int minRange, int maxRange, IntervalHeap<Integer> heap, int subtree) {
		if (subtree >= heap.heap.size()) {
			return 0;
		}
		Node<Integer> node = heap.heap.get(subtree);
		if (minRange <= node.min && node.max <= maxRange) {
			return 0;
		}

		int outOfRange = 0;
		if (node.min != null && node.min < minRange) {
			outOfRange++;
		}
		if (node.max != null && node.max > maxRange) {
			outOfRange++;
		}
		return outOfRange + checkWithinRange(minRange, maxRange, heap, heap.getLeftChild(subtree))
				+ checkWithinRange(minRange, maxRange, heap, heap.getRightChild(subtree));
	}

	@SuppressWarnings("unused")
	private static void testIntervalHeap() {
		IntervalHeap<Integer> heap = new IntervalHeap<>();
		Integer[] nums = { 2, 30, 3, 17, 4, 15, 4, 12, 3, 11, 5, 10, 6, 15, 4, 10, 5, 11, 5, 9, 4, 7, 8, 8, 7, 9 };
		Arrays.asList(nums).forEach(n -> heap.insert(n));
		heap.insert(40);
		System.out.println(heap);
		while (!heap.isEmpty()) {
			System.out.println(heap.removeMax());
		}
	}

	@SuppressWarnings("unused")
	private static void testComplementaryRangeSearch() {
		int[] numbers = { 3, 4, 5, 6, 8, 12 };
		System.out.println(complementaryRangeSearch(numbers, 5, 7));
	}

	public static void main(String[] args) {
		//testIntervalHeap();
		//testComplementaryRangeSearch()
	}
}
