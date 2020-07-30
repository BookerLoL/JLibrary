import java.util.AbstractSequentialList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/*
 * Does not use sentinel node as head
 * 
 * Singly linked list
 */

public class LinkedList<E> extends AbstractSequentialList<E> implements List<E>, Stack<E>, Cloneable {
	private static class Node<E> {
		private E data;
		private Node<E> next;

		public Node(E data, Node<E> next) {
			this.data = data;
			this.next = next;
		}

		public E setdata(E newdata) {
			E prev = newdata;
			data = newdata;
			return prev;
		}
	}

	private Node<E> head;
	private int size;

	public boolean add(E data) {
		add(size, data);
		return true;
	}

	@Override
	public void add(int index, E element) {
		checkExclusiveBounds(index);

		if (index == 0) {
			head = new Node<>(element, head);
		} else {
			Node<E> prev = getNode(index - 1);
			prev.next = new Node<E>(element, prev.next);
		}
		size++;
	}

	private void checkExclusiveBounds(int index) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public boolean remove(Object o) {
		if (isEmpty()) {
			return false;
		}

		Node<E> prev = null;
		Node<E> temp = head;

		if (o == null) {
			for (; temp != null && temp.data != null; prev = temp, temp = temp.next)
				;
		} else {
			for (; temp != null && !o.equals(temp.data); prev = temp, temp = temp.next)
				;
		}

		if (temp != null) {
			if (temp == head) {
				head = temp.next;
			} else {
				prev.next = temp.next;
			}
			size--;
			return true;
		}
		return false;
	}

	public E remove(int index) {
		E prev;
		Node<E> temp;
		if (index == 0 && head != null) {
			temp = head;
			prev = temp.data;
			head = temp.next;
		} else {
			temp = getNode(index - 1);
			prev = temp.next.data;
			temp.next = temp.next.next;
		}
		size--;
		return prev;
	}

	@Override
	public int indexOf(Object o) {
		Node<E> temp = head;
		int i = 0;

		if (o == null) {
			for (; temp != null && temp.data != null; temp = temp.next, i++)
				;
		} else {
			for (; temp != null && !o.equals(temp.data); temp = temp.next, i++)
				;
		}

		return temp != null ? i : -1;
	}

	public E get(int index) {
		return getNode(index).data;
	}

	public E set(int index, E newdata) {
		return getNode(index).setdata(newdata);
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	public void clear() {
		head = null;
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

	private Node<E> getNode(int index) {
		checkInclusiveBounds(index);
		Node<E> temp = head;
		for (; index != 0; temp = temp.next, index--)
			;
		return temp;
	}

	private void checkInclusiveBounds(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder('[');
		for (Node<E> temp = head; temp != null; temp = temp.next) {
			sb.append(temp.data + ",");
		}
		if (sb.length() != 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(']');
		return sb.toString();
	}

	@Override
	public Iterator<E> iterator() {
		return new LLIter();
	}

	private class LLIter implements Iterator<E> {
		Node<E> curr = head;

		@Override
		public boolean hasNext() {
			return curr != null;
		}

		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			E data = curr.data;
			curr = curr.next;
			return data;
		}

	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean empty() {
		return size == 0;
	}

	@Override
	public E peek() {
		return head != null ? head.data : null;
	}

	@Override
	public E pop() {
		if (head == null) {
			throw new EmptyStackException();
		}

		Node<E> temp = head;
		head = head.next;
		size--;
		return temp.data;
	}

	@Override
	public E push(E item) {
		head = new Node<>(item, head);
		size++;
		return item;
	}

	@Override
	public int search(Object o) {
		return indexOf(o);
	}
	
	@SuppressWarnings("unchecked")
	public Object clone() {
		LinkedList<E> clone = null;
		try {
			clone = (LinkedList<E>) super.clone();		
			clone.head = null;
			clone.size = 0;
			for (Node<E> temp = head; temp != null; temp = temp.next) {
				clone.add(temp.data);
			}
		
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
}
