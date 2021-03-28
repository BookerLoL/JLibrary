package main.compiler.scanner.fa;

import java.util.Set;

public class PartitionEdge extends Edge {
	public PartitionEdge(State from, State next, Set<Character> transitionChars) {
		super(from, next, transitionChars);
	}
}

