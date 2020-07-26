import java.util.Arrays;
import java.util.Comparator;

public class AVLTree<T extends Comparable<? super T>> {
	private class TreeNode {
		T data;
		TreeNode left;
		TreeNode right;
		int height;

		public TreeNode(T data) {
			this.data = data;
		}

		public String toString() {
			return "h: " + height + ", d: " + data + ", bf:" + balanceFactor(this);
		}
	}

	private int size;
	private Comparator<T> comp;
	private TreeNode root;

	public AVLTree() {
		this(Comparator.naturalOrder());
	}

	public AVLTree(Comparator<T> comp) {
		this.comp = comp != null ? comp : Comparator.naturalOrder();
	}

	public void insert(T data) {
		root = insert(root, data);
		size++;
	}

	private TreeNode insert(TreeNode curr, T data) {
		if (curr == null) {
			return new TreeNode(data);
		}

		if (comp.compare(data, curr.data) < 0) {
			curr.left = insert(curr.left, data);
		} else {
			curr.right = insert(curr.right, data);
		}

		return rebalance(curr);
	}

	public void remove(T data) {
		root = remove(root, data);
	}

	private TreeNode remove(TreeNode curr, T data) {
		if (curr == null) {
			return curr;
		}

		int compValue = comp.compare(data, curr.data);
		if (compValue < 0) {
			curr.left = remove(curr.left, data);
		} else if (compValue > 0) {
			curr.right = remove(curr.right, data);
		} else if (curr.left != null && curr.right != null) {
			TreeNode replacement = getMaxNode(curr.left);
			curr.data = replacement.data;
			curr.left = remove(curr.left, curr.data);
			size--;
		} else {
			curr = curr.left != null ? curr.left : curr.right;
			size--;
		}

		return rebalance(curr);
	}

	@SuppressWarnings("unused")
	private TreeNode getMinNode(TreeNode curr) {
		if (curr == null) {
			return curr;
		}

		for (; curr.left != null; curr = curr.left);
		return curr;
	}

	private TreeNode getMaxNode(TreeNode curr) {
		if (curr == null) {
			return curr;
		}

		for (; curr.right != null; curr = curr.right);
		return curr;
	}

	private TreeNode rebalance(TreeNode curr) {
		if (curr == null) {
			return curr;
		}

		updateHeight(curr);
		int balanceFactor = balanceFactor(curr);
		if (isHeavyRight(balanceFactor)) {
			return isRightWeighted(balanceFactor(curr.right)) ? rotateRR(curr) : rotateRL(curr);
		} else if (isHeavyLeft(balanceFactor)) {
			return isLeftWeighted(balanceFactor(curr.left)) ? rotateLL(curr) : rotateLR(curr);
		}
		return curr;
	}

	private int getHeight(TreeNode curr) {
		return curr != null ? curr.height : -1;
	}

	private void updateHeight(TreeNode curr) {
		curr.height = Math.max(getHeight(curr.left), getHeight(curr.right)) + 1;
	}

	private int balanceFactor(TreeNode curr) {
		return curr != null ? getHeight(curr.left) - getHeight(curr.right) : 0;
	}

	private boolean isHeavyRight(int balanceFactor) {
		return balanceFactor < -1;
	}

	private boolean isHeavyLeft(int balanceFactor) {
		return balanceFactor > 1;
	}

	private boolean isRightWeighted(int balanceFactor) {
		return balanceFactor <= -1;
	}

	private boolean isLeftWeighted(int balanceFactor) {
		return balanceFactor >= 1;
	}

	public boolean contains(T data) {
		return contains(root, data);
	}

	private boolean contains(TreeNode curr, T data) {
		while (curr != null) {
			int compVal = comp.compare(data, curr.data);
			if (compVal < 0) {
				curr = curr.left;
			} else if (compVal > 0) {
				curr = curr.right;
			} else {
				return true;
			}
		}
		return false;
	}

	private TreeNode rotateLL(TreeNode node) {
		TreeNode leftChild = node.left;
		node.left = leftChild.right;
		leftChild.right = node;

		updateHeight(node);
		updateHeight(leftChild);
		return leftChild;
	}

	private TreeNode rotateRR(TreeNode node) {
		TreeNode rightChild = node.right;
		node.right = rightChild.left;
		rightChild.left = node;

		updateHeight(node);
		updateHeight(rightChild);
		return rightChild;
	}

	private TreeNode rotateLR(TreeNode node) {
		node.left = rotateRR(node.left);
		return rotateLL(node);
	}

	private TreeNode rotateRL(TreeNode node) {
		node.right = rotateLL(node.right);
		return rotateRR(node);
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void printPreorder() {
		preorder(root, false, null);
	}

	private void preorder(TreeNode curr, boolean isLeft, TreeNode parent) {
		if (curr != null) {
			String path = isLeft ? "is left: " : "is right: ";
			path = parent == null ? "" : path;
			System.out.println(path + curr + "\tfrom: " + parent);
			preorder(curr.left, true, curr);
			preorder(curr.right, false, curr);
		}
	}

	public static void main(String[] args) {
		test3();
	}

	@SuppressWarnings("unused")
	private static void test1() {
		AVLTree<Integer> tree = new AVLTree<>();
		Integer[] nums = { 20, 10, 0, 15, 30, 12, 13, 14, 11, 15, 0 };
		Arrays.asList(nums).forEach(num -> tree.insert(num));
		tree.printPreorder();
	}

	@SuppressWarnings("unused")
	private static void test2() {
		AVLTree<Integer> tree = new AVLTree<>();
		Integer[] nums = { 50, 45, 40, 35, 30, 25, 20, 15, 10, 5 };
		Arrays.asList(nums).forEach(num -> tree.insert(num));
		tree.printPreorder();
	}

	private static void test3() {
		AVLTree<Integer> tree = new AVLTree<>();
		Integer[] nums = { 50, 25, 75, 20, 35, 70, 80 };
		Arrays.asList(nums).forEach(num -> tree.insert(num));
		tree.remove(50);
		tree.remove(35);
		tree.printPreorder();
		tree.remove(25);
		tree.remove(75);
		tree.printPreorder();
	}
}
