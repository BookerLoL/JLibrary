package main.collections.NoCategoryYet;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
 *  Assumes that there are no duplicate intervals using duplicate left values, example: 1-2, 1-5
 *  Definition of overlap needs to be defined clearly
 * 
 *  references: https://en.wikipedia.org/wiki/Interval_tree, https://www.geeksforgeeks.org/interval-tree/
 */
public class IntervalTree<T extends Comparable<? super T>> {
	protected class Interval {
		private T low;
		private T high;

		public Interval(T low, T high) {
			this.low = low;
			this.high = high;
		}

		public String toString() {
			return "Interval: " + low.toString() + " - " + high.toString();
		}
	}

	private class Node {
		private Interval interval;
		private T max;
		private Node left;
		private Node right;

		public Node(Interval interval) {
			this.interval = interval;
			max = interval.high;
		}

		public String toString() {
			return interval + "\tmax: " + max.toString();
		}
	}

	public Interval create(T low, T high) {
		return new Interval(low, high);
	}

	private Node root;

	public void insert(Interval interval) {
		root = insert(root, interval);
	}

	private Node insert(Node current, Interval interval) {
		if (current == null) {
			return new Node(interval);
		}

		if (current.interval.low.compareTo(interval.low) > 0) {
			current.left = insert(current.left, interval);
		} else {
			current.right = insert(current.right, interval);
		}

		if (current.max.compareTo(interval.high) < 0) {
			current.max = interval.high;
		}

		return current;
	}

	public void delete(Interval interval) {
		root = delete(root, interval);
	}

	private Node delete(Node current, Interval interval) {
		if (current == null) {
			return current;
		}

		int lowDiff = current.interval.low.compareTo(interval.low);
		if (lowDiff > 0) {
			current.left = delete(current.left, interval);
		} else if (lowDiff < 0) {
			current.right = delete(current.right, interval);
		} else {
			if (current.interval.high.compareTo(interval.high) == 0) {
				if (current.left == null) {
					return current.right;
				} else if (current.right == null) {
					return current.left;
				} else {
					Node replacement = inorderSuccessor(current);
					current.interval = replacement.interval;
					current.right = delete(current.right, replacement.interval);
				}
			}
		}
		current.max = max(current.interval.high, current.right != null ? current.right.max : null,
				current.left != null ? current.left.max : null);
		return current;
	}

	@SafeVarargs
	private T max(T... inputs) {
		T best = inputs[0];
		for (int i = 1; i < inputs.length; i++) {
			if (inputs[i] != null && best.compareTo(inputs[i]) < 0) {
				best = inputs[i];
			}
		}
		return best;
	}

	private Node inorderSuccessor(Node current) {
		return current.right != null ? findMin(current.right) : null;
	}

	private Node findMin(Node current) {
		for (; current.left != null; current = current.left)
			;
		return current;
	}

	public boolean overlaps(Interval i1, Interval i2) {
		return i1.high.compareTo(i2.low) >= 0 && i1.low.compareTo(i2.high) <= 0;
	}

	public List<Interval> findAllOverlaps(Interval interval) {
		return findAllOverlapsHelper(root, interval);
	}

	private List<Interval> findAllOverlapsHelper(Node current, Interval interval) {
		if (current == null) {
			return new LinkedList<>();
		}

		List<Interval> allIntervals = new LinkedList<>();
		if (overlaps(current.interval, interval)) {
			allIntervals.add(current.interval);
		}

		// this section could be refined to prune a tree depending on the interval low
		// value of the current node
		allIntervals.addAll(findAllOverlapsHelper(current.left, interval));
		allIntervals.addAll(findAllOverlapsHelper(current.right, interval));

		return allIntervals;
	}

	public void printPreorder() {
		printPreorder(root);
	}

	private void printPreorder(Node current) {
		if (current != null) {
			System.out.println(current);
			printPreorder(current.left);
			printPreorder(current.right);
		}
	}

	public static void main(String[] args) {
		test1();
	}

	private static void test1() {
		IntervalTree<Integer> test = new IntervalTree<>();
		Integer[][] ins = { { 15, 20 }, { 10, 30 }, { 17, 19 }, { 5, 20 }, { 12, 15 }, { 30, 40 } };
		Arrays.asList(ins).forEach(input -> test.insert(test.create(input[0], input[1])));
		test.printPreorder();
		test.delete(test.create(30, 40));
		System.out.println(test.findAllOverlaps(test.create(18, 25)));
		test.printPreorder();
	}
}
