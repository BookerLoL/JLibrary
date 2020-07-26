package introduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class AdjacencyGraph {
	List<Vertex> adjList;
	boolean isDirected;

	public AdjacencyGraph(int numNodes, boolean isDirected) {
		adjList = new LinkedList<>();
		for (int i = 0; i < numNodes; i++) {
			adjList.add(new Vertex(i));
		}
		this.isDirected = isDirected;
	}

	public Vertex getVertex(int name) {
		for (Vertex vertex : adjList) {
			if (vertex.getID() == name) {
				return vertex;
			}
		}
		return null;
	}

	public int numberOfNodes() {
		return totalVertices();
	}

	public void dfs(int startingVertex) {
		dfsHelper(getVertex(startingVertex), new HashSet<>());
	}

	private void dfsHelper(Vertex vertex, Set<Vertex> seenVertices) {
		if (!seenVertices.contains(vertex)) {
			seenVertices.add(vertex);
			System.out.println(vertex);
			for (Edge edge : vertex.getEdges()) {
				dfsHelper(edge.getTo(), seenVertices);
			}
		}
	}

	public void bfs(int startingVertex) {
		bfsHelper(getVertex(startingVertex), new HashSet<>());
	}

	private void bfsHelper(Vertex vertex, Set<Vertex> seenVertices) {
		Queue<Vertex> queue = new LinkedList<>();
		queue.add(vertex);
		while (!queue.isEmpty()) {
			Vertex curr = queue.remove();
			if (!seenVertices.contains(curr)) {
				seenVertices.add(curr);
				System.out.println(curr.getEdges());
				for (Edge edge : curr.getEdges()) {
					queue.add(edge.getTo());
				}
			}

		}
	}

	public void addEdge(int tail, int head, double weight) {
		Vertex tailVertex = getVertex(tail);
		Vertex headVertex = getVertex(head);
		Edge newEdge = new Edge(weight, tailVertex, headVertex);
		if (!tailVertex.contain(newEdge)) {
			tailVertex.add(newEdge);
			if (!isDirected) {
				Edge reversedEdge = new Edge(weight, headVertex, tailVertex);
				headVertex.add(reversedEdge);
			}
		}
	}

	/*
	 * Prims algorithm, must be undirected and connected
	 */
	public static AdjacencyGraph primsMST(AdjacencyGraph graph, int startVertex) {
		Set<Vertex> unvisited = new HashSet<>(graph.adjList);
		Set<Vertex> visited = new HashSet<>();
		AdjacencyGraph mst = new AdjacencyGraph(unvisited.size(), graph.isDirected());

		Vertex start = graph.getVertex(startVertex);
		Edge currBest = null;
		visited.add(start);
		unvisited.remove(start);
		while (!unvisited.isEmpty()) {
			double minValue = Double.MAX_VALUE;
			for (Vertex node : visited) {
				for (Edge edge : node.getEdges()) {
					if (edge.getWeight() < minValue && !visited.contains(edge.getTo())) {
						currBest = edge;
						minValue = edge.getWeight();
					}
				}
			}
			mst.addEdge(currBest.getFrom().getID(), currBest.getTo().getID(), currBest.getWeight());
			visited.add(currBest.getTo());
			unvisited.remove(currBest.getTo());
		}
		return mst;
	}

	/*
	 * Kruskals algorithm, must be undirected and connected
	 */
	public static AdjacencyGraph kruskalsMST(AdjacencyGraph graph) {
		List<Edge> edges = new ArrayList<>();

		for (Vertex v : graph.adjList) {
			for (Edge edge : v.getEdges()) {
				edges.add(edge);
			}
		}

		Collections.sort(edges, (edge1, edge2) -> {
			if (edge1.getWeight() <= edge2.getWeight()) {
				return -1;
			} else {
				return 1;
			}
		});

		AdjacencyGraph mst = new AdjacencyGraph(graph.totalVertices(), graph.isDirected());
		final int MAX_EDGES = graph.totalVertices() - 1;
		int edgesAdded = 0;
		for (Edge edge : edges) {
			if (edgesAdded == MAX_EDGES) {
				break;
			}

			mst.addEdge(edge.getFrom().getID(), edge.getTo().getID(), edge.getWeight());
			if (mst.hasCycle()) {
				mst.removeEdge(edge.getFrom().getID(), edge.getTo().getID());
			} else {
				edgesAdded++;
			}
		}

		return mst;
	}

	protected void removeEdge(int tail, int head) {
		Vertex v = getVertex(tail);
		Vertex headV = getVertex(head);
		for (int i = 0; i < v.getEdges().size(); i++) {
			Edge edge = v.getEdges().get(i);
			if (edge.getFrom().equals(v) && edge.getTo().equals(headV)) {
				v.getEdges().remove(i);
				break;
			}
		}

		if (!isDirected) {
			for (int i = 0; i < headV.getEdges().size(); i++) {
				Edge edge = headV.getEdges().get(i);
				if (edge.getFrom().equals(headV) && edge.getTo().equals(v)) {
					headV.getEdges().remove(i);
					break;
				}
			}
		}
	}

	public boolean hasCycle() {
		Set<Vertex> visited = new HashSet<>(this.totalVertices());
		for (Vertex v : adjList) {
			if (!visited.contains(v)) {
				if (isCycleHelper(v, null, visited)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isCycleHelper(Vertex curr, Vertex parent, Set<Vertex> visited) {
		visited.add(curr);
		for (Edge edge : curr.getEdges()) {
			if (!visited.contains(edge.getTo())) {
				if (isCycleHelper(edge.getTo(), curr, visited)) {
					return true;
				}
			} else if (!edge.getTo().equals(parent)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Must be Directed and acyclic
	 * 
	 * Can have different results depending on which Vertex was accessed first
	 */
	public List<Vertex> KahnsTopologicalSort() {
		List<Vertex> ts = new LinkedList<>();
		Map<Vertex, Integer> vertexToIndegree = new HashMap<>();
		Queue<Vertex> queue = new LinkedList<>();
		for (int i = 0; i < totalVertices(); i++) {
			int inDegrees = inDegree(i);
			if (inDegrees == 0) {
				queue.add(getVertex(i));
			} else {
				vertexToIndegree.put(getVertex(i), inDegrees);
			}
		}

		while (!queue.isEmpty()) {
			Vertex curr = queue.remove();
			ts.add(curr);
			for (Edge e : curr.getEdges()) {
				Vertex dest = e.getTo();
				if (vertexToIndegree.containsKey(dest)) {
					int inDegrees = vertexToIndegree.get(dest) - 1;
					if (inDegrees == 0) {
						queue.add(dest);
						vertexToIndegree.remove(dest);
					} else {
						vertexToIndegree.put(dest, inDegrees);
					}
				}
			}
		}

		if (ts.size() != totalVertices()) {
			// topological sort not possible
			return null;
		}
		System.out.println(ts);
		return ts;
	}

	public List<Vertex> dijkstrasShortestPath(int from, int to) {
		class PathVertex {
			Vertex source;
			Vertex previous;
			double distanceFromSource;

			public PathVertex(Vertex source, Vertex previous, double distanceFromSource) {
				this.source = source;
				this.previous = previous;
				this.distanceFromSource = distanceFromSource;
			}

			public Vertex getSource() {
				return source;
			}

			public Vertex getPrevious() {
				return previous;
			}

			public double getDistance() {
				return distanceFromSource;
			}

			public void setDistance(double newDistance) {
				distanceFromSource = newDistance;
			}

			public void set(Vertex newPrevious) {
				this.previous = newPrevious;
			}
		}
		
		Set<Vertex> unvisited = new HashSet<>(totalVertices());
		Vertex srcV = getVertex(from);
		List<PathVertex> pathVertices = new ArrayList<>(totalVertices());
		for (int i = 0; i < totalVertices(); i++) {
			Vertex src = getVertex(i);
			if (src == srcV) {
				pathVertices.add(new PathVertex(src, null, 0)); //at start
			} else {
				pathVertices.add(new PathVertex(src, null, Double.MAX_VALUE));
			}
			unvisited.add(src);
		}
		
		PathVertex currMin = pathVertices.get(from);
		while (!unvisited.isEmpty()) {
			for (PathVertex pv : pathVertices) {
				if (currMin == null && unvisited.contains(pv.getSource())) {
					currMin = pv;
				} else if (currMin != null && currMin.getDistance() > pv.getDistance() && unvisited.contains(pv.getSource())) {
					currMin = pv;
				}
			}
			
			
			for (Edge edge : currMin.getSource().getEdges()) {
				double newDist = edge.getWeight() + currMin.getDistance();
				PathVertex neighbor = pathVertices.get(edge.getTo().getID());
				if (neighbor.getDistance() > newDist) {
					System.out.println(currMin.getSource() + " to " + edge.getTo());
					neighbor.set(currMin.getSource());
					neighbor.setDistance(newDist);
				}
			}
			
			unvisited.remove(currMin.getSource());
			currMin = null;
		}
		
		PathVertex destPV = pathVertices.get(to);
		PathVertex srcPV = pathVertices.get(from);
		
		List<Vertex> path = new ArrayList<>();
		while (destPV != srcPV) {
			path.add(destPV.getSource());
			if (destPV.getPrevious() == null) {
				return new LinkedList<>();
			}
			destPV = pathVertices.get(destPV.getPrevious().getID());
		}
		path.add(srcV);
		Collections.reverse(path);
		return path;
	}

	public int totalVertices() {
		return adjList.size();
	}

	public boolean isDirected() {
		return isDirected;
	}

	public int degree(int vertex) {
		return outDegree(vertex) + inDegree(vertex);
	}

	public int outDegree(int vertex) {
		return getVertex(vertex).getEdges().size();
	}

	public int inDegree(int vertex) {
		int inDegrees = 0;
		Vertex destination = getVertex(vertex);
		for (int i = 0; i < totalVertices(); i++) {
			if (i != vertex) {
				Vertex v = getVertex(i);
				for (Edge edge : v.getEdges()) {
					if (edge.getTo().equals(destination)) {
						inDegrees++;
						break;
					}
				}
			}
		}
		return inDegrees;
	}

	private static AdjacencyGraph getGraph1Undirected() {
		int numNodes = 5;
		AdjacencyGraph graph = new AdjacencyGraph(numNodes, false);
		graph.addEdge(0, 1, 0);
		graph.addEdge(0, 3, 0);
		graph.addEdge(1, 2, 0);
		graph.addEdge(3, 2, 0);
		graph.addEdge(3, 4, 0);
		graph.addEdge(4, 0, 0);
		graph.addEdge(4, 2, 0);
		return graph;
	}

	private static AdjacencyGraph getGraph2Undirected() {
		int numNodes = 9;
		AdjacencyGraph graph = new AdjacencyGraph(numNodes, false);
		graph.addEdge(0, 1, 4);
		graph.addEdge(0, 7, 8);

		graph.addEdge(1, 7, 11);
		graph.addEdge(1, 2, 8);

		graph.addEdge(7, 8, 7);
		graph.addEdge(7, 6, 1);

		graph.addEdge(2, 3, 7);
		graph.addEdge(2, 8, 2);
		graph.addEdge(2, 5, 4);

		graph.addEdge(8, 6, 6);

		graph.addEdge(6, 5, 2);

		graph.addEdge(3, 4, 9);
		graph.addEdge(3, 5, 14);

		graph.addEdge(5, 4, 10);
		return graph;
	}

	private static AdjacencyGraph getGraph3Undirected() {
		int numNodes = 4;
		AdjacencyGraph graph = new AdjacencyGraph(numNodes, false);
		graph.addEdge(0, 1, 2);
		graph.addEdge(0, 2, 3);
		graph.addEdge(0, 3, 2);
		graph.addEdge(1, 3, 1);
		graph.addEdge(2, 3, 4);

		return graph;
	}

	private static AdjacencyGraph getGraph1Directed() {
		int numNodes = 6;
		AdjacencyGraph graph = new AdjacencyGraph(numNodes, true);
		graph.addEdge(5, 0, 0);
		graph.addEdge(5, 2, 0);
		graph.addEdge(4, 0, 0);
		graph.addEdge(4, 1, 0);
		graph.addEdge(2, 3, 0);
		graph.addEdge(3, 1, 0);

		return graph;
	}

	private static AdjacencyGraph getGraph2Directed() {
		int numNodes = 5;
		AdjacencyGraph graph = new AdjacencyGraph(numNodes, true);
		graph.addEdge(0, 1, 0);
		graph.addEdge(1, 2, 0);
		graph.addEdge(3, 2, 0);
		graph.addEdge(3, 4, 0);

		return graph;
	}

	public static void main(String[] args) {
		AdjacencyGraph graph = getGraph2Undirected();
		System.out.println(graph.dijkstrasShortestPath(0, 8));
		//graph.bfs(0);
	}
}
