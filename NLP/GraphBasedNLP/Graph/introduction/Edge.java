package introduction;

public class Edge {
	double weight;
	Vertex to;
	Vertex from;

	public Edge(double weight, Vertex from, Vertex to) {
		this.weight = weight;
		this.to = to;
		this.from = from;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof Edge)) {
			return false;
		}

		Edge edge = (Edge) o;
		return edge.getFrom().equals(getFrom()) && edge.getTo().equals(getTo());
	}
	
	public Vertex getFrom() {
		return from;
	}
	
	public Vertex getTo() {
		return to;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public String toString() {
		return "From: " + getFrom() + " edge weight: " + getWeight() + " to node: " + getTo() + "\t";
	}
}
