package main.collections.List;
public class SentinelLinkedList<T> {
	protected class Node {
		private T data;
		private Node next;

		public Node(T data, Node next) {
			this.data = data;
			this.next = next;
		}

		public T setData(T newData) {
			T prev = data;
			data = newData;
			return prev;
		}
	}

	private Node head;
	private int size;

	public SentinelLinkedList() {
		head = new Node(null, null);
		size = 0;
	}

	public boolean add(T data) {
		return add(getSize(), data);
	}

	public boolean add(int index, T data) {
		checkExclusiveBounds(index);

		Node prev = getNode(index - 1);
		Node newNext = new Node(data, prev.next);
		prev.next = newNext;
		size++;
		return true;
	}

	public boolean remove(T data) {
		if (isEmpty()) {
			return false;
		}

		Node temp = head.next;
		Node prev = head;

		if (data == null) {
			for (; temp != null && temp.data != data; prev = temp, temp = temp.next);
		} else {
			for (; temp != null && !data.equals(temp.data); prev = temp, temp = temp.next);
		}
		
		if (temp != null) {
			prev.next = temp.next;
			size--;
			return true;
		} 
		return false;
	}

	public T remove(int index) {
		checkInclusiveBounds(index);

		Node temp = getNode(index - 1);
		T prev = temp.next.data;
		temp.next = temp.next.next;

		size--;
		return prev;
	}

	public int indexOf(T data) {
		Node temp = head.next;
		int i = 0;
		
		if (data == null) {
			for (; temp != null && temp.data != data; temp = temp.next, i++);
		} else {
			for (; temp != null && !data.equals(temp.data); temp = temp.next, i++);
		}
		
		return temp != null ? i : -1;
	}
	
	public T get(int index) {
		checkInclusiveBounds(index);
		return getNode(index).data;
	}

	public T set(int index, T newData) {
		checkInclusiveBounds(index);
		return getNode(index).setData(newData);
	}

	public boolean contains(T data) {
		return indexOf(data) != -1;
	}
	private Node getNode(int index) {
		checkHeadToNullExclusiveBounds(index);
		Node temp = head;
		while (index != -1) {
			temp = temp.next;
			index--;
		}
		return temp;
	}
	
	private void checkInclusiveBounds(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	private void checkExclusiveBounds(int index) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	private void checkHeadToNullExclusiveBounds(int index) {
		if (index < -1 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	public boolean isEmpty() {
		return size == 0;
	}

	public int getSize() {
		return size;
	}

	public void clear() {
		head.next = null;
		size = 0;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node temp = head.next;
		while (temp != null) {
			sb.append(temp.data + ",");
			temp = temp.next;
		}
		if (sb.length() != 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(']');
		return sb.toString();
	}
}
