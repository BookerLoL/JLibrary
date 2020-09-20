package main.collections.List;
public class DoublyLinkedList<T> {
	protected class Node {
		private T data;
		private Node prev;
		private Node next;

		public Node(T data) {
			this(data, null, null);
		}

		public Node(T data, Node prev, Node next) {
			this.data = data;
			this.prev = prev;
			this.next = next;
		}

		public T getData() {
			return data;
		}

		public Node getPrev() {
			return prev;
		}

		public Node getNext() {
			return next;
		}

		public T setData(T newData) {
			T prev = getData();
			data = newData;
			return prev;
		}

		public void setPrev(Node newPrev) {
			prev = newPrev;
		}

		public void setNext(Node newNext) {
			next = newNext;
		}
	}

	private Node head;
	private Node tail;
	private int size;
	
	public DoublyLinkedList() {
		clear();
	}

	public boolean add(T data) {
		return add(getSize(), data);
	}

	public boolean add(int index, T data) {
		if (index < 0 || index > getSize()) {
			throw new IndexOutOfBoundsException();
		}

		Node newNode = new Node(data);
		if (isEmpty()) {
			head = newNode;
			tail = newNode;
		} else if (index == 0) {
			newNode.setNext(head);
			head.setPrev(newNode);
			head = newNode;
		} else if (index == getSize()) {
			newNode.setPrev(tail);
			tail.setNext(newNode);
			tail = newNode;
		} else {
			Node next = getNode(index);
			newNode.setPrev(next.getPrev());
			newNode.setNext(next);
			next.getPrev().setNext(newNode);
			next.setPrev(newNode);
		}
		size++;
		return true;
	}

	public boolean remove(T data) {
		Node temp = head;
		while (temp != null) {
			if (temp.getData().equals(data)) {
				break;
			}
			temp = temp.getNext();
		}

		if (temp != null) {
			removeHelper(temp);
			return true;
		} else {
			return false;
		}
	}

	public T remove(int index) {
		return removeHelper(getNode(index));
	}

	private T removeHelper(Node toRemove) {
		if (getSize() == 1) {
			head = null;
			tail = null;
		} else if (toRemove == head) {
			head = head.getNext();
			head.setPrev(null);
		} else if (toRemove == tail) {
			tail = tail.getPrev();
			tail.setNext(null);
		} else {
			toRemove.getPrev().setNext(toRemove.getNext());
			toRemove.getNext().setPrev(toRemove.getPrev());
		}

		size--;
		return toRemove.getData();
	}

	public int indexOf(T data) {
		if (isEmpty()) {
			return -1;
		}

		int index = 0;
		Node temp = head;
		while (temp != null) {
			if (temp.getData().equals(data)) {
				break;
			}
			temp = temp.getNext();
			index++;
		}

		return temp != null ? index : -1;
	}

	public T get(int index) {
		return getNode(index).getData();
	}

	public T set(int index, T newData) {
		return getNode(index).setData(newData);
	}

	private Node getNode(int index) {
		if (index < 0 || index >= getSize()) {
			throw new IndexOutOfBoundsException();
		}

		boolean isUpperHalf = index >= (getSize() / 2);
		Node temp = null;
		int i;
		if (isUpperHalf) {
			temp = tail;
			i = getSize() - 1;
			while (i != index) {
				temp = temp.getPrev();
				i--;
			}
		} else {
			temp = head;
			i = 0;
			while (i != index) {
				temp = temp.getNext();
				i++;
			}
		}
		return temp;
	}

	public boolean contains(T data) {
		return indexOf(data) != -1;
	}

	public void clear() {
		head = null;
		tail = null;
		size = 0;
	}

	public boolean isEmpty() {
		return getSize() == 0;
	}

	public int getSize() {
		return size;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node temp = head;
		while (temp != null) {
			sb.append(temp.getData() + ",");
			temp = temp.getNext();
		}
		if (!isEmpty()) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(']');
		return sb.toString();
	}
}
