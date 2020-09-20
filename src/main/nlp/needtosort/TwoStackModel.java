package main.nlp.needtosort;

//Similar to split buffer, data structure for text editors
public class TwoStackModel {
	private static class Stack<T> {
		protected static class Node<T> {
			Node<T> next;
			T data;

			public Node(T data, Node<T> next) {
				this.data = data;
				this.next = next;
			}
		}

		
		private Node<T> head;
		private int size;

		public void push(T item) {
			head = new Node<T>(item, head);
			size++;
		}

		public T pop() {
			T item = head.data;
			head = head.next;
			size--;
			return item;
		}

		public T peek() {
			return head.data;
		}
	   
		public String getFrontToBack() {
			return getStackContent().toString();
		}

		public String getBackToFront() {
			return getStackContent().reverse().toString();
		}

		private StringBuilder getStackContent() {
			StringBuilder sb = new StringBuilder(size);
			for (Node<T> curr = head; curr != null; curr = curr.next) {
				sb.append(curr.data);
			}
			return sb;
		}
	   
	    public boolean isEmpty() {
			return head == null;
		}

		public int size() {
			return size;
		}
	}
	
	public static final char EMPTY_CHAR = '\0';
	private Stack<Character> left;
	private Stack<Character> right;

	public TwoStackModel() {
		left = new Stack<>();
		right = new Stack<>();
	}

	public void insert(char ch) {
		left.push(ch);
	}

	public void insert(String input) {
		for (int i = 0; i < input.length(); i++) {
			left.push(input.charAt(i));
		}
	}

	public char delete() {
		return left.isEmpty() ? EMPTY_CHAR : left.pop();
	}

	public void moveLeft(int amount) {
		while (!left.isEmpty() && amount > 0) {
			right.push(left.pop());
			amount--;
		}
	}

	public void moveRight(int amount) {
		while (!right.isEmpty() && amount > 0) {
			left.push(right.pop());
			amount--;
		}
	}
	
	public char get() {
		return left.isEmpty() ? EMPTY_CHAR : left.peek();
	}

	public String concatenate() {
		return left.getBackToFront() + right.getFrontToBack();
	}

	public int size() {
		return left.size() + right.size();
	}

	public static void main(String[] args) {
		TwoStackModel buffer = new TwoStackModel();
		buffer.insert("OpenGenus");
		buffer.moveLeft(2);
		buffer.delete();
		System.out.println(buffer.get() + "\t" + buffer.size());
		buffer.delete();
		buffer.moveLeft(3);
		System.out.println(buffer.get() + "\t" + buffer.size());
		System.out.println(buffer.concatenate());
	}
}
