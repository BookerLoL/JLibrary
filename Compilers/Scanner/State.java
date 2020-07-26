package Scanner;

import java.util.LinkedList;
import java.util.List;

public abstract class State<T extends Edge<? extends State<T>>> {
	protected String name;
	protected boolean isFinal;
	protected List<T> edges;

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

	public List<T> getEdges() {
		return edges;
	}

	public String toString() {
		String isFinalStr = isFinal ? "is final" : "is not final";
		return "Node: " + name + " " + isFinalStr;
	}

	public int hashCode() {
		return this.name.hashCode();
	}

	/*
	 * We are going to assume the same name is equal node but in reality that isn't
	 * always true assuming you have two nodes with the same name. Would need to
	 * check if it's a final state and it's collection of edges for a deep equals
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		State<T> other = (State<T>) o;
		return this.getName().equals(other.getName());
	}

	private T get(char transitionCh) {
		for (T edge : getEdges()) {
			if (edge.transitionChars.contains(transitionCh)) {
				return edge;
			}
		}
		return null;
	}

	public State<T> transition(char transitionCh) {
		T edge = get(transitionCh);
		return edge != null ? edge.getNext() : null;
	}
	
	public State<T> changeToFinal(boolean isFinal) {
		this.isFinal = isFinal;
		return this;
	}

	public void add(T newEdge) {
		if (edges.contains(newEdge) || !newEdge.getFrom().equals(this)) {
			return;
		} 
		getEdges().add(newEdge);
	}
	
	public abstract int getID();
}
