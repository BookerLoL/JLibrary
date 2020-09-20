package main.collections.Queue;
import java.util.AbstractQueue;
import java.util.Iterator;

import main.collections.List.CircularLinkedList;

public class LinkedListQueue<E> extends AbstractQueue<E> {
	private CircularLinkedList<E> queue;

	public LinkedListQueue() {
		this(Integer.MAX_VALUE);
	}

	public LinkedListQueue(boolean maxCapacity) {
		this(maxCapacity ? Integer.MAX_VALUE : CircularArray.DEFAULT_CAPACITY);
	}

	public LinkedListQueue(int capacity) {
		queue = new CircularLinkedList<>();
	}

	public boolean add(E data) {
		return queue.add(data);
	}

	public boolean offer(E data) {
		return queue.add(data);
	}

	public E element() {
		return queue.element();
	}

	public E peek() {
		return queue.peek();
	}

	public E remove() {
		return queue.remove();
	}

	public E poll() {
		return queue.poll();
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@SuppressWarnings("unused")
	private boolean isFull() {
		return queue.size() == queue.capacity() && queue.isLimited();
	}

	public int size() {
		return queue.size();
	}

	public int getCapcity() {
		return queue.capacity();
	}

	@Override
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException();
	}
}
