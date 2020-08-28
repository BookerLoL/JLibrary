package august_2020;

public class ArrayTrie {
	private static final char STARTING_LETTER =  'a';
	private static final int MAX_CHILDREN = 26;
	private static class Node {
		private Node[] childrenLetters;
		private byte count;
		
		public Node() {
			childrenLetters = new Node[MAX_CHILDREN];
		}
		
		private boolean isLeaf() {
			return count == 0;
		}
		
		private Node add(char letter) {
			int indexPos = letter - STARTING_LETTER;
			if (childrenLetters[indexPos] == null) {
				childrenLetters[indexPos] = new Node();
				count++;
			}
			return childrenLetters[indexPos];
		}
	}
	
	private Node root;
	public ArrayTrie() {
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
			temp = temp.childrenLetters[word.charAt(i) - STARTING_LETTER];
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
