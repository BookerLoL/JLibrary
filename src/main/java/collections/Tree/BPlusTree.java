package main.collections.Tree;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/*
 * Range Search function needs to be implemented along with a normal search 
 * but that's not too difficult with insert and delete correctly updating leaf nodes and internal nodes
 * 
 * Behavior with duplicates not tested yet, could probably add some sort of list or count or something.
 * 
 * Finding a replacement node can be simplified by adding an instance Node pointer to the current node that contains that value
 * then simply give it the next available value prior to the recursive call as this would avoid having to go down the tree and avoid rescanning
 * 
 */
public class BPlusTree<T extends Comparable<? super T>> {
	private class Node {
		T[] keys;
		int totalKeys;
		Node[] children;
		int totalChildren;
		Node next;
		Node prev;

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
			String hasNext = next != null ? "has next" : "no next";
			String hasPrev = prev != null ? "has prev" : "no prev";
			return leafMsg + "total keys = " + totalKeys + "\t" + Arrays.toString(keys) + "\t total children: " + totalChildren + " " + hasPrev + " " + hasNext;
		}

		public boolean isFull() {
			return totalKeys == M_ORDER;
		}

		public void clear() {
			keys = null;
			children = null;
			next = null;
			prev = null;
		}
	}

	private final int M_ORDER;
	private final int MIN_KEYS;
	private Comparator<T> comp;
	private Node root;
	private int size;

	public BPlusTree(int order) {
		this(order, Comparator.naturalOrder());
	}

	public BPlusTree(int order, Comparator<T> comp) {
		M_ORDER = order <= 2 ? 3 : order;
		MIN_KEYS = M_ORDER % 2 == 0 ? (M_ORDER - 1) / 2 : M_ORDER / 2;
		this.comp = comp != null ? comp : Comparator.naturalOrder();
		root = new Node();
	}

	public void insert(T data) {
		root = insert(root, data);
		size++;
	}

	private int searchKeyIndex(Node curr, T data) {
		int index;
		for (index = 0; index < curr.totalKeys && comp.compare(data, curr.keys[index]) > 0; index++);
		return index;
	}

	private int searchDeleteKeyIndexInternal(Node curr, T data) {
		int index;
		for (index = 0; index < curr.totalKeys && comp.compare(data, curr.keys[index]) >= 0; index++);
		return index;
	}

	private Node insert(Node curr, T data) {
		if (!curr.isLeaf()) {
			int chosenIndex = searchKeyIndex(curr, data);
			Node child = curr.children[chosenIndex];
			Node updatedChild = insert(child, data);

			if (child != updatedChild) {
				curr = insertIntoInternal(curr, updatedChild, chosenIndex);
			}
		} else {
			curr = insertIntoLeaf(curr, data);
		}
		return curr;
	}

	private Node insertIntoInternal(Node curr, Node newChild, int chosenIndex) {
		System.arraycopy(curr.children, chosenIndex, curr.children, chosenIndex + 1, curr.totalChildren - chosenIndex);
		System.arraycopy(curr.keys, chosenIndex, curr.keys, chosenIndex + 1, curr.totalKeys - chosenIndex);

		curr.keys[chosenIndex] = newChild.keys[0];
		curr.children[chosenIndex + 1] = newChild.children[1];
		curr.totalChildren++;
		curr.totalKeys++;

		if (newChild.children[0].isLeaf()) {
			linkLeafNodes(curr, chosenIndex, chosenIndex + 1);
			linkLeafNodes(curr, chosenIndex + 1, chosenIndex + 2);
		}

		return curr.isFull() ? splitInternal(curr) : curr;
	}

	private Node insertIntoLeaf(Node curr, T data) {
		int insertPos = searchKeyIndex(curr, data);
		System.arraycopy(curr.keys, insertPos, curr.keys, insertPos + 1, curr.totalKeys - insertPos);
		curr.keys[insertPos] = data;
		curr.totalKeys++;

		return curr.isFull() ? splitLeaf(curr) : curr;
	}

	private Node splitLeaf(Node leaf) {
		int totalRightKeys = (M_ORDER + 1) / 2;
		int startRightPos = M_ORDER - totalRightKeys;

		Node parent = new Node();
		Node rightChild = new Node();

		parent.children[parent.totalChildren++] = leaf;
		parent.children[parent.totalChildren++] = rightChild;

		System.arraycopy(leaf.keys, startRightPos, rightChild.keys, 0, totalRightKeys);
		Arrays.fill(leaf.keys, startRightPos, M_ORDER, null);

		parent.keys[parent.totalKeys++] = rightChild.keys[0];
		parent.totalChildren = 2;
		rightChild.totalKeys = totalRightKeys;
		leaf.totalKeys = startRightPos;

		return parent;
	}

	private Node splitInternal(Node internal) {
		int totalLeftKeys = M_ORDER / 2;
		int startRightPos = totalLeftKeys + 1;

		Node parent = new Node();
		Node rightChild = new Node();

		parent.children[parent.totalChildren++] = internal;
		parent.children[parent.totalChildren++] = rightChild;

		System.arraycopy(internal.keys, startRightPos, rightChild.keys, 0, internal.totalKeys - startRightPos);
		System.arraycopy(internal.children, startRightPos, rightChild.children, 0, internal.totalChildren - startRightPos);

		parent.keys[parent.totalKeys++] = internal.keys[totalLeftKeys];
		parent.totalChildren = 2;
		rightChild.totalKeys = internal.totalKeys - startRightPos;
		rightChild.totalChildren = internal.totalChildren - startRightPos;
		internal.totalKeys = totalLeftKeys;
		internal.totalChildren = totalLeftKeys + 1;

		Arrays.fill(internal.keys, totalLeftKeys, M_ORDER, null);
		Arrays.fill(internal.children, totalLeftKeys + 1, M_ORDER + 1, null);

		return parent;
	}

	private void linkLeafNodes(Node parent, int beforeNodeIdx, int afterNodeIdx) {
		if (beforeNodeIdx < 0 || afterNodeIdx >= parent.totalChildren) {
			return;
		}

		Node beforeNode = parent.children[beforeNodeIdx];
		Node afterNode = parent.children[afterNodeIdx];

		if (afterNodeIdx == parent.totalChildren - 1 && afterNode.next == null) {
			afterNode.next = beforeNode.next;
		}

		if (beforeNodeIdx == 0 && beforeNode.prev == null && beforeNode != afterNode.prev) {
			beforeNode.prev = afterNode.prev;
		}

		beforeNode.next = afterNode;
		afterNode.prev = beforeNode;
	}

	public boolean delete(T data) {
		return delete(root, data);
	}

	private boolean delete(Node curr, T data) {
		if (!curr.isLeaf()) {
			int chosenIndex = searchDeleteKeyIndexInternal(curr, data);
			Node child = curr.children[chosenIndex];
			boolean changed = delete(child, data);

			if (changed) {
				boolean borrowed = false;
				updatedInternalKeyWithSameData(curr, data); // call this before merging, otherwise incorrect changes can
															// propagate
				if (child.totalKeys < MIN_KEYS) {
					if (canBorrow(curr, chosenIndex - 1)) {
						borrowFromPrev(curr, chosenIndex);
						borrowed = true;
					} else if (canBorrow(curr, chosenIndex + 1)) {
						borrowFromNext(curr, chosenIndex);
						borrowed = true;
					} else {
						mergeUsing(curr, chosenIndex);
					}
				}

				if (!borrowed) {
					if (isRootEmptyButHasAChild(curr)) {
						root = curr.children[0];
					} else if (curr.children[0].isLeaf()) {
						if (chosenIndex > 0 && chosenIndex < curr.totalChildren) {
							curr.keys[chosenIndex - 1] = curr.children[chosenIndex].keys[0];
						}
					}
				}
			}
			return changed;
		} else {
			return deleteFromLeaf(curr, data);
		}
	}

	private boolean isRootEmptyButHasAChild(Node curr) {
		return curr.totalKeys == 0 && curr == root && !curr.isLeaf();
	}

	private boolean updatedInternalKeyWithSameData(Node curr, T data) {
		int keyIndex = searchKeyIndex(curr, data);

		if (keyIndex < curr.totalKeys) {
			Node temp = curr;
			if (comp.compare(curr.keys[keyIndex], data) == 0) { // internal, most likely a parent of an internal
				while (!temp.isLeaf()) {
					int idx = searchDeleteKeyIndexInternal(temp, data);
					Node next = temp.children[idx];
					if (!next.isLeaf() && next.totalChildren == 1) {
						temp = next.children[0];
					} else {
						temp = next;
					}
				}
				curr.keys[keyIndex] = temp.keys[0];
				return true;
			}
		}
		return false;
	}

	private boolean deleteFromLeaf(Node leaf, T data) {
		int index = searchKeyIndex(leaf, data);
		if (index < leaf.totalKeys && comp.compare(leaf.keys[index], data) == 0) {
			if (index + 1 < leaf.totalKeys) {
				System.arraycopy(leaf.keys, index + 1, leaf.keys, index, leaf.totalKeys - (index + 1));
			}
			leaf.keys[--leaf.totalKeys] = null;
			size--;
			return true;
		}
		return false;
	}

	private boolean canBorrow(Node parent, int index) {
		if (index < 0 || index >= parent.totalChildren) {
			return false;
		}
		return parent.children[index].totalKeys > MIN_KEYS;
	}

	private void borrowFromPrev(Node parent, int currentIndex) {
		Node child = parent.children[currentIndex];
		Node prevNode = parent.children[currentIndex - 1];

		System.arraycopy(child.keys, 0, child.keys, 1, child.totalKeys++);

		T replacementData = prevNode.keys[--prevNode.totalKeys];
		prevNode.keys[prevNode.totalKeys] = null;

		if (child.isLeaf()) {
			child.keys[0] = replacementData;
			parent.keys[currentIndex - 1] = child.keys[0];
		} else {
			child.keys[0] = parent.keys[currentIndex - 1];
			parent.keys[currentIndex - 1] = replacementData;
			System.arraycopy(child.children, 0, child.children, 1, child.totalChildren++);
			child.children[0] = prevNode.children[--prevNode.totalChildren];
			prevNode.children[prevNode.totalChildren] = null;
		}
	}

	private void borrowFromNext(Node parent, int currentIndex) {
		Node child = parent.children[currentIndex];
		Node nextNode = parent.children[currentIndex + 1];

		T nextData = nextNode.keys[0];
		System.arraycopy(nextNode.keys, 1, nextNode.keys, 0, --nextNode.totalKeys);
		nextNode.keys[nextNode.totalKeys] = null;

		if (child.isLeaf()) {
			child.keys[child.totalKeys++] = nextData;
			parent.keys[currentIndex] = nextNode.keys[0];
		} else {
			child.keys[child.totalKeys++] = parent.keys[currentIndex];
			parent.keys[currentIndex] = nextData;
			child.children[child.totalChildren++] = nextNode.children[0];
			System.arraycopy(nextNode.children, 1, nextNode.children, 0, --nextNode.totalChildren);
			nextNode.children[nextNode.totalChildren] = null;
		}
	}

	private void mergeUsing(Node parent, int chosenIndex) {
		if (chosenIndex == 0) { // Merge chosen node it's left
			chosenIndex = 1;
		}
		Node leftChild = parent.children[chosenIndex - 1];
		Node rightChild = parent.children[chosenIndex];

		if (!leftChild.isLeaf()) { // internal to interal, preserve key order
			leftChild.keys[leftChild.totalKeys++] = parent.keys[chosenIndex - 1];
		}

		merge(leftChild, rightChild);

		if (chosenIndex + 1 < parent.totalChildren) {
			System.arraycopy(parent.children, chosenIndex + 1, parent.children, chosenIndex, parent.totalChildren - (chosenIndex + 1));
			System.arraycopy(parent.keys, chosenIndex, parent.keys, chosenIndex - 1, parent.totalKeys - chosenIndex);
		}

		parent.children[--parent.totalChildren] = null;
		parent.keys[--parent.totalKeys] = null;
	}

	private void merge(Node left, Node right) {
		System.arraycopy(right.keys, 0, left.keys, left.totalKeys, right.totalKeys);
		left.totalKeys += right.totalKeys;

		if (left.isLeaf()) {
			left.next = right.next;
			if (right.next != null) {
				right.next.prev = left;
			}
		} else {
			System.arraycopy(right.children, 0, left.children, left.totalChildren, right.totalChildren);
			left.totalChildren += right.totalChildren;
		}
		right.clear();
	}

	public void printLeftToRight() {
		Node current = root;
		for (; !current.isLeaf(); current = current.children[0]);
		for (; current != null; current = current.next) {
			System.out.println(Arrays.toString(current.keys));
		}
	}

	public void printRightToLeft() {
		Node current = root;
		for (; !current.isLeaf(); current = current.children[current.totalChildren - 1]);
		for (; current != null; current = current.prev) {
			System.out.println(Arrays.toString(current.keys));
		}
	}

	public String toString() {
		if (size == 0) {
			return "Is Empty";
		}

		StringBuilder sb = new StringBuilder();
		Queue<Node> q = new LinkedList<>();
		int totalKeysFound = 0;
		q.add(root);
		while (!q.isEmpty()) {
			Node curr = q.remove();
			sb.append(curr + "\n");
			if (curr.isLeaf()) {
				totalKeysFound += curr.totalKeys;
			}
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

	public static void main(String[] args) {
		test3();
		test4();
		test5();
	}

	private static void test5() {
		System.out.println("Testing with ORDER 5");
		BPlusTree<Integer> tree = new BPlusTree<Integer>(5);

		Integer[] nums = { 50, 150, 125, 25, 75, 10, 60, 35, 55, 65, 70, 175, 200, 140, 45, 40, 48, 141, 142, 143, 180,
				185, 190, 61, 62, 63, 80, 81, 82, 83, 84, 85, 100, 110, 120 };
		Arrays.asList(nums).forEach(n -> tree.insert(n));
		for (int i = nums.length - 1; i >= 0; i -= 3) {
			tree.delete(nums[i]);
		}
		for (int i = nums.length - 2; i >= 0; i -= 3) {
			tree.delete(nums[i]);
		}

		for (int i = nums.length - 3; i >= 0; i -= 3) {
			tree.delete(nums[i]);
		}
		System.out.println(tree);
		System.out.println(nums.length);
		// tree.printLeftToRight();
		// tree.printRightToLeft();
		/*
		 * for (int i = nums.length - 1; i > 0; i--) { // leave one elft
		 * tree.delete(nums[i]); //System.out.println(tree);
		 * System.out.println(tree.size); }
		 * 
		 * tree.delete(120); System.out.println(tree); System.out.println(tree.size);
		 */
	}

	private static void test4() {
		System.out.println("Testing with ORDER 4");
		BPlusTree<Integer> tree = new BPlusTree<Integer>(4);
		// Integer[] nums = { 50, 150, 125, 25, 75, 10, 60, 35, 55, 65, 70, 175, 200,
		// 100, 115, 110 };
		// Integer[] nums = { 50, 150, 125, 25, 75, 10, 60, 35,};
		// 1, 2, 3, 4, 21, 22, 23, 24, 25, 26
		Integer[] nums = { 50, 150, 125, 25, 75, 10, 60, 35, 55, 65, 70, 175, 200, 140, 45, 40, 48, 141, 142, 143, 180,
				185, 190, 61, 62, 63, 80, 81, 82, 83, 84, 85, 100, 110, 120 };

		Arrays.asList(nums).forEach(n -> tree.insert(n));

		for (int i = nums.length - 1; i >= 0; i -= 2) {
			tree.delete(nums[i]);
		}

		for (int i = 0; i < nums.length; i++) {
			tree.delete(nums[i]);
		}

		System.out.println(tree);
		System.out.println(nums.length);

	}

	private static void test3() {
		System.out.println("Testing with ORDER 3");
		BPlusTree<Integer> tree = new BPlusTree<Integer>(3);
		// Integer[] nums = { 50, 150, 125, 25, 75, 10, 60, 35, 55, 65, 70, 175, 200,
		// 100, 115, 110 };
		Integer[] nums = { 50, 150, 125, 25, 75, 10, 60, 35, 55, 65, 70, 175, 200, 140, 45, 40, 48, 141, 142, 143, 180,
				185, 190, 61, 62, 63, 80, 81, 82, 83, 84, 85, 100, 110, 120 };
		//
		// Integer[] nums = { 10, 12, 14, 16, 18, 20, 22 };
		Arrays.asList(nums).forEach(n -> tree.insert(n));
		/*
		 * //System.out.println(tree); tree.delete(81); tree.delete(80);
		 * tree.delete(63); tree.delete(62); tree.delete(61); tree.delete(190);
		 * tree.delete(185); tree.delete(180); tree.delete(143); tree.delete(142);
		 * System.out.println("\n\n\n"); tree.delete(141); //tree.delete(48);
		 * //tree.delete(40);
		 */
		for (int i = 0; i < nums.length; i++) {
			tree.delete(nums[i]);
		}
		for (int i = nums.length - 1; i >= 0; i--) {
			tree.delete(nums[i]);
		}
		System.out.println(tree);
		System.out.println(nums.length);
	}
}
