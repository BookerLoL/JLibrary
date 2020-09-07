package scanner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Utility {
	public static class Partitioner {
		public static <T extends State> boolean IS_EQUAL(Collection<T> left, Collection<T> right) {
			return left == right || (left.size() == right.size() && left.containsAll(right));
		}

		public static <T extends State> Set<State> setDifference(Collection<T> left, Collection<T> right) {
			return left.stream().filter(item -> !right.contains(item)).collect(Collectors.toSet());
		}
	}

	public static Set<Character> getChars(char from, char toInclusive) {
		Set<Character> transitions = new HashSet<>(toInclusive - from + 1);
		while (from <= toInclusive) {
			transitions.add(from);
			from++;
		}
		return transitions;
	}

	// Only chars that are printable
	public static boolean isPrintable(char ch) {
		return ch > 31 && ch < 127;
	}

	/*
	 * Used to get the index of where it belongs, useful if there is parallel
	 * mapping of the collection
	 * 
	 * This is useful to check if a collection contains the expected value
	 */
	public static <T extends Collection<? extends State>> int get(Collection<T> collection, T value) {
		int index = 0;
		for (T item : collection) {
			if (isEqual(item, value)) {
				return index;
			}
			index++;
		}

		return -1;
	}

	public static <T> boolean isEqual(Collection<T> collection1, Collection<T> collection2) {
		if (collection1 == collection2) {
			return true;
		}

		if (collection1 == null || collection2 == null) {
			return false;
		}

		if (collection1.size() != collection2.size()) {
			return false;
		}

		return collection1.containsAll(collection2);
	}

	public static <T extends State> boolean containsFinalState(Collection<T> nodes) {
		for (T node : nodes) {
			if (node.isFinal()) {
				return true;
			}
		}
		return false;
	}
}
