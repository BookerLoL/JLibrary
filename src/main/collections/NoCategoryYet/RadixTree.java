package main.collections.NoCategoryYet;
import java.util.Collection;
import java.util.HashMap;

public class RadixTree {
	private class Node {
		private boolean isLeaf;
		private HashMap<Character, Edge> edges;

		public Node(boolean isLeaf) {
			this.isLeaf = isLeaf;
			edges = new HashMap<>();
		}

		public Edge getTransition(char transitionChar) {
			return edges.get(transitionChar);
		}

		public void addEdge(String label, Node next) {
			edges.put(label.charAt(0), new Edge(label, next));
		}

		public int totalEdges() {
			return edges.size();
		}
		
		public Collection<Edge> getEdges() {
			return edges.values();
		}
	}

	private class Edge {
		private String label;
		private Node next;

		public Edge(String label) {
			this(label, new Node(true));
		}

		public Edge(String label, Node next) {
			this.label = label;
			this.next = next;
		}

		public String toString() {
			return "label: " + label + "\t" + next;
		}
	}

	private static final int NO_MISMATCH = -1;
	private Node root;

	public RadixTree() {
		root = new Node(false);
	}

	public void insert(String word) {
		Node current = root;
		int currIndex = 0;
		while (currIndex < word.length()) {
			char transitionChar = word.charAt(currIndex);
			Edge currentEdge = current.getTransition(transitionChar);
			String currStr = word.substring(currIndex);

			if (currentEdge == null) {
				current.edges.put(transitionChar, new Edge(currStr));
				break;
			}

			int splitIndex = getFirstMismatchLetter(currStr, currentEdge.label);
			if (splitIndex == NO_MISMATCH) {
				if (currStr.length() == currentEdge.label.length()) {
					currentEdge.next.isLeaf = true;
					break;
				} else if (currStr.length() < currentEdge.label.length()) {
					String suffix = currentEdge.label.substring(currStr.length());
					currentEdge.label = currStr;
					Node newNext = new Node(true);
					Node afterNewNext = currentEdge.next;
					currentEdge.next = newNext;
					newNext.addEdge(suffix, afterNewNext);
					break;
				} else {
					splitIndex = currentEdge.label.length();
				}
			} else {
				String suffix = currentEdge.label.substring(splitIndex);
				currentEdge.label = currentEdge.label.substring(0, splitIndex);
				Node prevNext = currentEdge.next;
				currentEdge.next = new Node(false);
				currentEdge.next.addEdge(suffix, prevNext);
			}

			current = currentEdge.next;
			currIndex += splitIndex;
		}
	}

	private int getFirstMismatchLetter(String word, String edgeWord) {
		int LENGTH = Math.min(word.length(), edgeWord.length());
		for (int i = 1; i < LENGTH; i++) {
			if (word.charAt(i) != edgeWord.charAt(i)) {
				return i;
			}
		}
		return NO_MISMATCH;
	}

	public boolean search(String word) {
		Node current = root;
		int currIndex = 0;
		while (currIndex < word.length()) {
			char transitionChar = word.charAt(currIndex);
			Edge edge = current.getTransition(transitionChar);
			if (edge == null) {
				return false;
			}

			String currSubstring = word.substring(currIndex);
			if (!currSubstring.startsWith(edge.label)) {
				return false;
			}
			currIndex += edge.label.length();
			current = edge.next;
		}

		return current.isLeaf;
	}

	public void delete(String word) {
		root = delete(root, word);
	}

	private Node delete(Node current, String word) {
		if (word.isEmpty()) {
			if (current.totalEdges() == 0 && current != root) {
				return null;
			}
			current.isLeaf = false;
			return current;
		}

		char transitionChar = word.charAt(0);
		Edge edge = current.getTransition(transitionChar);
		if (edge == null || !word.startsWith(edge.label)) {
			return current;
		}

		Node deleted = delete(edge.next, word.substring(edge.label.length()));
		if (deleted == null) {
			current.edges.remove(transitionChar);
			if (current.totalEdges() == 0 && !current.isLeaf && current != root) {
				return null;
			}
		} else if (deleted.totalEdges() == 1 && !deleted.isLeaf) {
			current.edges.remove(transitionChar);
			for (Edge afterDeleted : deleted.getEdges()) {
				current.addEdge(edge.label + afterDeleted.label, afterDeleted.next);
			}
		}
		return current;
	}

	public void printAllWords() {
		printAllWords(root, "");
	}

	private void printAllWords(Node current, String result) {
		if (current.isLeaf) {
			System.out.println(result);
		}

		for (Edge edge : current.edges.values()) {
			printAllWords(edge.next, result + edge.label);
		}
	}

	public static void main(String[] args) {
		test1();
	}

	private static void test1() {
		RadixTree radix = new RadixTree();
		radix.insert("test");
		radix.insert("water");
		radix.insert("slow");
		radix.insert("slower");
		radix.insert("team");
		radix.insert("tester");
		radix.insert("t");
		radix.insert("toast");
		// radix.printAllWords();

		System.out.println(radix.search("slower"));
		System.out.println(radix.search("t"));
		System.out.println(radix.search("te"));
		System.out.println(radix.search("tes"));
		System.out.println(radix.search("test"));
		System.out.println(radix.search("toast"));
		System.out.println(radix.search("tester"));

		System.out.println("DELETING NOW");
		radix.delete("test");
		radix.delete("t");
		radix.delete("slower");
		radix.delete("wate");

		System.out.println(radix.search("slower"));
		System.out.println(radix.search("t"));
		System.out.println(radix.search("te"));
		System.out.println(radix.search("tes"));
		System.out.println(radix.search("test"));
		System.out.println(radix.search("toast"));
		System.out.println(radix.search("tester"));

		System.out.println();
		radix.printAllWords();
	}

	private static void test2() {
		RadixTree radix = new RadixTree();
		radix.insert("water");
		radix.insert("wate");
		radix.insert("wat");
		radix.insert("wa");
		radix.insert("w");
		//radix.printAllWords();
		System.out.println("\nDELETING\n");
		radix.delete("w");
		radix.delete("wa");
		radix.delete("water");
		radix.delete("wat");
		radix.delete("wate");

		radix.insert("w");
		radix.insert("wat");
		radix.insert("water");
		radix.insert("wate");
		radix.insert("wa");
		radix.insert("watts");
		radix.insert("watoop");
		radix.insert("wope");
		//radix.printAllWords();
		System.out.println("\nDELETING\n");

		radix.delete("w");
		radix.delete("wa");
		radix.delete("wat");
		radix.delete("wope");
		radix.delete("water");
		radix.delete("wate");
		radix.delete("watoop");
		radix.delete("watts");

		
		radix.insert("waterings");
		radix.insert("water");
		radix.insert("watering");
		radix.delete("watering");
		radix.printAllWords();
	}
}
