package main.compiler.scanner.fa;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public abstract class Converter<T, S extends State> {
	public abstract String toString();

	public abstract S convert(T input);

	protected int id = 0;
	
	protected void resetID(int startingID) {
		id = startingID;
	}

	// BFS representation
	public static void print(State input) {
		Set<State> seen = new HashSet<>();
		Queue<State> nodes = new LinkedList<>();
		nodes.add(input);
		while (!nodes.isEmpty()) {
			State node = nodes.remove();
			if (!seen.contains(node)) {
				System.out.println(node);
				for (Edge edge : node.getEdges()) {
					System.out.println("\t" + edge);
					nodes.add(edge.getNext());
				}
				seen.add(node);
			}
		}
	}
}
