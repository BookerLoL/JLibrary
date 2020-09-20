package main.util.compression;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

//Reference: https://en.wikipedia.org/wiki/Huffman_coding
//Encode overrides the previous encoding
public class Huffman {
	private class Node {
		char letter;
		int freq;
		Node left;
		Node right;

		public Node(Node left, Node right) {
			letter = '\0';
			this.left = left;
			this.right = right;
			this.freq = left.freq + right.freq;
		}

		public Node(char letter, int freq) {
			this.letter = letter;
			this.freq = freq;
		}

		public String toString() {
			return "(" + letter + "," + freq + ")";
		}

		public boolean isLeaf() {
			return letter != '\0';
		}
	}

	private Node root;

	public String encode(String entireText) {
		HashMap<Character, Integer> freqCount = new HashMap<>();
		char[] letters = entireText.toCharArray();
		for (char letter : letters) {
			freqCount.compute(letter, (k, v) -> v == null ? 1 : v + 1);
		}

		PriorityQueue<Node> q = new PriorityQueue<>(freqCount.size(), Comparator.comparingInt(l -> l.freq));
		for (Entry<Character, Integer> letterFreqPair : freqCount.entrySet()) {
			q.add(new Node(letterFreqPair.getKey(), letterFreqPair.getValue()));
		}

		while (q.size() != 1) {
			q.add(new Node(q.poll(), q.poll()));
		}
		root = q.poll();

		Map<Character, String> mapping = getEncodings();
		StringBuilder sb = new StringBuilder();
		for (char letter : letters) {
			sb.append(mapping.get(letter));
		}
		return sb.toString();
	}

	public Map<Character, String> getEncodings() {
		HashMap<Character, String> mapping = new HashMap<>();
		if (root == null) {
			return mapping;
		}

		findEncodings(root, "", mapping);
		return mapping;
	}

	private void findEncodings(Node current, String encoding, Map<Character, String> mapping) {
		if (current.isLeaf()) {
			mapping.put(current.letter, encoding);
		} else {
			findEncodings(current.left, encoding + "0", mapping);
			findEncodings(current.right, encoding + "1", mapping);
		}
	}

	public String decode(String encoding) {
		StringBuilder sb = new StringBuilder();
		Node current = root;
		int index = 0;
		while (index < encoding.length()) {
			while (!current.isLeaf()) {
				current = encoding.charAt(index) == '0' ? current.left : current.right;
				index++;
			}
			sb.append(current.letter);
			current = root;
		}
		return sb.toString();
	}
}
