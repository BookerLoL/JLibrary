package main.collections.Pair;

import java.util.Map;

public abstract class Pair<K, V> implements Map.Entry<K, V> {
	protected K key;
	protected V val;

	public Pair(K key, V value) {
		this.key = key;
		val = value;
	}

	public abstract K getKey();
	public abstract V getValue();
	public abstract K setKey(K newKey);

	public static <K, V> Pair<K, V> make(K key, V value) {
		return new ImmutablePair<>(key, value);
	}
}
