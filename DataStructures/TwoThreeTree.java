import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

//2-3 is a specific form of B Tree
//Good reference: https://www.cs.drexel.edu/~amd435/courses/cs260/lectures/L-6_2-3_Trees.pdf
//There are some simplications on the code that could be done, but I didn't realize it at the time of writing this code.
public class TwoThreeTree<T extends Comparable<? super T>> {
	private class TreeNode {
		List<T> keys;
		List<TreeNode> children;

		public TreeNode() {
			keys = new LinkedList<>();
			children = new LinkedList<>();
		}

		public TreeNode(T data) {
			this();
			keys.add(data);
		}

		public boolean isLeaf() {
			return children.isEmpty();
		}

		public boolean hasToSplitKey() {
			return keys.size() > MAX_KEYS;
		}

		public int totalKeys() {
			return keys.size();
		}

		public int totalChildren() {
			return children.size();
		}

		public String toString() {
			String leafMsg = isLeaf() ? "leaf node\t" : "non-leaf node\t";
			return leafMsg + "keys: " + keys.toString() + ", children: " + children.size();
		}
	}
	
	private static final int MAX_KEYS = 2;
	private Comparator<T> comp;
	private TreeNode root;
	private int size;
	private Map<T, Integer> duplicatesMap;

	public TwoThreeTree() {
		this(Comparator.naturalOrder());
	}

	public TwoThreeTree(Comparator<T> comp) {
		this.comp = comp != null ? comp : Comparator.naturalOrder();
		duplicatesMap = new HashMap<>();
		root = new TreeNode();
	}

	public boolean contains(T data) {
		return contains(root, data);
	}

	private boolean contains(TreeNode current, T data) {
		int chosenIndex = 0;
		while (chosenIndex < current.totalKeys()) {
			int compValue = comp.compare(data, current.keys.get(chosenIndex));
			if (compValue == 0) {
				return true;
			} else if (compValue < 0) {
				break;
			}
			chosenIndex++;
		}
		return current.isLeaf() ? false : contains(current.children.get(chosenIndex), data);
	}

	public void insert(T data) {
		root = insert(root, data);
		size++;
	}

	private TreeNode insert(TreeNode current, T data) {
		if (!current.isLeaf()) {
			TreeNode chosenChild = null;
			int chosenIndex = 0;
			while (chosenIndex < current.totalKeys()) {
				int compValue = comp.compare(data, current.keys.get(chosenIndex));
				if (compValue == 0) {
					addDuplicate(current.keys.get(chosenIndex));
					return current;
				} else if (compValue < 0) {
					break;
				}
				chosenIndex++;
			}

			chosenChild = current.children.get(chosenIndex);
			TreeNode updatedChild = insert(chosenChild, data);

			if (chosenChild != updatedChild) { // must've splitted the child, created new node reference
				current = chosenChild.totalKeys() == 0 ? insertMergeInternal(current, updatedChild, chosenIndex)
						: insertMergeLeaf(current, updatedChild, chosenIndex);
				
				if (current.hasToSplitKey()) {
					current = splitOnInternal(current);
				}
			}
		} else {
			current = addKeyToLeaf(current, data);
		}
		return current;
	}

	private TreeNode insertMergeLeaf(TreeNode parent, TreeNode leafChild, int chosenIndex) {
		parent.keys.add(chosenIndex, leafChild.keys.get(0));
		parent.children.add(chosenIndex + 1, leafChild.children.get(0));
		return parent;
	}

	private TreeNode insertMergeInternal(TreeNode parent, TreeNode nonLeafChild, int chosenIndex) {
		parent.keys.add(chosenIndex, nonLeafChild.keys.get(0));
		parent.children.add(chosenIndex + 1, nonLeafChild.children.get(0));
		parent.children.add(chosenIndex + 2, nonLeafChild.children.get(1));
		parent.children.remove(chosenIndex);
		return parent;
	}

	private TreeNode splitOnInternal(TreeNode node) {
		TreeNode leftChild = new TreeNode(node.keys.remove(0));
		TreeNode newParent = new TreeNode(node.keys.remove(0));
		TreeNode rightChild = new TreeNode(node.keys.remove(0));

		leftChild.children.add(node.children.remove(0));
		leftChild.children.add(node.children.remove(0));
		rightChild.children.add(node.children.remove(0));
		rightChild.children.add(node.children.remove(0));

		newParent.children.add(leftChild);
		newParent.children.add(rightChild);

		return newParent;
	}

	private TreeNode splitOnLeaf(TreeNode node) { // leaf has violates 2 key rule
		TreeNode maxNode = new TreeNode(node.keys.remove(2));
		TreeNode newSplitNode = new TreeNode(node.keys.remove(1));

		if (node == root) { // root is leaf, no parent node to keep reference of node
			newSplitNode.children.add(node);
		}
		newSplitNode.children.add(maxNode);
		return newSplitNode;
	}

	private TreeNode addKeyToLeaf(TreeNode current, T data) {
		boolean dupAdded = false;
		int insertIndex = 0;
		while (insertIndex < current.totalKeys()) {
			int compValue = comp.compare(data, current.keys.get(insertIndex));
			if (compValue == 0) {
				addDuplicate(current.keys.get(insertIndex));
				dupAdded = true;
				break;
			} else if (compValue < 0) {
				break;
			}
			insertIndex++;
		}
		if (!dupAdded) {
			current.keys.add(insertIndex, data);
		}

		return current.hasToSplitKey() ? splitOnLeaf(current) : current;
	}

	private void addDuplicate(T data) {
		if (!duplicatesMap.containsKey(data)) {
			duplicatesMap.put(data, 1);
		} else {
			duplicatesMap.put(data, duplicatesMap.get(data) + 1);
		}
	}

	public boolean delete(T data) {
		int initialSize = size;
		root = delete(root, data);
		return initialSize != size;
	}

	private TreeNode delete(TreeNode current, T data) {
		if (!current.isLeaf()) {
			TreeNode chosenChild = null;
			int chosenIndex = 0;
			while (chosenIndex < current.totalKeys()) {
				int compValue = comp.compare(data, current.keys.get(chosenIndex));
				if (compValue == 0) {
					if (deleteDuplicate(data)) {
						return current;
					}
					data = findInorderSuccessor(current, chosenIndex);
					current.keys.set(chosenIndex, data);
					break;
				} else if (compValue < 0) {
					break;
				}
				chosenIndex++;
			}

			chosenChild = current.children.get(chosenIndex);
			TreeNode updatedChild = delete(chosenChild, data);

			if (updatedChild.keys.isEmpty()) {
				int redistributeIndex = getRedistributeIndex(current, chosenIndex);
				if (updatedChild.isLeaf()) {
					current = redistributeIndex != chosenIndex
							? redistributeLeaf(current, redistributeIndex, chosenIndex)
							: mergeLeaf(current, chosenIndex);
				} else {
					current = redistributeIndex != chosenIndex
							? redistributeInternal(current, redistributeIndex, chosenIndex)
							: mergeInternal(current, chosenIndex);
				}
			}

			if (current == root && current.keys.isEmpty()) { // case where merge propagates back to root
				current = current.children.get(0);
			}
			return current;
		} else {
			if (deleteInLeaf(current, data)) {
				size--;
			}
		}
		return current;
	}

	private int getRedistributeIndex(TreeNode current, int emptyNodeIndex) {
		if (emptyNodeIndex == 0) {
			if (current.children.get(1).totalKeys() == MAX_KEYS) {
				return 1;
			}
		} else if (emptyNodeIndex == current.totalChildren() - 1) {
			if (current.children.get(emptyNodeIndex - 1).totalKeys() == MAX_KEYS) {
				return emptyNodeIndex - 1;
			}
		} else if (current.children.get(emptyNodeIndex - 1).totalKeys() == MAX_KEYS) {
			return emptyNodeIndex - 1;
		} else if (current.children.get(emptyNodeIndex + 1).totalKeys() == MAX_KEYS) {
			return emptyNodeIndex + 1;
		}
		return emptyNodeIndex; // if this case, there is no nodes that can be redistributed
	}

	private TreeNode redistributeLeaf(TreeNode parent, int redistributeChildIndex, int emptyChildIndex) {
		int parentKeyIndex = (redistributeChildIndex + emptyChildIndex) / 2;
		int redistributeKeyIndex = emptyChildIndex < redistributeChildIndex ? 0 : 1; 
		parent.children.get(emptyChildIndex).keys.add(parent.keys.get(parentKeyIndex));
		parent.keys.set(parentKeyIndex, parent.children.get(redistributeChildIndex).keys.remove(redistributeKeyIndex));
		return parent;
	}

	private TreeNode mergeLeaf(TreeNode parent, int emptyChildIndex) {
		if (emptyChildIndex == 0) {
			parent.children.get(1).keys.add(0, parent.keys.remove(0));
		} else if (emptyChildIndex == 1) { // should go left in case there are only 2 children
			parent.children.get(0).keys.add(parent.keys.remove(0));
		} else { // emptyChildIndex == 2
			parent.children.get(1).keys.add(parent.keys.remove(1));
		}
		parent.children.remove(emptyChildIndex);
		return parent;
	}

	private TreeNode redistributeInternal(TreeNode parent, int redistributeChildIndex, int emptyChildIndex) {
		int parentKeyIndex = (redistributeChildIndex + emptyChildIndex) / 2;

		parent.children.get(emptyChildIndex).keys.add(parent.keys.get(parentKeyIndex));
		// empty node has lesser children value than redistribute node, append after
		if (emptyChildIndex < redistributeChildIndex) {
			parent.keys.set(parentKeyIndex, parent.children.get(redistributeChildIndex).keys.remove(0));
			parent.children.get(emptyChildIndex).children.add(1,
					parent.children.get(redistributeChildIndex).children.remove(0));
		} else {
			parent.keys.set(parentKeyIndex, parent.children.get(redistributeChildIndex).keys.remove(1));
			parent.children.get(emptyChildIndex).children.add(0,
					parent.children.get(redistributeChildIndex).children.remove(2));
		}
		return parent;
	}

	private TreeNode mergeInternal(TreeNode parent, int emptyChildIndex) {
		if (emptyChildIndex == 0) {
			parent.children.get(1).keys.add(0, parent.keys.remove(0));
			parent.children.get(1).children.add(0, parent.children.get(emptyChildIndex).children.remove(0));
		} else if (emptyChildIndex == 1) {
			parent.children.get(0).keys.add(parent.keys.remove(0));
			parent.children.get(0).children.add(parent.children.get(emptyChildIndex).children.remove(0));
		} else { // emptyChildIndex == 2
			parent.children.get(1).keys.add(parent.keys.remove(1));
			parent.children.get(1).children.add(parent.children.get(emptyChildIndex).children.remove(0));
		}
		parent.children.remove(emptyChildIndex);
		return parent;
	}

	private boolean deleteInLeaf(TreeNode current, T data) {
		for (int i = 0; i < current.totalKeys(); i++) {
			int compValue = comp.compare(data, current.keys.get(i));
			if (compValue == 0) {
				if (!deleteDuplicate(data)) {
					current.keys.remove(i);
				}
				return true;
			} else if (compValue < 0) {
				break;
			}
		}
		return false;
	}

	private boolean deleteDuplicate(T data) {
		if (duplicatesMap.containsKey(data)) {
			int duplicateAmount = duplicatesMap.get(data) - 1;
			if (duplicateAmount == 0) {
				duplicatesMap.remove(data);
			} else {
				duplicatesMap.put(data, duplicateAmount);
			}
			return true;
		}
		return false;
	}

	private T findInorderSuccessor(TreeNode node, int keyIndex) {
		for (node = node.children.get(keyIndex); !node.isLeaf(); node = node.children.get(node.totalChildren() - 1));
		return node.keys.get(node.totalKeys() - 1);
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public String toString() {
		if (isEmpty()) {
			return "Empty";
		}
		StringBuilder sb = new StringBuilder();
		Queue<TreeNode> q = new LinkedList<>();
		q.add(root);
		int keyCount = 0;
		while (!q.isEmpty()) {
			TreeNode curr = q.remove();

			sb.append(curr + "\n");
			keyCount += curr.totalKeys();
			for (TreeNode child : curr.children) {
				q.add(child);
			}
		}
		sb.append("Duplicates count: " + duplicatesMap + "\n");
		sb.append("keys found: " + keyCount);
		return sb.toString();
	}

	public static void main(String[] args) {
		test3();
	}

	@SuppressWarnings("unused")
	private static void test3() {
		TwoThreeTree<Integer> tree = new TwoThreeTree<>();
		int[] ary = { 100, 90, 80, 70, 60, 50, 40, 30, 20, 10, 5, 15, 25, 110, 120, 65, 68 };

		for (int i = 0; i < ary.length; i++) { //did for loop for debugging
			tree.insert(ary[i]);
		}
		
		System.out.println(tree);
		tree.delete(110);
		tree.delete(100);
		tree.delete(90);
		tree.delete(70);
		tree.delete(20);
		tree.delete(30);
		tree.delete(50);
		tree.delete(80);
		tree.delete(10);
		
		for (int i = 0; i < ary.length-1; i++) {
			tree.delete(ary[i]);
		}

		System.out.println(tree);
		System.out.println(tree.size());
	}

	@SuppressWarnings("unused")
	private static void test2() {
		TwoThreeTree<Integer> tree = new TwoThreeTree<>();
		Integer[] ary = { 10, 50, 100, 80, 90, 40, 30, 20, 70, 60, 45, 65, 75, 110 };
		Arrays.asList(ary).forEach(num -> tree.insert(num));
		System.out.println(tree);
		tree.delete(100);
		System.out.println(tree);
		System.out.println(tree.size());
	}

	@SuppressWarnings("unused")
	private static void test1() {
		TwoThreeTree<Integer> tree = new TwoThreeTree<>();
		
		System.out.println(tree);
		for (int i = 10; i > 0; i--) {
			tree.insert(i);
			tree.insert(i + 20);
			tree.insert(i + 10);
			System.out.println(i + " " + (i + 20) + " " + (i + 10));
		}

		System.out.println(tree);
		System.out.println(tree.size());
		System.out.println(tree.delete(19));
		System.out.println(tree);
		System.out.println(tree.size());
	}
}
