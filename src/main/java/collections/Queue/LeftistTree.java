package main.collections.Queue;
import java.util.Comparator;

/*
 * Leftist Tree
 * 
 * Doesn't really use a bias besides for the data the nodes contain
 * 
 * Used as a priority queue, use comparator to create a min or max queue
 */
public class LeftistTree<T extends Comparable<? super T>> {
	private class TreeNode {
		T data;
		TreeNode left;
		TreeNode right;

		public TreeNode(T data) {
			this.data = data;
			left = null;
			right = null;
		}
	}

	private static final int MAX_CAPACITY = Integer.MAX_VALUE;
	private static final int DEFAULT_CAPACITY = 10;
	private int maxCapacity;
	private int size;
	private TreeNode root;
	private Comparator<T> comp;

	public LeftistTree() {
		this(MAX_CAPACITY, Comparator.naturalOrder());
	}

	public LeftistTree(Comparator<T> comp) {
		this(MAX_CAPACITY, comp);
	}

	public LeftistTree(int maxCapacity, Comparator<T> comp) {
		this.maxCapacity = maxCapacity <= 0 ? DEFAULT_CAPACITY : maxCapacity;
		this.comp = comp == null ? Comparator.naturalOrder() : comp;
		size = 0;
		root = null;
	}

	public void insert(T data) {
		if (!isFull()) {
			root = meld(root, new TreeNode(data));
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

	private TreeNode meld(TreeNode hblt1, TreeNode hblt2) {
		if (hblt1 == null) {
			return hblt2;
		} else if (hblt2 == null) {
			return hblt1;
		}

		TreeNode newRoot = null;
		TreeNode lowerHblt = null;

		if (compare(hblt1, hblt2) >= 0) {
			newRoot = hblt1;
			lowerHblt = hblt2;
		} else {
			newRoot = hblt2;
			lowerHblt = hblt1;
		}

		TreeNode L = newRoot.left;
		TreeNode C = meld(newRoot.right, lowerHblt);

		if (compare(L, C) >= 0) {
			newRoot.left = L;
			newRoot.right = C;
		} else {
			newRoot.left = C;
			newRoot.right = L;
		}

		return newRoot;
	}

	private int compare(TreeNode o1, TreeNode o2) {
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
