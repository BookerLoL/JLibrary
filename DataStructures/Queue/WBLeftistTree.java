import java.util.Comparator;

/*
 * Weight Biased Leftist Tree, WBLT
 * 
 * - Weight is determined by the numbers in the left and the right child node and the node itself
 * 		- in other words: 1 + left.Weight + right.Weight
 * 
 * Used as a priority queue, use comparator to create a min or max queue
 * 
 * max WBLT == max Tree, min WBLT == min tree
 * 
 * Each node value is >= it's children using comparator
 * 
 * every internal nodes child's left >= right 
 */
public class WBLeftistTree<T extends Comparable<T>> {
	private class TreeNode {
		T data;
		TreeNode left;
		TreeNode right;
		int weight;

		public TreeNode(T data) {
			this.data = data;
			left = null;
			right = null;
			weight = 1;
		}

		public String toString() {
			return "s val: " + weight + " , data: " + data.toString();
		}
	}

	public static final int MAX_CAPACITY = Integer.MAX_VALUE;
	public static final int DEFAULT_CAPACITY = 10;
	private int maxCapacity;
	private int size;
	private TreeNode root;
	private Comparator<T> comp;

	public WBLeftistTree() {
		this(MAX_CAPACITY, Comparator.naturalOrder());
	}

	public WBLeftistTree(Comparator<T> comp) {
		this(MAX_CAPACITY, comp);
	}

	public WBLeftistTree(int maxCapacity, Comparator<T> comp) {
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
		TreeNode R = meld(newRoot.right, lowerHblt);

		if (L == null) {
			newRoot.left = R;
			newRoot.right = L;
			newRoot.weight = R.weight + 1;
		} else {
			if (L.weight < R.weight) {
				newRoot.left = R;
				newRoot.right = L;
			} else {
				newRoot.left = L;
				newRoot.right = R;
			}
			newRoot.weight = L.weight + 1 + R.weight;
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

	public boolean isFull() {
		return size == maxCapacity;
	}

	public int getCapacity() {
		return maxCapacity;
	}
}
