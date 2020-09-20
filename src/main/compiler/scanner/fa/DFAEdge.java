package main.compiler.scanner.fa;

import java.util.Set;

public class DFAEdge extends Edge {
	public DFAEdge(State from, State next, Set<Character> transitionChars) {
		super(from, next, transitionChars);
	}
}
