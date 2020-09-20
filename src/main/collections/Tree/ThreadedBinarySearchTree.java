package main.collections.Tree;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/*
 * ThreadedBinarySearchTree allow for inorder to be performed without a stack needed
 */
public class ThreadedBinarySearchTree<T extends Comparable<T>> {
	protected class TreeNode {
		protected T data;
		protected TreeNode left;
		protected TreeNode right;
		private boolean lPredecessor;
		private boolean rSuccessor;
		int count;

		public TreeNode(T data) {
			this(data, null, null);
		}

		public TreeNode(T data, TreeNode left, TreeNode right) {
			this.data = data;
			this.left = left;
			this.right = right;
			lPredecessor = true;
			rSuccessor = true;
			count = 1;
		}

		public void setRight(TreeNode newRight) {
			right = newRight;
		}

		public void setLeft(TreeNode newLeft) {
			left = newLeft;
		}

		public T setData(T data) {
			T prev = getData();
			this.data = data;
			return prev;
		}

		public T getData() {
			return data;
		}

		public TreeNode getLeft() {
			return left;
		}

		public TreeNode getRight() {
			return right;
		}

		public boolean hasPredecessor() {
			return lPredecessor;
		}

		public boolean hasSuccessor() {
			return rSuccessor;
		}

		public void setLPredecessor(boolean lPred) {
			lPredecessor = lPred;
		}

		public void setRSucccessor(boolean rSucc) {
			rSuccessor = rSucc;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public boolean isAtMaxCount() {
			return count == getMaxLimit();
		}

		public String toString() {
			return "(" + getData() + ", count: " + getCount() + ")";
		}
	}

	public static final int MAX_LIMIT = 1;
	private TreeNode root;
	private Comparator<T> comp;
	private int maxLimit;

	public ThreadedBinarySearchTree() {
		this(Comparator.naturalOrder(), MAX_LIMIT);
	}

	public ThreadedBinarySearchTree(Comparator<T> comp) {
		this(comp, MAX_LIMIT);
	}

	public ThreadedBinarySearchTree(int maxLimit) {
		this(Comparator.naturalOrder(), maxLimit);
	}

	public ThreadedBinarySearchTree(Comparator<T> comp, int maxLimit) {
		root = null;
		this.comp = comp == null ? Comparator.naturalOrder() : comp;
		this.maxLimit = maxLimit <= 0 ? MAX_LIMIT : maxLimit;
	}

	public void insert(T data) {
		root = insert(root, data);
	}

	public int getMaxLimit() {
		return maxLimit;
	}

	private TreeNode insert(TreeNode root, T data) {
		TreeNode parent = null;
		TreeNode curr = root;

		while (curr != null) {
			int compResult = comp.compare(data, curr.getData());
			if (compResult == 0) {
				if (!curr.isAtMaxCount()) {
					curr.setCount(curr.getCount() + 1);
				}
				return root;
			}

			parent = curr;
			if (compResult < 0) {
				if (!curr.hasPredecessor()) {
					curr = curr.getLeft();
				} else {
					break;
				}
			} else {
				if (!curr.hasSuccessor()) {
					curr = curr.getRight();
				} else {
					break;
				}
			}
		}

		TreeNode newNode = new TreeNode(data);

		if (parent == null) {
			root = newNode;
		} else if (comp.compare(data, parent.getData()) < 0) { //maybe can swap to: parent.hasPredecessor() 
			newNode.setLeft(parent.getLeft());
			newNode.setRight(parent);
			parent.setLPredecessor(false);
			parent.setLeft(newNode);
		} else {
			newNode.setLeft(parent);
			newNode.setRight(parent.getRight());
			parent.setRSucccessor(false);
			parent.setRight(newNode);
		}

		return root;
	}

	public String inorder() {
		if (root == null) {
			return "Tree is empty";
		}

		StringBuilder sb = new StringBuilder();
		TreeNode curr = root;
		while (!curr.hasPredecessor()) {
			curr = curr.getLeft();
		}

		while (curr != null) {
			sb.append(curr + " ");
			curr = inorderNext(curr);
		}
		return sb.toString();
	}

	private TreeNode inorderNext(TreeNode curr) {
		if (curr.hasSuccessor()) {
			return curr.getRight();
		}

		curr = curr.getRight();
		while (!curr.hasPredecessor()) {
			curr = curr.getLeft();
		}
		return curr;
	}

	public String preorder() {
		if (root == null) {
			return "Tree is empty";
		}

		StringBuilder sb = new StringBuilder();
		TreeNode curr = root;

		do {
			sb.append(curr + " ");
			curr = preorderNext(curr);
		} while (curr != null);

		return sb.toString();
	}

	private TreeNode preorderNext(TreeNode curr) {
		if (curr.hasSuccessor() && curr.getRight() != null) {
			return curr.getRight().getRight();
		}

		if (!curr.hasPredecessor() && curr.getLeft() != null) {
			return curr.getLeft();
		} else {
			return curr.getRight();
		}
	}

	public String postorder() {
		if (root == null) {
			return "Tree is empty";
		}
		StringBuilder sb = new StringBuilder();
	    TreeNode curr = root;
	    Set<TreeNode> visited = new HashSet<>();
	    while (curr != null && !visited.contains(curr)) { 
	    	if (curr.getLeft() != null && !curr.hasPredecessor() && !visited.contains(curr.getLeft())) {
	    		curr = curr.getLeft();
	    	} else if (curr.getRight() != null && !curr.hasSuccessor() && !visited.contains(curr.getRight())) {
	    		curr = curr.getRight();
	    	} else {
	    		sb.append(curr + " ");
	    		visited.add(curr);
	    		curr = root;
	    	}
	    } 
	    return sb.toString();
	}

	public String levelorder() {
		LinkedListQueue<TreeNode> q = new LinkedListQueue<>();
		StringBuilder sb = new StringBuilder();
		TreeNode current = root;

		while (current != null) {
			sb.append(current + " ");
			if (!current.hasPredecessor()) {
				q.add(current.getLeft());
			}
			if (!current.hasSuccessor()) {
				q.add(current.getRight());
			}

			current = q.poll();
		}
		return sb.toString();
	}

	public boolean contains(T data) {
		return containsHelper(root, data);
	}

	private boolean containsHelper(TreeNode current, T data) {
		if (current == null) {
			return false;
		}

		if (current.getData().equals(data)) {
			return true;
		}

		return comp.compare(data, current.getData()) < 0 ? containsHelper(current.getLeft(), data)
				: containsHelper(current.getRight(), data);
	}

	public static void main(String[] args) {
		ThreadedBinarySearchTree<Integer> tree = new ThreadedBinarySearchTree<>(2);
		Integer[] ary = { 12, 4, 16, 2, 6, 14, 18, 17 };
		Arrays.asList(ary).forEach(item -> tree.insert(item));
		Arrays.asList(ary).forEach(item -> tree.insert(item));

		System.out.println(tree.inorder());
		System.out.println(tree.preorder());
		System.out.println(tree.postorder());
		System.out.println(tree.levelorder());
	}
}
