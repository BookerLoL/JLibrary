package scanner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EdgeFactory {
	public static Edge create(Type type, State from, State next, Character... transitionChars) {
		return create(type, from, next, new HashSet<>(Arrays.asList(transitionChars)));
	}

	public static Edge create(Type type, State from, State next, Set<Character> transitionChars) {
		switch (type) {
		case NFA:
			return new NFAEdge(from, next, transitionChars);
		case DFA:
			return new DFAEdge(from, next, transitionChars);
		case PARTITION:
			return new PartitionEdge(from, next, transitionChars);
		}
		return null;
	}

	//only NFA states can have empty transitions
	public static Edge EMPTY_TRANSITION(State from, State next) { 
		return create(Type.NFA, from, next, Edge.EMPTY_TRANSITION);
	}
}
