import java.util.Comparator;

//Reference: Java 9 Data Structures and Algorithms, Debasish Ray Chawdhuri
//Mimics an array based heap
public class LinkedHeap<T extends Comparable<? super T>> {
	private class Node {
		T value;
		Node left;
		Node right;
		Node parent;

		public Node(T value, Node parent) {
			this.value = value;
			this.parent = parent;
		}
	}

	private Comparator<T> comp;
	private Node root;
	private int size;

	public LinkedHeap() {
		this(Comparator.naturalOrder());
	}

	public LinkedHeap(Comparator<T> comp) {
		this.comp = comp;
	}

	private void swapWithParent(Node parent, boolean left) {
		Node child = left ? parent.left : parent.right;
		Node childLeft = child.left;
		Node childRight = child.right;
		Node childSibling = left ? parent.right : parent.left;
		Node grandParent = parent.parent;

		parent.left = childLeft;
		if (childLeft != null) {
			childLeft.parent = parent;
		}

		parent.right = childRight;
		if (childRight != null) {
			childRight.parent = parent;
		}

		parent.parent = child;
		if (left) {
			child.right = childSibling;
			child.left = parent;
		} else {
			child.left = childSibling;
			child.right = parent;
		}
		child.parent = grandParent;
		if (childSibling != null) {
			childSibling.parent = grandParent;
		}

		if (parent == root) {
			root = child;
		} else {
			if (grandParent.left == parent) {
				grandParent.left = child;
			} else {
				grandParent.right = child;
			}
		}
	}

	private Node findNodeAtPosition(int pos) {
		if (pos == 1) {
			return root;
		}
		int side = pos % 2;
		int parentPos = pos / 2;
		Node parent = findNodeAtPosition(parentPos);
		return side == 0 ? parent.left : parent.right;
	}

	private void trickleUp(Node curr) {
		if (curr == root) {
			return;
		} else if (comp.compare(curr.value, curr.parent.value) < 0) {
			swapWithParent(curr.parent, curr.parent.left == curr);
			trickleUp(curr);
		}
	}

	public void insert(T val) {
		if (root == null) {
			root = new Node(val, null);
		} else {
			Node parent = findNodeAtPosition((size + 1) / 2);
			int side = (size + 1) % 2;
			Node newNode = new Node(val, parent);
			if (side == 0) {
				parent.left = newNode;
			} else {
				parent.right = newNode;
			}
			trickleUp(newNode);
		}
		size++;
	}

	private void trickleDown(Node curr) {
		if (curr == null || curr.left == null) {
			return;
		}

		if (curr.right == null) {
			if (comp.compare(curr.left.value, curr.value) < 0) {
				swapWithParent(curr, true);
				trickleDown(curr);
			}
		} else {
			boolean leftSide = false;
			if (comp.compare(curr.left.value, curr.right.value) < 0) {
				leftSide = true;
			}
			swapWithParent(curr, leftSide);
			trickleDown(curr);
		}
	}

	public T removeMin() {
		if (root == null) {
			return null;
		}

		Node last = findNodeAtPosition(size);
		if (last == root) {
			root = null;
			size--;
			return last.value;
		}
		T val = root.value;
		root.value = last.value;
		Node parent = last.parent;
		if (parent.left == last) {
			parent.left = null;
		} else {
			parent.right = null;
		}
		size--;
		trickleDown(root);
		return val;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public int size() {
		return size;
	}
}
