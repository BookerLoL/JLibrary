package main.compiler.scanner.fa;

import java.util.Set;

public class StateFactory {
	public static State create(Type type, int id, boolean isFinal) {
		return create(type, id, isFinal, null);
	}
	
	protected static State create(Type type, int id, boolean isFinal, Set<State> nodes) {
		switch (type) {
		case NFA:
			return new NFAState(id, isFinal);
		case DFA:
			return new DFAState(id, isFinal);
		case PARTITION:
			return new PartitionState(id, isFinal, nodes);
		}
		return null;
	}
}
