import java.util.AbstractList;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularLinkedList<E> extends AbstractList<E> implements Deque<E>, Cloneable {
	protected class Node {
		private E data;
		private Node next;

		public Node(E data, Node next) {
			this.data = data;
			this.next = next;
		}

		public E setData(E newData) {
			E prev = data;
			data = newData;
			return prev;
		}

		public String toString() {
			return data.toString();
		}
	}

	private Node last;
	private int size;
	private int maxCapacity;
	private boolean isLimited;

	public CircularLinkedList() {
		this(10, false);
	}

	public CircularLinkedList(int initialCapacity) {
		this(initialCapacity, false);
	}

	public CircularLinkedList(int initialCapacity, boolean isLimited) {
		maxCapacity = initialCapacity <= 0 ? 10 : initialCapacity;
		this.isLimited = isLimited;
	}

	public boolean add(E data) {
		return addHelper(size, data);
	}

	public void add(int index, E data) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}

		addHelper(index, data);
	}

	private boolean addHelper(int index, E data) {
		if (size == maxCapacity) {
			if (isLimited) {
				return false;
			} else {
				maxCapacity *= 2;
			}
		}

		if (isEmpty()) {
			last = new Node(data, last);
			last.next = last;
		} else {
			Node prev = getNode(index - 1);
			Node newNext = new Node(data, prev.next);
			prev.next = newNext;
			if (index == size) {
				last = newNext;
			}
		}
		size++;
		return true;
	}

	public boolean remove(Object data) {
		if (isEmpty()) {
			return false;
		}

		Node prev = last;
		Node temp = last.next;
		if (data == null) {
			for (int index = 0; index < size; prev = temp, temp = temp.next) {
				if (temp.data == data) {
					if (size == 1) {
						last = null;
					} else {
						prev.next = temp.next;
						if (temp == last) {
							last = prev;
						}
					}
					size--;
					return true;
				}
			}
		} else {
			for (int index = 0; index < size; prev = temp, temp = temp.next) {
				if (data.equals(temp.data)) {
					if (size == 1) {
						last = null;
					} else {
						prev.next = temp.next;
						if (temp == last) {
							last = prev;
						}
					}
					size--;
					return true;
				}
			}
		}
		return false;
	}

	public E remove(int index) {
		if (isOutOfBounds(index)) {
			throw new IndexOutOfBoundsException();
		}

		E prev;
		if (size == 1) {
			prev = last.data;
			last = null;
		} else {
			Node before = getNode(index - 1);
			if (before.next == last) {
				last = before;
			}
			prev = before.next.data;
			before.next = before.next.next;

		}
		size--;
		return prev;
	}

	@Override
	public int indexOf(Object data) {
		if (isEmpty()) {
			return -1;
		}

		Node temp = last.next;
		if (data == null) {
			for (int i = 0; i < size; i++, temp = temp.next) {
				if (temp.data == data) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < size; i++, temp = temp.next) {
				if (data.equals(temp.data)) {
					return i;
				}
			}
		}
		return -1;
	}

	public E get(int index) {
		if (isOutOfBounds(index)) {
			throw new IndexOutOfBoundsException();
		}
		return getNode(index).data;
	}

	public E set(int index, E newData) {
		if (isOutOfBounds(index)) {
			throw new IndexOutOfBoundsException();
		}
		return getNode(index).setData(newData);
	}

	public void clear() {
		last = null;
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public int capacity() {
		return maxCapacity;
	}

	protected boolean isLimited() {
		return isLimited;
	}

	public E getFirst() {
		return last != null ? last.next.data : null;
	}

	public E getLast() {
		return last != null ? last.data : null;
	}

	public void rotate(int positions) {
		for (; positions > 0; positions--, last = last.next)
			;
	}

	private Node getNode(int index) {
		Node temp = last;
		if (last != null && index != (size - 1)) {
			for (; index > -1; index--, temp = temp.next)
				;
		}
		return temp;
	}

	private boolean isOutOfBounds(int index) {
		return index < 0 || index >= size;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		if (size > 1) {
			Node temp = last.next;
			while (temp != last) {
				sb.append(temp + ",");
				temp = temp.next;
			}
			sb.append(temp.data + ",");
		} else if (size == 1) {
			sb.append(last.data + ",");
		}

		if (sb.length() != 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(']');
		return sb.toString();
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	@Override
	public Iterator<E> descendingIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addFirst(E e) {
		if (!addHelper(0, e)) {
			throw new IllegalStateException("Queue is full");
		}
	}

	@Override
	public void addLast(E e) {
		add(size, e);
	}

	@Override
	public boolean offerFirst(E e) {
		return addHelper(0, e);
	}

	@Override
	public boolean offerLast(E e) {
		return addHelper(size, e);
	}

	@Override
	public E removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException("Deque is empty");
		}
		return remove(0);
	}

	@Override
	public E removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException("Deque is empty");
		}
		return remove(size - 1);
	}

	@Override
	public E pollFirst() {
		return isEmpty() ? null : remove(0);
	}

	@Override
	public E pollLast() {
		return isEmpty() ? null : remove(size - 1);
	}

	@Override
	public E peekFirst() {
		return isEmpty() ? null : get(0);
	}

	@Override
	public E peekLast() {
		return isEmpty() ? null : get(size - 1);
	}

	@Override
	public boolean removeFirstOccurrence(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeLastOccurrence(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean offer(E e) {
		return offerLast(e);
	}

	@Override
	public E remove() {
		return removeFirst();
	}

	@Override
	public E poll() {
		return pollFirst();
	}

	@Override
	public E element() {
		return getFirst();
	}

	@Override
	public E peek() {
		return peekFirst();
	}

	@Override
	public void push(E e) {
		addFirst(e);
	}

	@Override
	public E pop() {
		return removeFirst();
	}
}
