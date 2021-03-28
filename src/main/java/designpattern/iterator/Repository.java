package main.designpattern.iterator;

public class Repository<T> implements Container<T> {
	@Override
	public Iterator<T> getIterator() {
		return new RepoIterator<>();
	}

	private class RepoIterator<T> implements Iterator<T> {
		@Override
		public T next() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean hasNext() {
			throw new UnsupportedOperationException();
		}
	}
}
