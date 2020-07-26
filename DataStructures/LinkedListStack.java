import java.util.EmptyStackException;

public class LinkedListStack<E> implements Stack<E> {
	private LinkedList<E> stack;

	public LinkedListStack() {
		stack = new LinkedList<>();
	}

	public E push(E data) {
		stack.add(0, data);
		return data;
	}

	public E pop() {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		return stack.remove(0);
	}

	public E peek() {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		return stack.get(0);
	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public boolean empty() {
		return stack.isEmpty();
	}

	@Override
	public int search(Object o) {
		return stack.search(o);
	}
}
