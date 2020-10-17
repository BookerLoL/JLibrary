package main.collections.Pair;

public class ImmutablePair<K, V> extends Pair<K, V> {
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

	@Override
	public K setKey(K newKey) {
		throw new UnsupportedOperationException("Key is not mutable");
	}

	public static <K, V> ImmutablePair<K, V> make(K key, V value) {
		return new ImmutablePair<>(key, value);
	}

}
