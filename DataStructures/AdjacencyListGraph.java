import java.util.Comparator;
import java.util.Stack;

public class AdjacencyListGraph {
	public static final double DEFAULT_WEIGHT = 1.0;

	protected static class Edge implements Comparable<Edge> {
		Vertex toVertex;
		double weight;

		public Edge(Vertex toVertex, double weight) {
			this.toVertex = toVertex;
			this.weight = weight;
		}

		public Edge(Vertex toVertex) {
			this(toVertex, DEFAULT_WEIGHT);
		}

		public static Comparator<Edge> getVertexComparator() {
			return new Comparator<Edge>() {
				@Override
				public int compare(Edge o1, Edge o2) {
					return o1.getToVertex().getID() - o2.getToVertex().getID();
				}
			};
		}

		public static Comparator<Edge> getWeightComparator() {
			return new Comparator<Edge>() {
				@Override
				public int compare(Edge o1, Edge o2) {
					double diff = o1.getWeight() - o2.getWeight();
					if (diff < 0.0) {
						return -1;
					} else if (diff > 0.0) {
						return 1;
					} else {
						return 0;
					}
				}
			};
		}

		public void setWeight(double weight) {
			this.weight = weight;
		}

		public Vertex getToVertex() {
			return toVertex;
		}

		public double getWeight() {
			return weight;
		}

		public String toString() {
			return "V: " + getToVertex() + ", weight: " + getWeight();
		}

		@Override
		public int compareTo(Edge o) {
			return getToVertex().getID() - o.getToVertex().getID();
		}

		public static Comparator<Edge> getWeightComparator = (e1, e2) -> {
			if (e1.getToVertex().compareTo(e2.getToVertex()) == 0) {
				return 0;
			}
			double diff = e1.getWeight() - e2.getWeight();
			if (diff < 0.0) {
				return -1;
			} else if (diff > 0.0) {
				return 1;
			} else {
				return 0;
			}
		};
	}

	class Vertex implements Comparable<Vertex> {
		int id;
		OrderedLinkedList<Edge> edges;

		public Vertex(int id) {
			this.id = id;
			edges = new OrderedLinkedList<>();
		}

		public int getID() {
			return id;
		}

		public OrderedLinkedList<Edge> getEdges() {
			return edges;
		}

		public void add(int vertex, double weight) {
			edges.add(new Edge(getVertex(vertex), weight));
		}

		public boolean contains(int vertex) {
			return edges.contains(new Edge(getVertex(vertex)));
		}

		public void remove(int vertex) {
			edges.remove(new Edge(getVertex(vertex)));
		}

		public void set(int vertex, double weight) {
			edges.set(new Edge(getVertex(vertex)), new Edge(getVertex(vertex), weight));
		}

		public Edge get(int i) {
			return edges.get(i);
		}

		public int getTotalEdges() {
			return edges.size();
		}

		public String toString() {
			return "" + id;
		}

		@Override
		public int compareTo(Vertex o) {
			return getID() - o.getID();
		}
	}

	private boolean isDirected;
	private ArrayList<Vertex> vertices;

	public AdjacencyListGraph(int numVertices) {
		this(numVertices, false);
	}

	public AdjacencyListGraph(int numVertices, boolean isDirected) {
		numVertices = numVertices < 0 ? 0 : numVertices;
		vertices = new ArrayList<>(numVertices);
		this.isDirected = isDirected;

		int i = 0;
		while (numVertices != 0) {
			vertices.add(new Vertex(i));
			numVertices--;
			i++;
		}
	}

	private Vertex getVertex(int vertex) {
		if (!isValid(vertex)) {
			throw new IllegalArgumentException("vertex is not within: 0 - " + (totalVertices() - 1));
		}
		return vertices.get(vertex);
	}

	public boolean containsEdge(int from, int to) {
		return getVertex(from).contains(to);
	}

	public void setEdgeWeight(int from, int to, double newWeight) {
		getVertex(from).set(to, newWeight);
		if (!isDirected() && from != to) {
			getVertex(to).set(from, newWeight);
		}
	}

	public void removeEdge(int from, int to) {
		getVertex(from).remove(to);
		if (!isDirected() && from != to) {
			getVertex(to).remove(from);
		}
	}

	public void addEdge(int from, int to, double weight) {
		getVertex(from).add(to, weight);
		if (!isDirected() && from != to) {
			getVertex(to).add(from, weight);
		}
	}

	public void addEdge(int from, int to) {
		addEdge(from, to, DEFAULT_WEIGHT);
	}

	private boolean isValid(int vertex) {
		return vertex >= 0 && vertex < totalVertices();
	}

	public boolean isDirected() {
		return isDirected;
	}

	public int totalVertices() {
		return vertices.size();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < totalVertices(); i++) {
			Vertex v = getVertex(i);
			sb.append(v + "\t Edges: " + v.getEdges() + "\n");
		}
		return sb.toString();
	}

	public String dfs(int startVertex) {
		StringBuilder sb = new StringBuilder();
		boolean[] visited = new boolean[totalVertices()];
		dfsHelper(sb, visited, startVertex);
		return sb.toString();
	}

	private void dfsHelper(StringBuilder sb, boolean[] visited, int vertex) {
		if (!visited[vertex]) {
			visited[vertex] = true;
			Vertex currVertex = getVertex(vertex);
			sb.append(vertex + " ");
			for (Edge edge : currVertex.getEdges()) {
				dfsHelper(sb, visited, edge.getToVertex().getID());
			}
		}
	}

	public String bfs(int startVertex) {
		StringBuilder sb = new StringBuilder();
		boolean[] visited = new boolean[totalVertices()];
		ArrayQueue<Vertex> queue = new ArrayQueue<>(totalVertices());
		Vertex curr = getVertex(startVertex);
		visited[curr.getID()] = true;
		sb.append(curr + " ");
		queue.add(curr);

		while (!queue.isEmpty()) {
			curr = queue.poll();
			for (Edge edge : curr.getEdges()) {
				Vertex toV = edge.getToVertex();
				if (!visited[toV.getID()]) {
					visited[toV.getID()] = true;
					sb.append(toV + " ");
					queue.add(toV);
				}
			}
		}

		return sb.toString();
	}

	public boolean hasCycle() {
		if (isDirected()) {
			return directedCycleDetection();
		} else {
			return nondirectedCycleDetection();
		}
	}

	private boolean directedCycleDetection() {
		byte[] visited = new byte[totalVertices()];
		for (int i = 0; i < totalVertices(); i++) {
			if (visited[i] == 0) {
				if (dfsCycleHelper(i, visited)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean dfsCycleHelper(int vertex, byte[] visited) {
		if (visited[vertex] == 1) { // 1 is explored
			return true;
		}

		visited[vertex] = 1;
		for (Edge edge : getVertex(vertex).getEdges()) {
			if (visited[edge.getToVertex().getID()] == 0) {
				dfsCycleHelper(edge.getToVertex().getID(), visited);
			} else if (visited[edge.getToVertex().getID()] == 1) {
				return true;
			}
		}
		visited[vertex] = 2; // 2 is fully explored
		return false;
	}

	private boolean nondirectedCycleDetection() {
		boolean[] visited = new boolean[totalVertices()];
		for (int i = 0; i < totalVertices(); i++) {
			if (!visited[i]) {
				if (bfsCycleHelper(i, -1, visited)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean bfsCycleHelper(int currV, int prevV, boolean[] visited) {
		visited[currV] = true;
		for (Edge edge : getVertex(currV).getEdges()) {
			if (!visited[edge.getToVertex().getID()]) {
				if (bfsCycleHelper(edge.getToVertex().getID(), currV, visited)) {
					return true;
				}
			} else if (edge.getToVertex().getID() != prevV) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Useful to determine if a group of
	 */
	public int[] getConnectedComponents() {
		int[] grouping = new int[totalVertices()];
		int group = 0;
		for (int i = 0; i < totalVertices(); i++) {
			if (grouping[i] == 0) {
				group++;
				dfsVisitGroup(i, grouping, group);
			}
		}

		return grouping;
	}

	private void dfsVisitGroup(int vertex, int[] grouping, int group) {
		grouping[vertex] = group;
		for (Edge edge : getVertex(vertex).getEdges()) {
			if (grouping[edge.getToVertex().getID()] == 0) {
				dfsVisitGroup(edge.getToVertex().getID(), grouping, group);
			} else if (isDirected() && group != grouping[edge.getToVertex().getID()]) {
				// useful if didn't get starting node in a directed graph
				grouping[vertex] = grouping[edge.getToVertex().getID()];
			}
		}
	}

	public int outdegree(int vertex) {
		return getVertex(vertex).getTotalEdges();
	}

	public int indegree(int vertex) {
		int indegree = 0;
		for (int i = 0; i < vertices.size(); i++) {
			Vertex v = getVertex(i);
			for (Edge edge : v.getEdges()) {
				if (edge.getToVertex().getID() == vertex) {
					indegree++;
					break;
				}
			}
		}
		return indegree;
	}

	public String allTopologicalSort() {
		if (!isDirected()) {
			return "Graph is not directed";
		}
		boolean[] visited = new boolean[totalVertices()];
		int[] indegrees = new int[totalVertices()];
		for (int i = 0; i < totalVertices(); i++) {
			indegrees[i] = indegree(i);
		}
		ArrayList<String> stack = new ArrayList<>(totalVertices());
		StringBuilder sb = new StringBuilder();
		allTopologicalSortHelper(visited, indegrees, stack, sb);
		return sb.toString();
	}

	private void allTopologicalSortHelper(boolean[] visited, int[] indegrees, ArrayList<String> stack,
			StringBuilder sb) {
		boolean flag = false;

		for (int i = 0; i < totalVertices(); i++) {
			if (!visited[i] && indegrees[i] == 0) {
				visited[i] = true;
				stack.add(i + " ");
				for (Edge edge : getVertex(i).getEdges()) {
					indegrees[edge.getToVertex().getID()]--;
				}
				allTopologicalSortHelper(visited, indegrees, stack, sb);

				visited[i] = false;
				stack.remove(stack.size() - 1);
				for (Edge edge : getVertex(i).getEdges()) {
					indegrees[edge.getToVertex().getID()]++;
				}
				flag = true;
			}
		}

		if (!flag) {
			for (int i = 0; i < stack.size(); i++) {
				sb.append(stack.get(i));
			}
			sb.append("\n");
		}
	}

	public String dfsTopologicalSort() {
		boolean[] visited = new boolean[totalVertices()];
		Stack<String> stack = new Stack<>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < totalVertices(); i++) {
			if (!visited[i]) {
				dfsTopologicalSortHelper(i, visited, stack);
			}
		}

		while (!stack.isEmpty()) {
			sb.append(stack.pop());
		}
		return sb.toString();
	}

	private void dfsTopologicalSortHelper(int vertex, boolean[] visited, Stack<String> stack) {
		visited[vertex] = true;
		for (Edge edge : getVertex(vertex).getEdges()) {
			if (!visited[edge.getToVertex().getID()]) {
				dfsTopologicalSortHelper(edge.getToVertex().getID(), visited, stack);
			}
		}
		stack.add(vertex + " ");
	}

	public AdjacencyListGraph kruskalMST() {
		throw new UnsupportedOperationException();
	}

	public static void main(String[] args) {
		AdjacencyListGraph graph = dag1();
		System.out.println(graph);
		System.out.println();
		System.out.println(graph.dfs(0));
		System.out.println(graph.bfs(0));
		System.out.println(graph.hasCycle());
		System.out.println(graph.dfsTopologicalSort());
		System.out.println();
		System.out.println(graph.allTopologicalSort());
	}

	private static AdjacencyListGraph undirected1() {
		AdjacencyListGraph graph = new AdjacencyListGraph(6, false);
		graph.addEdge(0, 1, 1.0);
		graph.addEdge(0, 2, 3.0);
		graph.addEdge(0, 3, 5.0);
		graph.addEdge(1, 2, 2.0);
		graph.addEdge(2, 3, 4.0);
		graph.addEdge(2, 4, 7.0);
		graph.addEdge(3, 4, 6.0);
		graph.addEdge(0, 5, 8.0);
		return graph;
	}

	private static AdjacencyListGraph undirected2() {
		AdjacencyListGraph graph = new AdjacencyListGraph(4, false);
		graph.addEdge(0, 1, DEFAULT_WEIGHT);
		graph.addEdge(1, 2, DEFAULT_WEIGHT);
		graph.addEdge(2, 3, DEFAULT_WEIGHT);
		graph.addEdge(3, 3, DEFAULT_WEIGHT);

		return graph;
	}

	private static AdjacencyListGraph dag1() {
		AdjacencyListGraph graph = new AdjacencyListGraph(6, true);
		graph.addEdge(5, 2);
		graph.addEdge(5, 0);
		graph.addEdge(4, 0);
		graph.addEdge(4, 1);
		graph.addEdge(2, 3);
		graph.addEdge(3, 1);
		return graph;
	}
}
