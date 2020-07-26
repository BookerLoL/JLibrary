package introduction;

import java.util.List;

//Just an example of what a Graph class may look like
public abstract class Graph {
	protected boolean isDirected;
	protected boolean isWeighted;
	protected int numVertices;

	public Graph(int numberOfVertices) {
		this(numberOfVertices, false, false);
	}

	public Graph(int numberOfVertices, boolean isDirected) {
		this(numberOfVertices, isDirected, false);
	}

	public Graph(int numberOfVertices, boolean isDirected, boolean isWeighted) {
		this.isWeighted = isWeighted;
		this.isDirected = isDirected;
		numVertices = numberOfVertices;
	}

	public boolean isWeighted() {
		return isWeighted;
	}

	public int totalVertices() {
		return numVertices;
	}

	public boolean isDirected() {
		return isDirected;
	}
	
	public abstract Vertex get(int i);
	public abstract List<Edge> get(Vertex v);
}
