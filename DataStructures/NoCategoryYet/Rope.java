package august_2020;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

//Does not check bounds, could add that if needed
//Used stringbuilder to avoid uncessary string concatenations
//https://en.wikipedia.org/wiki/Rope_(data_structure)
public class Rope {
	protected static class Node {
		Node left;
		Node right;
		int lengthWeight;
		String data;

		public Node() {
		}

		public Node(String str) {
			data = str;
			lengthWeight = str.length();
		}

		private Node(Node left, Node right) {
			if (left.lengthWeight == 0) {
				left = right;
				right = null;
			}

			this.left = left;
			this.right = right;
			lengthWeight = getWeight(left);
		}

		protected static int getWeight(Node leftChild) {
			if (leftChild == null) {
				return 0;
			}
			int weight = leftChild.lengthWeight;
			if (!leftChild.isLeaf() && leftChild.right != null) {
				do {
					weight += leftChild.right.lengthWeight;
					leftChild = leftChild.right;
				} while (leftChild.right != null);
			}
			return weight;
		}

		public boolean isLeaf() {
			return data != null;
		}

		public boolean isEmpty() {
			return lengthWeight == 0;
		}

		public String toString() {
			String dataOutput = data != null ? data : "";
			return "weight: " + lengthWeight + "\tis leaf: " + isLeaf() + "\t" + dataOutput;
		}
	}

	private Node root;
	private int length;
	private StringBuilder sb;

	public void insert(String str, int index) {
		checkExclusiveBoundaries(index);
		if (str == null || str.length() == 0) {
			return;
		}

		Node newNode = new Node(str);

		if (root == null) {
			root = newNode;
		} else {
			if (index == 0) {
				root = concatenate(newNode, root);
			} else if (index == length) {
				root = concatenate(root, newNode);
			} else {
				Node[] splitNodes = split(index, root);
				root = concatenate(splitNodes[0], concatenate(newNode, splitNodes[1]));
			}
		}
		updateLength();
	}

	public void append(String str) {
		insert(str, length);
	}

	public void prepend(String str) {
		insert(str, 0);
	}

	public void delete(int from, int to) {
		checkExclusiveBoundaries(from);
		checkExclusiveBoundaries(to);
		if (root == null) {
			return;
		}

		if (from == 0) {
			if (to == length) {
				clear();
				return;
			}
			Node[] splitNodes = split(to, root);
			root = splitNodes[1];

			if (root.isEmpty()) {
				root = null;
			}
		} else {
			Node[] splitNodesLeft = split(from, root);
			Node[] splitNodesRight = split(to - from, splitNodesLeft[1]);
			root = concatenate(splitNodesLeft[0], splitNodesRight[1]);
		}

		updateLength();
	}

	private Node[] split(int index, Node currRoot) {
		if (index == 0) {
			return new Node[] { new Node(), currRoot };
		}

		Stack<Node> path = new Stack<>();
		Node curr = currRoot;
		while (!curr.isLeaf()) {
			path.add(curr);
			if (curr.lengthWeight <= index && curr.right != null) {
				index -= curr.lengthWeight;
				curr = curr.right;
			} else {
				curr = curr.left;
			}
		}

		Node rightSide = new Node(curr.data.substring(index));
		curr.data = curr.data.substring(0, index);
		curr.lengthWeight = curr.data.length();

		if (!path.isEmpty()) { // Go back up and update the parents weight and unlink right side if not in path
			Node parent = null;
			Queue<Node> prunedNodes = new LinkedList<>();
			while (!path.isEmpty()) {
				parent = path.pop();

				if (parent.right != curr) {
					prunedNodes.add(parent.right);
					parent.right = null;
				} else if (parent.left.isEmpty()) {
					parent.left = parent.right;
					parent.right = null;
				} else if (parent.right.isEmpty()) {
					parent.right = null;
				}

				parent.lengthWeight = Node.getWeight(parent.left);
				curr = parent;
			}

			while (!prunedNodes.isEmpty()) {
				rightSide = concatenate(rightSide, prunedNodes.remove());
			}
		}
		return new Node[] { curr, rightSide };
	}

	public char charAt(int index) {
		checkInclusiveBoundaries(index);
		Node curr = root;
		while (!curr.isLeaf()) {
			if (curr.lengthWeight <= index && curr.right != null) {
				index -= curr.lengthWeight;
				curr = curr.right;
			} else {
				curr = curr.left;
			}
		}
		return curr.data.charAt(index);
	}

	public String report(int start, int end) {
		checkExclusiveBoundaries(end);
		checkExclusiveBoundaries(start);
		if (start >= end || isEmpty()) {
			return "";
		}

		sb = new StringBuilder(end - start);
		report(start, end, root);
		String results = sb.toString();
		sb = null;
		return results;
	}

	private void report(int start, int end, Node current) {
		if (current.isLeaf()) {
			if (end < 1) {
				return;
			}

			if (end > current.lengthWeight) {
				end = current.lengthWeight;
			}

			if (start < 0) {
				start = 0;
			}

			sb.append(current.data.substring(start, end));
			return;
		}

		// These checks are to prune early
		if (end < 1) {
			return;
		}

		if (current.lengthWeight > start) {
			report(start, end, current.left);
		}
		if (current.right != null) {
			report(start - current.lengthWeight, end - current.lengthWeight, current.right);
		}
	}

	private void updateLength() {
		Node current = root;
		length = 0;
		while (current != null) {
			length += current.lengthWeight;
			current = current.right;
		}
	}

	public void clear() {
		length = 0;
		root = null;
	}

	public boolean isEmpty() {
		return length == 0;
	}

	public int length() {
		return length;
	}

	public String toString() {
		return report(0, length);
	}

	private Node concatenate(Node left, Node right) {
		return new Node(left, right);
	}

	private void checkInclusiveBoundaries(int index) {
		if (index < 0 || index >= length) {
			throw new IndexOutOfBoundsException(
					"Index needs to be between: " + 0 + " " + (length - 1) + ", input: " + index);
		}
	}

	private void checkExclusiveBoundaries(int index) {
		if (index < 0 || index > length) {
			throw new IndexOutOfBoundsException("Index needs to be between: " + 0 + " " + length + ", input: " + index);
		}
	}

	private void inorderPrint() {
		inorderPrint(root);
	}

	private void inorderPrint(Node node) {
		if (node != null) {
			inorderPrint(node.left);
			System.out.println(node);
			inorderPrint(node.right);
		}
	}

	private void preorder() {
		preorderPrint(root);
	}

	private void preorderPrint(Node node) {
		if (node != null) {
			System.out.println(node);
			preorderPrint(node.left);
			preorderPrint(node.right);
		}
	}

	public static void main(String[] args) {
		Rope rope = new Rope();
		rope.insert("Hello_", 0);
		rope.append("my_");
		rope.append("na");
		rope.append("me_i");
		rope.append("s");
		rope.append("_Simon");

		String finalInput = "Hello_my_name_is_Simon";
		for (int i = 0; i < finalInput.length(); i++) {
			System.out.println(rope.charAt(i));
		}

		for (int i = 0; i < finalInput.length(); i++) {
			for (int j = i; j <= finalInput.length(); j++) {
				// String result = rope.report(i, j);
				// System.out.println(result.equals(finalInput.substring(i, j)) + "\t" +
				// result);
			}
		}

		rope.prepend("s");
		rope.insert("testing", rope.length() / 2);
		rope.insert("testing", rope.length() / 2);
		rope.insert("testing", rope.length() / 2);
		rope.insert("testing", rope.length() / 2);
		rope.insert("testing", rope.length() / 2);
		rope.insert("testing", rope.length());
		rope.insert("testing", rope.length());
		rope.insert("testing", rope.length());
		rope.insert("testing", rope.length());
		rope.insert("testing", rope.length());
		rope.insert("testing", rope.length());
		rope.insert("testing", rope.length());
		rope.insert("testing", rope.length());
		rope.insert("testing", rope.length());
		rope.insert("testing", rope.length());
		rope.insert("testing", rope.length());
		rope.insert("testing", rope.length());
		rope.insert("testing", rope.length());
		rope.delete(5, rope.length()/2);
		rope.delete(5, rope.length()/2);
		rope.delete(5, rope.length()/2);
		rope.delete(5, rope.length()/2);
		rope.delete(3, rope.length()/2);
		rope.delete(0, rope.length()/2);
		rope.delete(0, rope.length()-2);
		System.out.println(rope.report(0, rope.length));
		rope.inorderPrint();
		//rope.delete(0, rope.length());
		//System.out.println(rope.toString());
		//
		//System.out.println(rope.toString() + "\t" + rope.length());

	}
}
