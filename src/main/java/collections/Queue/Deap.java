package main.collections.Queue;
import java.util.Arrays;
import java.util.Comparator;

public class Deap<T extends Comparable<? super T>> {
	private Comparator<T> comp;
	private T[] heap;
	private int size;

	public Deap() {
		this(10, Comparator.naturalOrder());
	}

	public Deap(int initialCapacity) {
		this(initialCapacity, Comparator.naturalOrder());
	}

	@SuppressWarnings("unchecked")
	public Deap(int initialCapacity, Comparator<T> comp) {
		heap = (T[]) new Comparable[initialCapacity + 1];
		this.comp = comp != null ? comp : Comparator.naturalOrder();
	}

	public void insert(T data) {
		if (size == heap.length - 1) {
			heap = Arrays.copyOf(heap, heap.length * 2);
		}
		
		heap[++size] = data;
		if (size < 2) {
			return;
		}

		int currentIndex = size;
		int correspondence = correspondence(currentIndex); // index on the other side of the tree
		if (isRightSide(currentIndex)) {
			if (comp.compare(heap[currentIndex], heap[correspondence]) < 0) {
				swap(currentIndex, correspondence);
				trickleUpMin(correspondence);
			} else {
				trickleUpMax(currentIndex);
			}
		} else {
			if (comp.compare(heap[currentIndex], heap[correspondence]) > 0) {
				swap(currentIndex, correspondence);
				trickleUpMax(correspondence);
			} else {
				trickleUpMin(currentIndex);
			}
		}
	}

	private void trickleUpMin(int currentPos) {
		for (int parent = getParent(currentPos); parent != 0 && comp.compare(heap[parent], heap[currentPos]) > 0; currentPos = parent, parent = getParent(currentPos)) {
			swap(currentPos, parent);
		}
	}

	private void trickleUpMax(int currentPos) {
		for (int parent = getParent(currentPos); parent != 0&& comp.compare(heap[parent], heap[currentPos]) < 0; currentPos = parent, parent = getParent(currentPos)) {
			swap(currentPos, parent);
		}
	}

	public T getMin() {
		checkEmpty();
		return heap[1];
	}

	public T getMax() {
		return size <= 1 ? getMin() : heap[2];
	}

	public T removeMin() {
		T minData = getMin();
		T replacementData = heap[size]; // save this till we reach leaf node
		heap[size--] = null;

		if (size == 0) {
			return minData;
		}

		int nextPos, currentPos = 1;
		while (!isLeaf(currentPos)) { //Walk down the tree
			int leftChild = getLeftChild(currentPos);
			int rightChild = getRightChild(currentPos);
			if (heap[rightChild] != null && comp.compare(heap[leftChild], heap[rightChild]) > 0) {
				nextPos = rightChild;
				heap[currentPos] = heap[nextPos];
			} else {
				nextPos = leftChild;
				heap[currentPos] = heap[nextPos];
			}

			currentPos = nextPos;
		}

		heap[currentPos] = replacementData;

		if (hasEnoughForCorrespondence()) { 
			int correspondence = correspondence(currentPos);
			if (comp.compare(heap[currentPos], heap[correspondence]) > 0) {
				swap(currentPos, correspondence);
				trickleUpMax(correspondence);
			} else {
				trickleUpMin(currentPos);
			}
		}

		return minData;
	}

	public T removeMax() {
		T maxData = getMax();
		T replacementData = heap[size];
		heap[size--] = null;

		if (size <= 1) {
			return maxData;
		}

		int nextPos, currentPos = 2;
		while (!isLeaf(currentPos)) { //Walk down the tree
			int leftChild = getLeftChild(currentPos);
			int rightChild = getRightChild(currentPos);
			if (heap[rightChild] != null && comp.compare(heap[leftChild], heap[rightChild]) < 0) {
				nextPos = rightChild;
				heap[currentPos] = heap[nextPos];
			} else {
				nextPos = leftChild;
				heap[currentPos] = heap[nextPos];
			}

			currentPos = nextPos;
		}

		heap[currentPos] = replacementData;
		
		if (hasEnoughForCorrespondence()) { 
			int correspondence = correspondence(size + 1);
			if (correspondence + 1 <= size) { 
				if (comp.compare(heap[correspondence + 1], heap[correspondence]) > 0) {
					correspondence++; 
				}
			}

			if (isLeftSide(correspondence) && comp.compare(heap[currentPos], heap[correspondence]) < 0) {
				swap(currentPos, correspondence);
				trickleUpMin(correspondence);
			} else {
				trickleUpMax(currentPos);
			}
		}
		return maxData;
	}

	private void checkEmpty() {
		if (size == 0) {
			throw new IllegalStateException("deap is empty");
		}
	}
	
	private boolean hasEnoughForCorrespondence() {
		return size >= 2;
	}

	private void swap(int index1, int index2) {
		T temp = heap[index1];
		heap[index1] = heap[index2];
		heap[index2] = temp;
	}

	private int getParent(int index) {
		return (index - 1) / 2;
	}

	private boolean isLeaf(int index) {
		return getLeftChild(index) > size;
	}

	private int getLeftChild(int index) {
		return index * 2 + 1;
	}

	private int getRightChild(int index) {
		return index * 2 + 2;
	}

	private int correspondence(int index) {
		int offset = 1 << findRow(index);
		if (isLeftSide(index)) {
			if (index + offset > size) { // no right side, go to parents correspondence
				index = getParent(index);
				offset >>= 1;
			}
			return index + offset;
		}
		return index - offset;

	}

	private boolean isRightSide(int index) { // Max subtree
		for (; index > 2; index = getParent(index));
		return index == 2;
	}

	private boolean isLeftSide(int index) { // Min subtree
		for (; index > 2; index = getParent(index));
		return index == 1;
	}

	private int findRow(int index) {
		int row = 1;
		int total = 2;
		for (; total < index; total += (2 << row), row++);
		return row - 1;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public String toString() {
		return "Size: " + size + ", " + Arrays.toString(heap);
	}
}
