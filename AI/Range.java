public class Range<T extends Comparable<? super T>> {
	private static final long serialVersionUID = 7576286243581509263L;
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
