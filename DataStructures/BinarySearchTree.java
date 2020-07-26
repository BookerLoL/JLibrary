import java.util.Arrays;
import java.util.Comparator;

public class BinarySearchTree<T extends Comparable<T>> {
	private class TreeNode {
		protected T data;
		protected TreeNode left;
		protected TreeNode right;
		protected int count;

		public TreeNode(T data) {
			this(data, null, null, 1);
		}

		public TreeNode(T data, TreeNode left, TreeNode right, int count) {
			this.data = data;
			this.left = left;
			this.right = right;
			this.count = count;
		}

		public String toString() {
			return "(" + data + ", count: " + count + ")";
		}
	}

	protected static final int MAX_COUNT = Integer.MAX_VALUE;
	private Comparator<T> comp;
	private TreeNode root;
	private int maxCount;

	public BinarySearchTree() {
		this(MAX_COUNT, Comparator.naturalOrder());
	}

	public BinarySearchTree(Comparator<T> comparator) {
		this(MAX_COUNT, comparator);
	}

	public BinarySearchTree(int maxCount) {
		this(maxCount, Comparator.naturalOrder());
	}

	public BinarySearchTree(int maxCount, Comparator<T> comparator) {
		maxCount = maxCount <= 0 ? Integer.MAX_VALUE : maxCount;
		comp = comparator == null ? Comparator.naturalOrder() : comparator;
		root = null;
	}

	public void insert(T data) {
		root = insert(root, data);
	}

	private TreeNode insert(TreeNode current, T data) {
		if (current == null) {
			return new TreeNode(data);
		}

		if (current.data.equals(data)) {
			if (current.count != maxCount) {
				current.count++;
			}
			return current;
		}

		if (comp.compare(data, current.data) < 0) {
			current.left = insert(current.left, data);
		} else {
			current.right = insert(current.right, data);
		}

		return current;
	}

	public void delete(T data) {
		root = delete(root, data);
	}

	private TreeNode delete(TreeNode current, T data) {
		if (current == null) {
			return current;
		}

		if (comp.compare(data, current.data) < 0) {
			current.left = delete(current.left, data);
		} else if (comp.compare(data, current.data) > 0) {
			current.right = delete(current.right, data);
		} else {
			current.count--;
			if (current.count != 0) { 
				return current;
			} else { //count is 0, delete node node by replacing it
				if (current.right == null) {
					return current.left;
				} else if (current.left == null) {
					return current.right;
				} else {
					TreeNode inorderSuccessor = getMinNode(current.right);
					current.data = inorderSuccessor.data;
					current.count = inorderSuccessor.count;
					current.right = deleteCompletely(current.right, current.data);
				}
			}
		}
		return current;
	}

	public void deleteAll(T data) {
		root = deleteCompletely(root, data);
	}

	private TreeNode deleteCompletely(TreeNode current, T data) {
		if (current == null) {
			return current;
		}

		if (comp.compare(data, current.data) < 0) {
			current.left = deleteCompletely(current.left, data);
		} else if (comp.compare(data, current.data) > 0) {
			current.right = deleteCompletely(current.right, data);
		} else {
			if (current.right == null) {
				return current.left;
			} else if (current.left == null) {
				return current.right;
			} else {
				TreeNode inorderSuccessor = getMinNode(current.right);
				current.data = inorderSuccessor.data;
				current.count = inorderSuccessor.count;
				current.right = deleteCompletely(current.right, current.data);
			}
		}
		return current;
	}

	public boolean contains(T data) {
		return containsHelper(root, data);
	}

	private boolean containsHelper(TreeNode current, T data) {
		if (current == null) {
			return false;
		}

		if (current.data.equals(data)) {
			return true;
		}

		return comp.compare(data, current.data) < 0 ? containsHelper(current.left, data)
				: containsHelper(current.right, data);
	}

	public T getMinValue() {
		TreeNode min = getMinNode(root);
		return min == null ? null : min.data;
	}

	private TreeNode getMinNode(TreeNode current) {
		if (current == null) {
			return current;
		}
		
		for (; current.left != null; current = current.left);
		return current;
	}

	public T getMaxValue() {
		TreeNode max = getMaxNode(root);
		return max == null ? null : max.data;
	}

	private TreeNode getMaxNode(TreeNode current) {
		if (current == null) {
			return current;
		}
		
		for (; current.right != null; current = current.right);
		return current;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public int size() {
		return sizeHelper(root);
	}

	private int sizeHelper(TreeNode current) {
		if (current == null) {
			return 0;
		}

		return 1 + sizeHelper(current.left) + sizeHelper(current.right);
	}

	public String levelorder() {
		LinkedListQueue<TreeNode> q = new LinkedListQueue<>();
		StringBuilder sb = new StringBuilder();
		TreeNode current = root;

		while (current != null) {
			sb.append(current + " ");
			if (current.left != null) {
				q.add(current.left);
			}
			if (current.right != null) {
				q.add(current.right);
			}

			current = q.poll();
		}
		return sb.toString();
	}

	public String inorder() {
		StringBuilder sb = new StringBuilder();
		inorder(root, sb);
		return sb.toString();
	}

	private void inorder(TreeNode current, StringBuilder sb) {
		if (current != null) {
			inorder(current.left, sb);
			sb.append(current + " ");
			inorder(current.right, sb);
		}
	}

	public String preorder() {
		StringBuilder sb = new StringBuilder();
		preorder(root, sb);
		return sb.toString();
	}

	private void preorder(TreeNode current, StringBuilder sb) {
		if (current != null) {
			sb.append(current + " ");
			preorder(current.left, sb);
			preorder(current.right, sb);
		}
	}

	public String postorder() {
		StringBuilder sb = new StringBuilder();
		postorder(root, sb);
		return sb.toString();
	}

	private void postorder(TreeNode current, StringBuilder sb) {
		if (current != null) {
			postorder(current.left, sb);
			postorder(current.right, sb);
			sb.append(current + " ");
		}
	}

	public static void main(String[] args) {
		BinarySearchTree<Integer> tree = new BinarySearchTree<>();
		Integer[] ary = { 12, 4, 16, 2, 6, 14, 18, 17 };
		Arrays.asList(ary).forEach(item -> tree.insert(item));
		Arrays.asList(ary).forEach(item -> tree.insert(item));

		System.out.println(tree.levelorder());
		System.out.println(tree.preorder());
		System.out.println(tree.postorder());
		System.out.println(tree.inorder());
	}
}
