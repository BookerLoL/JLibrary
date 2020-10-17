package main.collections.Pair;

public class MutablePair<K, V> extends Pair<K, V> {
	public MutablePair() {
		super(null, null);
	}

	public MutablePair(K key, V value) {
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
	public K setKey(K newKey) {
		K prev = key;
		key = newKey;
		return prev;
	}

	@Override
	public V setValue(V value) {
		V prev = val;
		val = value;
		return prev;
	}

	public static <K, V> MutablePair<K, V> make(K key, V value) {
		return new MutablePair<>(key, value);
	}
}
