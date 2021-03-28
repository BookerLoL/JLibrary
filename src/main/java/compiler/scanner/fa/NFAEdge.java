package main.compiler.scanner.fa;

import java.util.Set;

public class NFAEdge extends Edge {
	public NFAEdge(State from, State next, Set<Character> transitionChars) {
		super(from, next, transitionChars);
	}
}
