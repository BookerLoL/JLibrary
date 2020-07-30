import java.util.Arrays;

public class AdjacencyMatrixGraph {
	public static final double NO_WEIGHT = 0.0;
	public static final double DEFAULT_WEIGHT = 1.0;
	private boolean isDirected;
	private double[][] graph;

	public AdjacencyMatrixGraph(int numberOfNodes) {
		this(numberOfNodes, false);
	}

	public AdjacencyMatrixGraph(int numberOfNodes, boolean isDirected) {
		graph = new double[numberOfNodes][numberOfNodes];
		this.isDirected = isDirected;
	}

	public boolean hasEdge(int from, int to) {
		if (isValid(from) && isValid(to)) {
			return graph[from][to] != NO_WEIGHT;
		}
		return false;
	}

	public double getWeight(int from, int to) {
		if (isValid(from) && isValid(to)) {
			return graph[from][to];
		}
		return NO_WEIGHT;
	}

	public void addEdge(int from, int to) {
		addEdge(from, to, DEFAULT_WEIGHT);
	}

	public void addEdge(int from, int to, double weight) {
		if (isValid(from) && isValid(to)) {
			graph[from][to] = weight;
			if (!isDirected()) {
				graph[to][from] = weight;
			}
		}
	}

	public void removeEdge(int from, int to) {
		if (isValid(from) && isValid(to)) {
			graph[from][to] = NO_WEIGHT;
			if (!isDirected()) {
				graph[to][from] = NO_WEIGHT;
			}
		}
	}

	private boolean isValid(int vertex) {
		return vertex > -1 && vertex < graph.length;
	}

	public boolean isDirected() {
		return isDirected;
	}

	public int totalVertices() {
		return graph.length;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (double[] vertexRow : graph) {
			sb.append(Arrays.toString(vertexRow) + "\n");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(5);
		graph.addEdge(0, 4, 3.5);
		graph.addEdge(1, 3, 2.5);
		graph.addEdge(2, 2, 1.5);
		System.out.println(graph);
		
	}
}
