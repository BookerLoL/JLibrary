package main.collections.Queue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Random;

public class Treap<T extends Comparable<? super T>> {
	private class Node {
		T data;
		int priority;
		Node left;
		Node right;

		public Node(T data) {
			this.data = data;
			priority = randomizer.nextInt();
		}

		public String toString() {
			return data + " has priority: " + priority;
		}
	}

	private Random randomizer;
	private int size;
	private Node root;
	private Comparator<T> comp;

	public Treap() {
		this(Comparator.naturalOrder());
	}

	public Treap(Comparator<T> comp) {
		this.comp = comp != null ? comp : Comparator.naturalOrder();
		randomizer = new Random();
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	private Node rotateWithLeftChild(Node parent) {
		System.out.println("Rotating left child");
		Node leftChild = parent.left;
		parent.left = leftChild.right;
		leftChild.right = parent;
		return leftChild;
	}

	private Node rotateWithRightChild(Node parent) {
		System.out.println("Rotating right child");

		Node rightChild = parent.right;
		parent.right = rightChild.left;
		rightChild.left = parent;
		return rightChild;
	}

	public void insert(T data) {
		root = insert(root, data);
		size++;
	}

	private Node insert(Node current, T data) {
		if (current == null) {
			return new Node(data);
		}

		if (comp.compare(data, current.data) <= 0) {
			current.left = insert(current.left, data);
			if (current.left.priority > current.priority) {
				current = rotateWithLeftChild(current);
			}
		} else {
			current.right = insert(current.right, data);
			if (current.right.priority > current.priority) {
				current = rotateWithRightChild(current);
			}
		}
		return current;
	}

	public boolean contains(T data) {
		for (Node curr = root; curr != null;) {
			int compValue = comp.compare(data, curr.data);
			if (compValue < 0) {
				curr = curr.right;
			} else if (compValue > 0) {
				curr = curr.left;
			} else {
				return true;
			}
		}
		return false;
	}

	public void delete(T data) {
		root = delete(root, data);
	}

	private Node delete(Node curr, T data) {
		if (curr == null) {
			return curr;
		}

		int compVal = comp.compare(data, curr.data);
		if (compVal < 0) {
			curr.left = delete(curr.left, data);
		} else if (compVal > 0) {
			curr.right = delete(curr.right, data);
		} else {
			if (curr.right == null) {
				return curr.left;
			} else if (curr.left == null) {
				return curr.right;
			} else if (curr.left.priority < curr.right.priority) {
				curr = rotateWithRightChild(curr);
				curr.left = delete(curr.left, data);
			} else {
				curr = rotateWithLeftChild(curr);
				curr.right = delete(curr.right, data);
			}
		}
		return curr;
	}

	public T findMax() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		Node curr = root;
		for (; curr.right != null; curr = curr.right)
			;
		return curr.data;
	}

	public T findMin() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		Node curr = root;
		for (; curr.left != null; curr = curr.left)
			;
		return curr.data;
	}

	public void preorder() {
		preorder(root, null);
	}

	private void preorder(Node current, Node parent) {
		if (current != null) {
			System.out.println(current + "\tparent: " + parent);
			preorder(current.left, current);
			preorder(current.right, current);
		}
	}

	public static void main(String[] args) {
		Treap<Integer> tree = new Treap<>();
		Integer[] nums = { 1, 3, 6, 8, 9, 14, 20, 21, 30, 31, 50, 60, -10, -30, -40, -50 };
		Arrays.asList(nums).forEach(n -> tree.insert(n));
		tree.preorder();
		System.out.println(tree.findMax());
		System.out.println(tree.findMin());
		tree.delete(30);
		tree.delete(21);
		Arrays.asList(nums).forEach(n -> tree.delete(n));
		tree.preorder();
	}
}
