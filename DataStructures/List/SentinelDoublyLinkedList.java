public class SentinelDoublyLinkedList<T> {
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

		public T setData(T newData) {
			T prev = data;
			data = newData;
			return prev;
		}
	}

	private Node head;
	private Node tail;
	private int size;

	public SentinelDoublyLinkedList() {
		head = new Node(null);
		tail = new Node(null);
		clear();
	}

	public boolean add(T data) {
		return add(getSize(), data);
	}

	public boolean add(int index, T data) {
		checkExclusiveBounds(index);

		Node next = getNode(index);
		Node prev = next.prev;

		Node newNode = new Node(data, prev, next);
		prev.next = newNode;
		next.prev = newNode;

		size++;
		return true;
	}

	public boolean remove(T data) {
		Node temp = head.next;
		if (data == null) {
			while (temp != tail) {
				if (temp.data == data) {
					break;
				}
				temp = temp.next;
			}
		} else {
			while (temp != tail) {
				if (data.equals(temp.data)) {
					break;
				}
				temp = temp.next;
			}
		}

		if (temp != tail) {
			removeHelper(temp);
			return true;
		} else {
			return false;
		}
	}

	public T remove(int index) {
		checkInclusiveBounds(index);
		return removeHelper(getNode(index));
	}

	private T removeHelper(Node toRemove) {
		toRemove.prev.next = toRemove.next;
		toRemove.next.prev = toRemove.prev;
		size--;
		return toRemove.data;
	}

	public int indexOf(T data) {
		if (isEmpty()) {
			return -1;
		}

		int index = 0;
		Node temp = head.next;
		while (temp != tail) {
			if (temp.data.equals(data)) {
				break;
			}
			temp = temp.next;
			index++;
		}

		return temp != tail ? index : -1;
	}

	public T get(int index) {
		checkInclusiveBounds(index);
		return getNode(index).data;
	}

	public T set(int index, T newData) {
		checkInclusiveBounds(index);
		return getNode(index).setData(newData);
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

	private void checkHeadTailsBounds(int index) {
		if (index < -1 || index > size) {
			throw new IndexOutOfBoundsException();
		}
	}

	private Node getNode(int index) {
		checkHeadTailsBounds(index);
		
		Node temp = null;
		int i;
		boolean isUpperHalf = index >= (getSize() / 2);
		if (isUpperHalf) {
			for (temp = tail, i = size; i > index; i--, temp = temp.prev);
		} else {
			for (temp = head, i = -1; i < index; i++, temp = temp.next);
		}
		return temp;
	}

	public boolean contains(T data) {
		return indexOf(data) != -1;
	}

	public void clear() {
		head.next = tail;
		tail.prev = head;
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int getSize() {
		return size;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node temp = head.next;
		while (temp != tail) {
			sb.append(temp.data + ",");
			temp = temp.next;
		}
		if (!isEmpty()) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(']');
		return sb.toString();
	}
}
