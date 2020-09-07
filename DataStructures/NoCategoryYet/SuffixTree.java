package august_2020;

import java.util.HashMap;

public class SuffixTree {
	private class Pair {
		String prefix;
		Node nextNode;

		public Pair(String prefix, Node next) {
			this.prefix = prefix;
			nextNode = next;
		}

		public String toString() {
			return "transition: " + prefix + "\t\t" + nextNode;
		}
	}

	private class Node {
		public HashMap<Character, Pair> prefixTransitions;

		public Node() {
			prefixTransitions = new HashMap<>();
		}

		public boolean isTerminating() {
			return prefixTransitions.isEmpty();
		}

		public boolean hasTransition(char ch) {
			return prefixTransitions.containsKey(ch);
		}

		public void addTransition(String prefix) {
			prefixTransitions.put(prefix.charAt(0), new Pair(prefix, new Node()));
		}

		public void addTransition(String prefix, Node next) {
			prefixTransitions.put(prefix.charAt(0), new Pair(prefix, next));
		}

		public Pair get(char key) {
			return prefixTransitions.get(key);
		}

		public String toString() {
			return "Node is leaf: " + isTerminating();
		}
	}

	private Node root;

	public SuffixTree(String input) {
		root = new Node();
		constructTree(input);
	}

	private void constructTree(String input) {
		Node current;
		Pair transitionPair;
		String currSuffix;
		char transitionLetter;

		for (int i = 0; i < input.length(); i++) {
			current = root;
			currSuffix = input.substring(i);

			while (currSuffix.length() > 0) {
				transitionLetter = currSuffix.charAt(0);

				if (!current.hasTransition(transitionLetter)) {
					current.addTransition(currSuffix);
					break;
				} else {
					transitionPair = current.get(transitionLetter);
					int splitIndex = findFirstMismatch(transitionPair.prefix, currSuffix);

					if (splitIndex == -1) {
						splitIndex = transitionPair.prefix.length();
					} else { // Need to split up transition
						String newPrefix = transitionPair.prefix.substring(0, splitIndex);
						String newSuffix = transitionPair.prefix.substring(splitIndex);
						Node prevNext = transitionPair.nextNode;

						transitionPair.prefix = newPrefix;
						transitionPair.nextNode = new Node();
						transitionPair.nextNode.addTransition(newSuffix, prevNext);
					}

					current = transitionPair.nextNode;
					currSuffix = currSuffix.substring(splitIndex);
				}
			}
		}
	}

	private int findFirstMismatch(String prefix, String str) {
		int maxLength = Math.min(prefix.length(), str.length());
		// first char is guaranteed to be the same
		for (int i = 1; i < maxLength; i++) {
			if (str.charAt(i) != prefix.charAt(i)) {
				return i;
			}
		}
		return -1; // full matched
	}

	public static void main(String[] args) {
		SuffixTree suffixTree = new SuffixTree("Mississippi$");
	}
}
