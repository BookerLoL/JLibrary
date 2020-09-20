package main.collections.Pair;
import java.util.Map;

public abstract class Pair<K extends Comparable<? super K>, V extends Comparable<? super V>>
		implements Map.Entry<K, V>, Comparable<Pair<K, V>> {
	protected K key;
	protected V val;

	public Pair(K key, V value) {
		this.key = key;
		val = value;
	}

	public abstract K getKey();
	public abstract V getValue();

	@Override
	public int compareTo(Pair<K, V> o) {
		// Doesn't check for handle null cases
		int keyCompare = key.compareTo(o.key);
		return keyCompare != 0 ? keyCompare : val.compareTo(o.val);
	}
	
	public  static <K extends Comparable<? super K>, V extends Comparable<? super V>> Pair<K, V> make(K key, V value) {
		return new ImmutablePair<>(key, value);
	}
}
