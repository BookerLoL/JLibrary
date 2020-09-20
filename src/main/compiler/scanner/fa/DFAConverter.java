package main.compiler.scanner.fa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

//algorithm name to convert NFA to DFA
public class DFAConverter extends Converter<State, State> {
	public State convert(State input) {
		resetID(0);
		return subsetConstruction(input);
	}

	private State subsetConstruction(State nfa) {
		// dfa does not contain empty transitions as part of the alphabet
		Set<Character> alphabet = new FiniteAutomaton(nfa).getAlphabet();
		alphabet.remove(Edge.EMPTY_TRANSITION);

		List<Set<State>> seenSets = new LinkedList<>(); // do not store empty sets
		List<State> dfaStates = new ArrayList<>(); // parallel listing with seenSets as each set represents a dfa state

		// initial setup
		Set<State> firstSet = epsilonClosure(new HashSet<>(Arrays.asList(nfa)));
		parallelAdd(seenSets, dfaStates, firstSet);
		List<Set<State>> worklist = new LinkedList<>();
		worklist.add(firstSet);

		while (!worklist.isEmpty()) {
			Set<State> currSet = worklist.remove(0);
			State dfaSourceNode = dfaStates.get(Utility.get(seenSets, currSet));
			for (char transitionVal : alphabet) {
				Set<State> transitionedSet = epsilonClosure(delta(currSet, transitionVal));
				if (!transitionedSet.isEmpty()) {
					if (parallelAdd(seenSets, dfaStates, transitionedSet)) {
						worklist.add(transitionedSet);
					}

					State transitionNode = dfaStates.get(Utility.get(seenSets, transitionedSet));
					Edge edge = Edge.getEdgeContaining(dfaSourceNode, transitionNode);
					if (edge == null) {
						dfaSourceNode.addEdge(EdgeFactory.create(Type.DFA, dfaSourceNode, transitionNode,
								Utility.getChars(transitionVal, transitionVal)));
					} else {
						edge.getTransitions().add(transitionVal); // Need this for multiple transitions
					}
				}
			}
		}
		return dfaStates.remove(0);
	}

	/*
	 * Returns a set of States that can be reached by only an episilon character
	 * 
	 * Includes the starting state since it's already reached
	 */
	private Set<State> epsilonClosure(Set<State> nfa) {
		Set<State> validStates = new HashSet<>();
		List<State> worklist = new LinkedList<>();
		worklist.addAll(nfa);
		while (!worklist.isEmpty()) {
			State node = worklist.remove(0);

			// avoids cycling issues
			if (!validStates.contains(node)) {
				for (Edge edge : node.getEdges()) {
					// Each edge contains a set of characters, should only contain 1 which is empty
					if (edge.transitionChars.size() == 1 && edge.transitionChars.contains(NFAEdge.EMPTY_TRANSITION)) {
						worklist.add(edge.next);
					}
				}
				validStates.add(node);
			}
		}
		return validStates;
	}

	//Get all states reached when using that transition value
	private Set<State> delta(Set<State> nfaStates, char transitionVal) {
		Set<State> states = new HashSet<>();
		for (State nfaState : nfaStates) {
			for (Edge edge : nfaState.getEdges()) {
				if (edge.hasTransition(transitionVal)) {
					states.add(edge.getNext());
				}
			}
		}
		return states;
	}

	private <T extends Collection<State>, U extends Collection<State>> boolean parallelAdd(
			Collection<T> firstCollection, U secondCollection, T item) {
		if (Utility.get(firstCollection, item) == -1) {
			firstCollection.add(item);
			secondCollection.add(StateFactory.create(Type.DFA, id++, Utility.containsFinalState(item)));
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "NFA to DFA";
	}
}
