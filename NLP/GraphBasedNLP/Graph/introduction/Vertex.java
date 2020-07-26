package introduction;

import java.util.LinkedList;
import java.util.List;

public class Vertex {
	protected int id;
	protected List<Edge> edges;

	public Vertex(int id) {
		this.id = id;
		edges = new LinkedList<>();
	}

	public int getID() {
		return id;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public String toString() {
		return "Vertex: " + id;
	}

	public void add(Edge edge) {
		if (edge.getFrom().equals(this) && !contain(edge)) {
			getEdges().add(edge);
		}
	}

	public boolean contain(Edge edge) {
		return getEdges().contains(edge);
	}
}
