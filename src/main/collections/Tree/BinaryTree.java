package main.collections.Tree;

public class BinaryTree<T> {
	protected class TreeNode {
		protected T data;
		protected TreeNode left;
		protected TreeNode right;

		public TreeNode(T data) {
			this(data, null, null);
		}

		public TreeNode(T data, TreeNode left, TreeNode right) {
			this.data = data;
			this.left = left;
			this.right = right;
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
	}

	private TreeNode root;

	public BinaryTree() {
		root = null;
	}

	public String inorder() {
		StringBuilder sb = new StringBuilder();
		inorder(root, sb);
		return sb.toString();
	}

	private void inorder(TreeNode current, StringBuilder sb) {
		if (current != null) {
			inorder(current.getLeft(), sb);
			sb.append(current.getData() + " ");
			inorder(current.getRight(), sb);
		}
	}

	public String preorder() {
		StringBuilder sb = new StringBuilder();
		preorder(root, sb);
		return sb.toString();
	}

	private void preorder(TreeNode current, StringBuilder sb) {
		if (current != null) {
			sb.append(current.getData() + " ");
			preorder(current.getLeft(), sb);
			preorder(current.getRight(), sb);
		}
	}

	public String postorder() {
		StringBuilder sb = new StringBuilder();
		postorder(root, sb);
		return sb.toString();
	}

	private void postorder(TreeNode current, StringBuilder sb) {
		if (current != null) {
			postorder(current.getLeft(), sb);
			postorder(current.getRight(), sb);
			sb.append(current.getData() + " ");
		}
	}
	
	public String levelorder() {
		LinkedListQueue<TreeNode> q = new LinkedListQueue<>();
		StringBuilder sb = new StringBuilder();
		TreeNode current = root;
		
		while (current != null) {
			sb.append(current.getData() + " ");
			if (current.getLeft() != null) {
				q.add(current.getLeft());
			}
			if (current.getRight() != null) {
				q.add(current.getRight());
			}
			
			
			current = q.poll();
		}
		return sb.toString();
	}

	public int size() {
		return sizeHelper(root);
	}

	private int sizeHelper(TreeNode current) {
		if (current == null) {
			return 0;
		}

		return 1 + sizeHelper(current.getLeft()) + sizeHelper(current.getRight());
	}
}
