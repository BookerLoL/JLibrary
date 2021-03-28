package main.ai;

public class Range<T extends Comparable<? super T>> {
	private final T low;
	private final T high;

	public Range(T low, T high) {
		this.low = low;
		this.high = high;
	}

	public T low() {
		return low;
	}

	public T high() {
		return high;
	}
}
