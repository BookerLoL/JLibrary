package main.collections.Pair;

public class ImmutablePair<K extends Comparable<? super K>, V extends Comparable<? super V>> extends Pair<K, V> {
	public ImmutablePair() {
		super(null, null);
	}

	public ImmutablePair(K key, V value) {
		super(key, value);
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return val;
	}
	
	@Override
	public V setValue(V value) {
		throw new UnsupportedOperationException("Value is not mutable");
	}
	
	public  static <K extends Comparable<? super K>, V extends Comparable<? super V>> ImmutablePair<K, V> make(K key, V value) {
		return new ImmutablePair<>(key, value);
	}
}
