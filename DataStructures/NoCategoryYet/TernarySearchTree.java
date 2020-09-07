
public class TernarySearchTree {
	private static class Node {
		char letter;
		boolean wordFlag;
		Node left, right, equal;

		public Node(char letter) {
			this.letter = letter;
		}

		public String toString() {
			return "Node: " + letter + "\t" + wordFlag;
		}
	}

	private Node root;

	public void insert(String word) {
		if (word == null || word.length() < 1) {
			return;
		}

		if (root == null) {
			root = new Node(word.charAt(0));
		}

		root = insert(root, word, 0);
	}

	private Node insert(Node current, String word, int index) {
		if (current == null) {
			current = new Node(word.charAt(index));
		}

		char letter = word.charAt(index);
		if (letter < current.letter) {
			current.left = insert(current.left, word, index);
		} else if (letter > current.letter) {
			current.right = insert(current.right, word, index);
		} else {
			if (index == word.length() - 1) {
				current.wordFlag = true;
				return current;
			}
			current.equal = insert(current.equal, word, index + 1);
		}
		return current;
	}

	public boolean search(String word) {
		if (word == null) {
			return false;
		} else if (word.length() == 0) {
			return true;
		} else if (root == null) {
			return false;
		}

		Node current = root;
		int index = 0;
		while (current != null) {
			char letter = word.charAt(index);
			if (letter < current.letter) {
				current = current.left;
			} else if (letter > current.letter) {
				current = current.right;
			} else {
				index++;
				if (index == word.length() && current.wordFlag) {
					return true;
				}
				current = current.equal;
			}
		}
		return false;
	}

	public void printAllWords() {
		printAllWords(root, "");
	}

	private void printAllWords(Node curr, String result) {
		if (curr != null) {
			printAllWords(curr.left, result);

			if (curr.wordFlag) {
				System.out.println(result + curr.letter);
			}

			printAllWords(curr.equal, result + curr.letter);
			printAllWords(curr.right, result);
		}
	}

	public static void main(String[] args) {
		TernarySearchTree tst = new TernarySearchTree();
		tst.insert("cat");
		tst.insert("up");
		tst.insert("bug");
		tst.insert("cats");
		tst.insert("a");
		tst.printAllWords();
		System.out.println();
		System.out.println(tst.search("cats"));
		System.out.println(tst.search("catss"));
		System.out.println(tst.search("a"));
		System.out.println(tst.search("z"));
	}
}
