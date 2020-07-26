import java.util.Comparator;

public class PairingHeap<T extends Comparable<T>> {
	private class Node {
		T data;
		SentinelDoublyLinkedList<Node> children; // should just use java list

		public Node(T data) {
			this.data = data;
			children = new SentinelDoublyLinkedList<>();
		}

		public void addChild(Node node) {
			children.add(node);
		}

		public SentinelDoublyLinkedList<Node> getChildren() {
			return children;
		}
	}

	private Comparator<T> comp;
	private int size;
	private Node head;

	public PairingHeap() {
		this(Comparator.naturalOrder());
	}

	public PairingHeap(Comparator<T> comp) {
		this.comp = comp;
	}

	public void insert(T data) {
		size++;
		head = merge(head, new Node(data));
	}

	private Node merge(Node h1, Node h2) {
		if (h1 == null) {
			return h2;
		} else if (h2 == null) {
			return h1;
		}

		if (compare(h1.data, h2.data) >= 0) {
			h1.addChild(h2);
			return h1;
		} else {
			h2.addChild(h1);
			return h2;
		}
	}

	private int compare(T data1, T data2) {
		if (data1 == null) {
			return -1;
		} else if (data2 == null) {
			return 1;
		}

		return comp.compare(data1, data2);
	}

	private Node twoPassMerge(SentinelDoublyLinkedList<Node> nodes) {
		if (nodes.isEmpty()) {
			return null;
		} else if (nodes.getSize() == 1) {
			return nodes.remove(0);
		} else {
			return merge(merge(nodes.remove(0), nodes.remove(0)), twoPassMerge(nodes));
		}
	}

	public T remove() {
		if (isEmpty()) {
			return null;
		}
		T data = head.data;
		head = twoPassMerge(head.getChildren());
		size--;
		return data;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}
}
