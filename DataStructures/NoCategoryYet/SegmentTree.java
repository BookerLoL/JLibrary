package august_2020;

import java.util.LinkedList;
import java.util.Queue;

public class SegmentTree {
	private static class Node {
		int from;
		int to;
		Node left;
		Node right;
		int total;

		public Node(int from, int to) {
			this.from = from;
			this.to = to;
		}

		public String toString() {
			return "[" + from + "-" + to + "]" + " sum: " + total;
		}
	}

	private Node root;
	private int size;

	public SegmentTree(int[] values) {
		root = constructTree(values, 0, values.length - 1);
		size = values.length;
	}

	private Node constructTree(int[] values, int minIndex, int maxIndex) {
		Node current = new Node(minIndex, maxIndex);

		if (minIndex == maxIndex) {
			current.total = values[minIndex];
		} else {
			current.left = constructTree(values, minIndex, (minIndex + maxIndex) / 2);
			current.right = constructTree(values, (minIndex + maxIndex) / 2 + 1, maxIndex);
			current.total = current.left.total + current.right.total;
		}
		return current;
	}

	public void set(int index, int value) {
		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException("Index is not within bounds");
		}

		root = set(index, value, root);
	}

	private Node set(int selectedIndex, int value, Node current) {
		if (current.from == current.to && current.from == selectedIndex) {
			current.total = value;
		} else {
			int mid = (current.from + current.to) / 2;
			if (selectedIndex <= mid) {
				current.left = set(selectedIndex, value, current.left);
			} else {
				current.right = set(selectedIndex, value, current.right);
			}
			current.total = current.left.total + current.right.total;
		}
		return current;
	}
	
	public int sumOf(int rangeFrom, int rangeTo) {
		if (rangeFrom < 0 || rangeTo >= size || rangeFrom > rangeTo) {
			throw new IllegalArgumentException("Range from should be:  0 <= rangeFrom <= rangeTo < size");
		}
		
		return sumOf(root, rangeFrom, rangeTo);
	}
	
	private int sumOf(Node current, int fromIndex, int toIndex) {
		if (current.from == fromIndex && current.to == toIndex) {
			return current.total;
		}
		
		int leftSideMaxIndex = (current.from + current.to) / 2;
		if (fromIndex <= leftSideMaxIndex && toIndex > leftSideMaxIndex) {
			return sumOf(current.left, fromIndex, leftSideMaxIndex) + sumOf(current.right, leftSideMaxIndex + 1, toIndex);
		} else if (toIndex <= leftSideMaxIndex) {
			return sumOf(current.left, fromIndex, toIndex);
		} else {
			return sumOf(current.right, fromIndex, toIndex);
		}
	}
	
	public int size() {
		return size;
	}

 	public void levelOrder() {
		Queue<Node> q = new LinkedList<>();
		q.add(root);
		while (!q.isEmpty()) {
			Node curr = q.poll();
			System.out.println(curr);
			if (curr.left != null) {
				q.add(curr.left);
			}
			if (curr.right != null) {
				q.add(curr.right);
			}
		}
	}

	public static void main(String[] args) {
		int[] nums = { 2, 3, 4, 1, 2, 8, 7, 4 };
		SegmentTree tree = new SegmentTree(nums);
		tree.levelOrder();
		System.out.println(tree.sumOf(3, 7));
	}
}
