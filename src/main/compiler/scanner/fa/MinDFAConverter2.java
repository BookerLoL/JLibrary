package main.compiler.scanner.fa;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

// Brzozowski's DFA minimization algorithm, NFA to min DFA
public class MinDFAConverter2 extends Converter<State, State> {
	DFAConverter dfaConv;

	public MinDFAConverter2() {
		dfaConv = new DFAConverter();
	}

	@Override
	public State convert(State input) {
		resetID(0);
		return brzozowskisMinimization(input);
	}

	private State brzozowskisMinimization(State input) {
		return dfaConv.convert(reverse(dfaConv.convert(reverse(input))));
	}

	// if multiple final states, add extra state as final to only have 1 final state
	private State reverse(State nfa) {
		Set<State> seen = new HashSet<>();
		List<Edge> edges = new LinkedList<>();
		Queue<State> worklist = new LinkedList<>();
		Queue<State> finalList = new LinkedList<>();
		worklist.add(nfa);
		int MAX_ID = Integer.MIN_VALUE; // ID's matter and we can't have dupliate id's because adding edges won't work
										// properly then
		while (!worklist.isEmpty()) {
			State state = worklist.remove();
			if (!seen.contains(state)) {
				for (Edge edge : state.getEdges()) {
					worklist.add(edge.getNext());
					edges.add(edge);
				}
				state.getEdges().clear();

				if (state.isFinal()) {
					finalList.add(state);
				}
				seen.add(state);
				if (state.getID() > MAX_ID) {
					MAX_ID = state.getID();
				}
			}
		}

		while (finalList.size() > 1) {
			State newFinal = StateFactory.create(Type.NFA, MAX_ID + 1, true);
			while (!finalList.isEmpty()) {
				State remainingfinalState = finalList.remove().setIsFinal(false);
				Edge newEndEdge = EdgeFactory.EMPTY_TRANSITION(remainingfinalState, newFinal);
				edges.add(newEndEdge);
			}
			finalList.add(newFinal);
		}

		for (Edge edge : edges) {
			edge.getNext().addEdge(EdgeFactory.create(Type.NFA, edge.getNext(), edge.getFrom(), edge.getTransitions()));
		}

		nfa.setIsFinal(true);
		return finalList.remove().setIsFinal(false);
	}

	@Override
	public String toString() {
		return "Converts NFA to Minimized DFA";
	}
}
