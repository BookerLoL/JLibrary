import java.util.AbstractQueue;
import java.util.Iterator;

/*
 * Wrapper class for a CircularArray to make it more clear that it's a queue.
 * 
 * Can add other methods too.
 * 
 * See notes on CircularArray implementation for increasing capacity.
 */
public class ArrayQueue<E> extends AbstractQueue<E> {
	private CircularArray<E> queue;

	public ArrayQueue() {
		this(Integer.MAX_VALUE, true);
	}

	public ArrayQueue(boolean maxCapacity) {
		this(maxCapacity ? Integer.MAX_VALUE : CircularArray.DEFAULE_CAPACITY, !maxCapacity);
	}

	public ArrayQueue(int initialCapacity) {
		this(initialCapacity, false);
	}

	public ArrayQueue(int initialCapacity, boolean limitCapacity) {
		super();
		if (initialCapacity <= 0) {
			initialCapacity = CircularArray.DEFAULE_CAPACITY;
		}

		queue = new CircularArray<>(initialCapacity, limitCapacity);
	}

	public boolean add(E data) {
		return queue.add(data);
	}

	public boolean offer(E data) {
		return queue.offer(data);
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

	public int size() {
		return queue.size();
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public boolean isFull() {
		return queue.isFull();
	}

	public int capcity() {
		return queue.capacity();
	}

	public E peekLast() {
		return queue.peekLast();
	}

	public E getLast() {
		return queue.getLast();
	}

	public E removeLast() {
		return queue.removeLast();
	}

	public E pollLast() {
		return queue.pollLast();
	}

	@Override
	public Iterator<E> iterator() {
		return queue.iterator();
	}
}
