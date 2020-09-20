package main.collections.List;
import java.util.Comparator;
import java.util.Iterator;

public class OrderedLinkedList<T extends Comparable<T>> implements Iterable<T> {
	private class Node {
		T data;
		Node next;

		public Node(T data, Node next) {
			this.data = data;
			this.next = next;
		}

		public String toString() {
			return data.toString();
		}
	}

	private Comparator<T> comp;
	private int size;
	private Node head;

	public OrderedLinkedList() {
		this(Comparator.naturalOrder());
	}

	public OrderedLinkedList(Comparator<T> comp) {
		this.comp = comp;
	}

	public void add(T data) {
		if (head == null || comp.compare(data, head.data) < 0) {
			head = new Node(data, head);
		} else {
			Node curr = head;
			while (curr.next != null && comp.compare(curr.data, data) < 0) {
				curr = curr.next;
			}

			curr.next = new Node(data, curr.next);
		}
		size++;
	}

	public boolean contains(T data) {
		return getNodeByData(data) != null;
	}

	public void remove(T data) {
		if (isEmpty()) {
			return;
		}

		if (comp.compare(data, head.data) == 0) {
			head = head.next;
			size--;
		} else {
			Node temp = head;
			while (temp.next != null && comp.compare(temp.next.data, data) < 0) {
				temp = temp.next;
			}

			if (temp.next != null && comp.compare(temp.next.data, data) == 0) {
				temp.next = temp.next.next;
				size--;
			}
		}
	}

	public void set(T oldData, T newData) {
		Node node = getNodeByData(oldData);
		if (node != null) {
			node.data = newData;
		}
	}

	private Node getNodeByData(T data) {
		if (data == null) {
			for (Node temp = head; temp != null; temp = temp.next) {
				if (data == temp.data) {
					return temp;
				}
			}
		} else {
			for (Node temp = head; temp != null; temp = temp.next) {
				int compVal = comp.compare(data, temp.data);
				if (compVal == 0) {
					return temp;
				} else if (compVal > 0) {
					break;
				}
			}
		}
		return null;
	}

	private Node getNodeByIndex(int i) {
		if (i < 0 || i >= size()) {
			return null;
		}
		Node temp = head;
		while (i != 0) {
			temp = temp.next;
			i--;
		}
		return temp;
	}

	public void set(int index, T newData) {
		Node node = getNodeByIndex(index);
		if (node != null) {
			node.data = newData;
		}
	}

	public T get(int i) {
		Node node = getNodeByIndex(i);
		return node != null ? node.data : null;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node temp = head;
		while (temp != null) {
			sb.append(temp + ", ");
			temp = temp.next;
		}
		return sb.toString();
	}

	private class ListIterator implements Iterator<T> {
		private Node curr = head;

		@Override
		public boolean hasNext() {
			return curr != null;
		}

		@Override
		public T next() {
			T data = curr.data;
			curr = curr.next;
			return data;
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new ListIterator();
	}
}