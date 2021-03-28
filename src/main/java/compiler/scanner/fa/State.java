package main.compiler.scanner.fa;

import java.util.LinkedList;
import java.util.List;

public abstract class State {
	public abstract int getID();

	protected String name;
	protected boolean isFinal;
	protected List<Edge> edges;

	public State(String name, boolean isFinal) {
		this.name = name;
		this.isFinal = isFinal;
		edges = new LinkedList<>();
	}

	public String getName() {
		return name;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public boolean addEdge(Edge newEdge) {
		if (!newEdge.getFrom().equals(this) || getEdges().contains(newEdge)) {
			return false;
		}
		return getEdges().add(newEdge);
	}

	public State transition(char transitionCh) {
		Edge edge = getEdge(transitionCh);
		return edge != null ? edge.getNext() : null;
	}

	private Edge getEdge(char transitionCh) {
		for (Edge edge : getEdges()) {
			if (edge.hasTransition(transitionCh)) {
				return edge;
			}
		}
		return null;
	}

	public State setIsFinal(boolean isFinal) {
		this.isFinal = isFinal;
		return this;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o == null) {
			return false;
		} else if (getClass() != o.getClass()) {
			return false;
		}

		State other = (State) o;
		return getName().equals(other.getName());
	}

	public String toString() {
		String isFinalStr = isFinal ? "is final" : "is not final";
		return "Node: " + getName() + " " + isFinalStr;
	}

	public int hashCode() {
		return name.hashCode();
	}
}
