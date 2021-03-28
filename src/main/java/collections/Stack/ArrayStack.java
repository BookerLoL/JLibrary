package main.collections.Stack;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/*
 * Stack implementation 
 * 
 * Do remember that stack is LIFO
 * 
 * Index returned will be based on how the stack would pop out the contents.
 * 
 */
public class ArrayStack<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable {
	private ArrayList<E> stack;

	public ArrayStack(int initialCapacity) {
		stack = new ArrayList<>(initialCapacity);
	}

	public E push(E data) {
		stack.add(data);
		return data;
	}

	public E pop() {
		if (stack.isEmpty()) {
			throw new EmptyStackException();
		}

		return stack.remove(stack.size() - 1);
	}

	public E peek() {
		if (stack.isEmpty()) {
			throw new EmptyStackException();
		}

		return stack.get(stack.size() - 1);
	}

	@Override
	public int size() {
		return stack.size();
	}

	@Override
	public boolean isEmpty() {
		return stack.size() == 0;
	}

	@Override
	public boolean contains(Object o) {
		return stack.indexOf(o) >= 0;
	}

	protected class StackIterator implements Iterator<E> {
		private int index;

		public StackIterator() {
			index = stack.size() - 1;
		}

		@Override
		public boolean hasNext() {
			return index >= 0;
		}

		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return stack.get(index--);
		}
	}

	@Override
	public Object[] toArray() {
		int size = stack.size();
		Object[] stackArray = new Object[size];
		for (int i = size - 1; i >= 0; i--) {
			stackArray[size - (i + 1)] = stackArray[i];
		}
		return stackArray;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		int size = stack.size();
		T[] stackArray = a.length < size ? (T[]) new Object[size] : a;
		for (int i = size - 1; i >= 0; i--) {
			stackArray[size - (i + 1)] = stackArray[i];
		}
		return stackArray;
	}

	@Override
	public boolean add(E e) {
		return stack.add(e);
	}

	

	@Override
	public boolean containsAll(Collection<?> c) {
		return stack.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return stack.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return stack.remove(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return stack.retainAll(c);
	}

	@Override
	public void clear() {
		stack.clear();
	}

	@Override
	public E get(int index) {
		return index >= 0 ? stack.get(stack.size() - (index - 1)) : stack.get(-1);
	}
}
