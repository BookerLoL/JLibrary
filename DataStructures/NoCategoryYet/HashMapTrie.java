package august_2020;

import java.util.Map;
import java.util.HashMap;

public class HashMapTrie {
	private static class Node {
		private Map<Character, Node> childrenLetters;
		
		public Node() {
			childrenLetters = new HashMap<>();
		}
		
		private boolean isLeaf() {
			return childrenLetters.isEmpty();
		}
		
		private Node add(char letter) {
			return childrenLetters.compute(letter, (k, v) -> v == null ? new Node() : v);
		}
	}
	
	private Node root;
	public HashMapTrie() {
		root = new Node();
	}
	
	public void insert(String word) {
		Node temp = root;
		for (int i = 0; i < word.length(); i++) {
			temp = temp.add(word.charAt(i));  
		}
	}
	
	private Node getNode(String word) {
		Node temp = root;
		for (int i = 0; i < word.length() && temp != null; i++) {
			temp = temp.childrenLetters.getOrDefault(word.charAt(i), null);
		}
		return temp;
	}
	
	public boolean search(String word) {
		Node result = getNode(word);
		return result != null && result.isLeaf();
	}
	
	public boolean startsWith(String prefix) {
		return getNode(prefix) != null;
	}
}
