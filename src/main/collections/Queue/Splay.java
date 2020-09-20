package main.collections.Queue;
import java.util.Arrays;
import java.util.Comparator;

public class Splay<T extends Comparable<? super T>> {
	private class Node {
		Node left;
		Node right;
		T data;

		public Node(T data) {
			this.data = data;
		}
	}

	private Comparator<T> comp;
	private Node root;

	public Splay() {
		this(Comparator.naturalOrder());
	}

	public Splay(Comparator<T> comp) {
		this.comp = comp != null ? comp : Comparator.naturalOrder();
	}

	public Node rightRotate(Node current) {
		Node leftChild = current.left;
		current.left = leftChild.right;
		leftChild.right = current;
		return leftChild;
	}

	public Node leftRotate(Node current) {
		Node rightChild = current.right;
		current.right = rightChild.left;
		rightChild.left = current;
		return rightChild;
	}

	public void insert(T data) {
		insertHelper(data);
	}

	private void insertHelper(T data) {
		if (root == null) {
			root = new Node(data);
			return;
		}
		
		root = splay(root, data);
		
		int compVal = comp.compare(data, root.data);
		
		if (compVal < 0) {
			Node dataNode = new Node(data);
			dataNode.left = root.left;
			dataNode.right = root;
			root.left = null;
			root = dataNode;
		} else if (compVal > 0) {
			Node dataNode = new Node(data);
			dataNode.right = root.right;
			dataNode.left = root;
			root.right = null;
			root = dataNode;
		} else {
			root.data = data;
		}
	}
	
	public boolean contains(T data) {
		if (root == null) {
			return false;
		}
		
		root = splay(root, data);
		return comp.compare(data, root.data) == 0;
	}

	
	public void delete(T data) {
		if (root == null) {
			return;
		}
		
		root = splay(root, data);
		
		if (comp.compare(root.data, data) == 0) {
			System.out.println("here");
			if (root.left == null) {
				root = root.right;
			} else {
				Node tempRight = root.right;
				root = root.left;
				splay(root, data);
				root.right = tempRight;
			}
		}
	}
	
	private Node splay(Node current, T data) {
		if (current == null) {
			return current;
		}
		
		int compVal = comp.compare(data, current.data);
		if (compVal < 0) {
			if (current.left == null) {
				return current;
			}
			int compVal2 = comp.compare(data, current.left.data);
			if (compVal2 < 0) {
				current.left.left = splay(current.left.left, data);
				current = rightRotate(current);
			} else if (compVal2 > 0) {
				current.left.right = splay(current.left.right, data);
				if (current.left.right != null) {
					current.left = leftRotate(current.left);
				}
			}
			return current.left == null ? current : rightRotate(current);
		} else if (compVal > 0) {
			if (current.right == null) {
				return current;
			}
			int compVal2 = comp.compare(data, current.right.data);
			if (compVal2 < 0) {
				current.right.left = splay(current.right.left, data);
				if (current.right.left != null) {
					current.right = rightRotate(current.right);
				}
			} else if (compVal2 > 0) {
				current.right.right = splay(current.right.right, data);
				current = leftRotate(current);
			}
			
			return current.right == null ? current : leftRotate(current);
		}
		return current;
	}
	
	public void inorder() {
		inorder(root);
	}
	
	private void inorder(Node n) {
		if (n != null) {
			System.out.println(n.data);
			inorder(n.left);
			inorder(n.right);
		}
	}
	
	public static void main(String[] args) {
		Splay<Integer> splay = new Splay<>();
		Integer[] nums = { 10, 20, 30, 100, 90, 40, 50, 60, 70, 80, 150, 110, 120 };
		Arrays.asList(nums).forEach(n -> splay.insert(n));
		splay.inorder();

		Arrays.asList(nums).forEach(n -> System.out.println(splay.contains(n)));
		Arrays.asList(nums).forEach(n -> splay.delete(n));
		splay.inorder();
		Arrays.asList(nums).forEach(n -> System.out.println(splay.contains(n)));
	}

}
