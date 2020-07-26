import java.util.Comparator;

public class SkewHeap<T extends Comparable<? super T>> {
	private class Node {
		T data;
		Node left;
		Node right;

		public Node(T data) {
			this.data = data;
			left = null;
			right = null;
		}
	}

	private static final int MAX_CAPACITY = Integer.MAX_VALUE;
	private static final int DEFAULT_CAPACITY = 10;
	private int maxCapacity;
	private int size;
	private Node root;
	private Comparator<T> comp;

	public SkewHeap() {
		this(MAX_CAPACITY, Comparator.naturalOrder());
	}

	public SkewHeap(Comparator<T> comp) {
		this(MAX_CAPACITY, comp);
	}

	public SkewHeap(int maxCapacity, Comparator<T> comp) {
		this.maxCapacity = maxCapacity <= 0 ? DEFAULT_CAPACITY : maxCapacity;
		this.comp = comp == null ? Comparator.naturalOrder() : comp;
		size = 0;
		root = null;
	}

	public void insert(T data) {
		if (!isFull()) {
			root = meld(root, new Node(data));
			size++;
		}
	}

	public T remove() {
		if (root == null) {
			return null;
		}
		T data = root.data;
		root = meld(root.left, root.right);
		size--;
		return data;
	}

	private Node meld(Node skew1, Node skew2) {
		Node merged = mergeRightPath(skew1, skew2);
		swapLeftSide(merged);
		return merged;
	}

	private Node mergeRightPath(Node skew1, Node skew2) {
		if (skew1 == null) {
			return skew2;
		} else if (skew2 == null) {
			return skew1;
		}

		if (compare(skew1, skew2) >= 0) {
			skew1.right = mergeRightPath(skew1.right, skew2);
			return skew1;
		} else {
			skew2.right = mergeRightPath(skew1, skew2.right);
			return skew2;
		}
	}

	private void swapLeftSide(Node skew) {
		if (skew == null) {
			return;
		}

		swapChildren(skew);
		swapLeftSide(skew.left);
	}

	private void swapChildren(Node node) {
		Node temp = node.right;
		node.right = node.left;
		node.left = temp;
	}

	private int compare(Node o1, Node o2) {
		if (o1 == null) {
			return -1;
		} else if (o2 == null) {
			return 1;
		}
		return comp.compare(o1.data, o2.data);
	}

	public T peek() {
		if (root == null) {
			return null;
		}
		return root.data;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public int getCapacity() {
		return maxCapacity;
	}

	public boolean isFull() {
		return size == maxCapacity;
	}
}
