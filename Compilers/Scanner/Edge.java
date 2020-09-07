package scanner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class Edge {
	public static final char EMPTY_TRANSITION = '\0';
	protected State from;
	protected State next;
	protected Set<Character> transitionChars;

	public Edge(State from, State next, char transitionChar) {
		this(from, next, new HashSet<>(Arrays.asList(transitionChar)));
	}

	public Edge(State from, State next, Set<Character> transitionChars) {
		this.from = from;
		this.next = next;
		this.transitionChars = transitionChars;
	}

	public State getFrom() {
		return from;
	}

	public State getNext() {
		return next;
	}

	public Set<Character> getTransitions() {
		return transitionChars;
	}

	public boolean hasTransition(char transitionChar) {
		return getTransitions().contains(transitionChar);
	}

	public void addTransition(Character... transitionChars) {
		getTransitions().addAll(Arrays.asList(transitionChars));
	}

	public static Edge getEdgeContaining(State source, State target) {
		for (Edge edge : source.getEdges()) {
			if (edge.getNext().equals(target)) {
				return edge;
			}
		}
		return null;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o == null) {
			return false;
		} else if (getClass() != o.getClass()) {
			return false;
		}

		Edge other = (Edge) o;
		return getFrom().equals(other.getFrom()) && getNext().equals(other.getNext());
	}

	public String toString() {
		return "From: " + getFrom().getName() + " to " + getNext().getName() + " using: " + getTransitions().toString();
	}
}
