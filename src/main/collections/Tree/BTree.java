package main.collections.Tree;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/* 
 * BTree implementation
 * 
 * 
 */
public class BTree<T extends Comparable<? super T>> {
	private class Node {
		T[] keys;
		int totalKeys;
		Node[] children;
		int totalChildren;

		@SuppressWarnings("unchecked")
		public Node() {
			keys = (T[]) new Comparable[M_ORDER];
			children = Arrays.copyOf(new Object[M_ORDER + 1], M_ORDER + 1, Node[].class);
		}

		public boolean isLeaf() {
			return totalChildren == 0;
		}

		public String toString() {
			String leafMsg = isLeaf() ? "leaf: " : "non-leaf: ";
			return leafMsg + "total keys = " + totalKeys + "\t" + Arrays.toString(keys) + "\t total children: " + totalChildren;
		}

		public void clear() {
			keys = null;
			children = null;
			totalKeys = 0;
			totalChildren = 0;
		}
	}

	private final int M_ORDER;
	private final int MIN_KEYS;
	private Comparator<T> comp;
	private Node root;
	private int size;

	public BTree(int order) {
		this(order, Comparator.naturalOrder());
	}

	public BTree(int order, Comparator<T> comp) {
		M_ORDER = order <= 2 ? 3 : order;
		MIN_KEYS = M_ORDER % 2 == 0 ? (M_ORDER - 1) / 2 : M_ORDER / 2;
		this.comp = comp != null ? comp : Comparator.naturalOrder();
		root = new Node();
	}

	public boolean contains(T data) {
		return contains(root, data);
	}

	private boolean contains(Node current, T data) {
		int chosenIndex = indexOf(current, data);
		return current.isLeaf() ? false : contains(current.children[chosenIndex], data);
	}

	private int indexOf(Node current, T data) {
		int chosenIndex = 0;
		for (; chosenIndex < current.totalKeys && comp.compare(data, current.keys[chosenIndex]) > 0; chosenIndex++);
		return chosenIndex;
	}

	public void insert(T data) {
		root = insert(root, data);
		size++;
	}

	private Node insert(Node curr, T data) {
		if (!curr.isLeaf()) {
			int chosenIndex = indexOf(curr, data);

			Node child = curr.children[chosenIndex];
			Node updatedChild = insert(child, data);

			if (child != updatedChild) {
				System.arraycopy(curr.children, chosenIndex, curr.children, chosenIndex + 1,
						curr.totalChildren - chosenIndex);
				System.arraycopy(curr.keys, chosenIndex, curr.keys, chosenIndex + 1, curr.totalKeys - chosenIndex);
				curr.keys[chosenIndex] = updatedChild.keys[0];
				curr.children[chosenIndex] = updatedChild.children[0]; // overwrite the child reference (left child)
				curr.children[chosenIndex + 1] = updatedChild.children[1]; // new right child
				curr.totalChildren++;
				curr.totalKeys++;

				if (curr.totalKeys == M_ORDER) {
					curr = split(curr);
				}
			}
		} else {
			curr = insertIntoLeaf(curr, data);
		}
		return curr;
	}

	private Node insertIntoLeaf(Node curr, T data) {
		int insertPos = indexOf(curr, data);
		System.arraycopy(curr.keys, insertPos, curr.keys, insertPos + 1, curr.totalKeys - insertPos);
		curr.keys[insertPos] = data;
		curr.totalKeys++;

		if (curr.totalKeys == M_ORDER) {
			curr = split(curr);
		}
		return curr;
	}

	private Node split(Node curr) {
		int totalRightKeys = M_ORDER / 2;
		Node parent = new Node();
		Node leftChild = new Node();
		Node rightChild = new Node();

		leftChild.totalKeys = MIN_KEYS;
		rightChild.totalKeys = totalRightKeys;
	
		parent.keys[parent.totalKeys++] = curr.keys[MIN_KEYS];
		parent.children[parent.totalChildren++] = leftChild;
		parent.children[parent.totalChildren++] = rightChild;

		System.arraycopy(curr.keys, MIN_KEYS + 1, rightChild.keys, 0, totalRightKeys);
		System.arraycopy(curr.keys, 0, leftChild.keys, 0, MIN_KEYS);
		
		if (!curr.isLeaf()) {
			leftChild.totalChildren = MIN_KEYS + 1;
			rightChild.totalChildren = totalRightKeys + 1;
			System.arraycopy(curr.children, MIN_KEYS + 1, rightChild.children, 0, rightChild.totalChildren);
			System.arraycopy(curr.children, 0, leftChild.children, 0, leftChild.totalChildren);
		}
		curr.clear();
		return parent;
	}

	public boolean delete(T data) {
		int prevSize = size;
		root = delete(root, data);
		return prevSize != size;
	}

	private Node delete(Node curr, T data) {
		if (!curr.isLeaf()) {
			int chosenIndex = indexOf(curr, data);
			boolean foundInNonLeaf = chosenIndex < curr.totalKeys && comp.compare(data, curr.keys[chosenIndex]) == 0;

			if (foundInNonLeaf) {
				if (chosenIndex != 0 && curr.children[chosenIndex].totalKeys >= MIN_KEYS) {
					data = getPredecessor(curr.children[chosenIndex]);
					curr.keys[chosenIndex] = data;
				} else if (chosenIndex != curr.totalChildren - 1
						&& curr.children[chosenIndex + 1].totalKeys >= MIN_KEYS) {
					data = getSuccessor(curr.children[chosenIndex + 1]);
					curr.keys[chosenIndex] = data;
					chosenIndex++;
				} else {
					return mergeChildren(curr, chosenIndex);
				}
			}

			Node child = curr.children[chosenIndex];
			Node updatedChild = delete(child, data);

			if (updatedChild.totalKeys < MIN_KEYS) {
				if (chosenIndex != 0 && curr.children[chosenIndex - 1].totalKeys > MIN_KEYS) {
					curr = borrowFromLeft(curr, chosenIndex - 1, chosenIndex);
				} else if (chosenIndex != curr.totalChildren - 1
						&& curr.children[chosenIndex + 1].totalKeys > MIN_KEYS) {
					curr = borrowFromRight(curr, chosenIndex + 1, chosenIndex);
				} else {
					curr = mergeChildren(curr, chosenIndex);
				}
			}
		} else {
			curr = deleteInLeaf(curr, data);
		}
		return curr;
	}

	private T getPredecessor(Node curr) {
		for (; !curr.isLeaf(); curr = curr.children[curr.totalChildren - 1]);
		return curr.keys[curr.totalKeys - 1];
	}

	private T getSuccessor(Node curr) {
		for (; !curr.isLeaf(); curr = curr.children[0]);
		return curr.keys[0];
	}

	// Some merge left to right, I chose right to left
	private Node mergeChildren(Node curr, int chosenIndex) {
		if (chosenIndex == curr.totalChildren - 1) {
			chosenIndex--;
		}
		
		Node child = curr.children[chosenIndex];
		
		child.keys[child.totalKeys++] = curr.keys[chosenIndex]; // give left child the key
		System.arraycopy(curr.keys, chosenIndex + 1, curr.keys, chosenIndex, curr.totalKeys - chosenIndex - 1);

		curr.children[chosenIndex] = merge(child, curr.children[chosenIndex + 1]);
		
		if (chosenIndex + 2 < curr.totalChildren) {
			System.arraycopy(curr.children, chosenIndex + 2, curr.children, chosenIndex + 1, curr.totalChildren - (chosenIndex + 1) - 1);
		}

		curr.keys[--curr.totalKeys] = null;
		curr.children[--curr.totalChildren] = null;

		if (curr == root && curr.totalKeys == 0) {
			return curr.children[0];
		}
		return curr;
	}

	private Node merge(Node left, Node right) {
		System.arraycopy(right.keys, 0, left.keys, left.totalKeys, right.totalKeys);
		System.arraycopy(right.children, 0, left.children, left.totalChildren, right.totalChildren);
		left.totalKeys += right.totalKeys;
		left.totalChildren += right.totalChildren;
		right.clear();
		return left;
	}

	private Node borrowFromLeft(Node parent, int leftNodeIndex, int transferToNodeIndex) {
		Node transferToNode = parent.children[transferToNodeIndex];
		Node leftNode = parent.children[leftNodeIndex];

		System.arraycopy(transferToNode.keys, 0, transferToNode.keys, 1, transferToNode.totalKeys++);
		transferToNode.keys[0] = parent.keys[leftNodeIndex];
		parent.keys[leftNodeIndex] = leftNode.keys[leftNode.totalKeys - 1];
		leftNode.keys[--leftNode.totalKeys] = null;

		if (!leftNode.isLeaf()) {
			System.arraycopy(transferToNode.children, 0, transferToNode.children, 1, transferToNode.totalChildren++);
			transferToNode.children[0] = leftNode.children[leftNode.totalChildren - 1];
			leftNode.children[--leftNode.totalChildren] = null;
		}
		return parent;
	}

	private Node borrowFromRight(Node parent, int rightNodeIndex, int transferToNodeIndex) {
		Node transferToNode = parent.children[transferToNodeIndex];
		Node rightNode = parent.children[rightNodeIndex];

		transferToNode.keys[transferToNode.totalKeys++] = parent.keys[transferToNodeIndex];
		parent.keys[transferToNodeIndex] = rightNode.keys[0];
		System.arraycopy(rightNode.keys, 1, rightNode.keys, 0, --rightNode.totalKeys);
		rightNode.keys[rightNode.totalKeys] = null;

		if (!rightNode.isLeaf()) {
			transferToNode.children[transferToNode.totalChildren++] = rightNode.children[0];
			System.arraycopy(rightNode.children, 1, rightNode.children, 0, --rightNode.totalChildren);
			rightNode.children[rightNode.totalChildren] = null;
		}
		return parent;
	}

	private Node deleteInLeaf(Node leafNode, T data) {
		int chosenIndex = indexOf(leafNode, data);
		boolean found = chosenIndex < leafNode.totalKeys && comp.compare(data, leafNode.keys[chosenIndex]) == 0;
		if (found) {
			if (chosenIndex + 1 < leafNode.totalKeys) {
				System.arraycopy(leafNode.keys, chosenIndex + 1, leafNode.keys, chosenIndex, leafNode.totalKeys - chosenIndex - 1);
			}
			leafNode.keys[--leafNode.totalKeys] = null;
			size--;
		}
		return leafNode;
	}

	public String toString() {
		if (size == 0) {
			return "Is Empty";
		}

		StringBuilder sb = new StringBuilder();
		Queue<Node> q = new LinkedList<>();
		int totalKeysFound = 0;
		sb.append("STARTING TO PRINT\n");
		q.add(root);
		while (!q.isEmpty()) {
			Node curr = q.remove();
			sb.append(curr + "\n");
			totalKeysFound += curr.totalKeys;
			for (int i = 0; i < curr.totalChildren; i++) {
				q.add(curr.children[i]);
			}
		}
		sb.append("Total keys found: " + totalKeysFound);
		return sb.toString();
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public static void main(String[] args) {
		test5();
		test4();
	}

	private static void test6() {
		System.out.println("Testing with ORDER 6");
		BTree<Integer> tree = new BTree<Integer>(6);
		Integer[] nums = { 50, 150, 125, 100, 75, 25 };
		Arrays.asList(nums).forEach(n -> tree.insert(n));
		System.out.println(tree);
		System.out.println(nums.length);
	}

	private static void test5() {
		System.out.println("Testing with ORDER 5");
		BTree<Integer> tree = new BTree<Integer>(5);
		// Integer[] nums = { 50, 150, 125, 25, 75, 10, 60, 35, 55, 65, 70, 175, 200,
		// 140, 45, 40, 48, 141, 142, 143, 180,185, 190, 61, 62, 63 };

		Integer[] nums = { 50, 150, 125, 25, 75, 10, 60, 35, 55, 65, 70, 175, 200, 140, 45, 40, 48, 141, 142, 143, 180,
				185, 190, 61, 62, 63, 80, 81, 82, 83, 84, 85, 100, 110, 120 };
		Arrays.asList(nums).forEach(n -> tree.insert(n));
		System.out.println(tree);
		System.out.println(nums.length);
		
		for (int i = nums.length - 1; i > 0; i--) { // leave one elft
			tree.delete(nums[i]);
			//System.out.println(tree);
			System.out.println(tree.size);
		}
		
		tree.delete(120);
		System.out.println(tree);
		System.out.println(tree.size);
	}

	private static void test4() {
		System.out.println("Testing with ORDER 4");
		BTree<Integer> tree = new BTree<Integer>(4);
		Integer[] nums = { 50, 150, 125, 25, 75, 10, 60, 35, 55, 65, 70, 175, 200, 100, 115, 110 };
		// Integer[] nums = { 50, 150, 125, 25, 75, 10, 60, 35,};
		// Integer[] nums = { 50, 55, 52, 51, };
		Arrays.asList(nums).forEach(n -> tree.insert(n));
		System.out.println(tree);
		System.out.println(nums.length);
		for (int i = nums.length - 1; i > 0; i--) { // leave 1 left
			tree.delete(nums[i]);
			//System.out.println(tree);
			System.out.println(tree.size);
		}
		System.out.println(tree);
		System.out.println(tree.size);
	}

	private static void test3() {
		System.out.println("Testing with ORDER 3");
		BTree<Integer> tree = new BTree<Integer>(3);
		Integer[] nums = { 50, 150, 125, 25, 75, 10, 60, 35, 55, 65, 70, 175, 200, 100, 115, 110 };
		Arrays.asList(nums).forEach(n -> tree.insert(n));
		System.out.println(tree);
		System.out.println(nums.length);
	}

	private static void test4small() {
		System.out.println("Testing with ORDER 4");
		BTree<Integer> tree = new BTree<Integer>(4);
		Integer[] nums = { 10, 20, 30, 40, 50 };
		Arrays.asList(nums).forEach(n -> tree.insert(n));
		System.out.println(tree);
		System.out.println(nums.length);
		tree.delete(20);
		tree.delete(30);
		System.out.println(tree);
		System.out.println(nums.length);
	}

}
