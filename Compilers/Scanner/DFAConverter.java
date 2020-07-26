package Scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

/*
 * I decided to use generics to maintain practice 
 * I couldve easily made this straightforward such as "NFA -> DFA" instead of saying "T extends NFA and U extends DFA"
 * 
 * I think I am checking extra cases that are not needed making this more ineffecient due to extra checks
 * 
 * Could also map epsilon closures and delta results but increases memory complexity, not worth the improvement tbh
 */
public class DFAConverter implements Convertible<State<DFAEdge_v1>, State<NFAEdge_v1>>, Printable<State<DFAEdge_v1>> {
	private int id = 0;

	@Override
	public State<DFAEdge_v1> convert(State<NFAEdge_v1> input) {
		id = 0;
		return subsetConstruction(input);
	}
	
	// algorithm name to convert NFA to DFA
	/*
	 * The algorithm could be improved if there is a way to hash a set into a node 
	 * perhaps using a checksum or compute a string to hash on 
	 */
	protected State<DFAEdge_v1> subsetConstruction(State<NFAEdge_v1> nfa) {
		//dfa does not contain empty transitions as part of the alphabet
		Set<Character> alphabet = new FiniteAutomaton<>((NFAState_v1) nfa).getAlphabet();
		alphabet.remove(Edge.EMPTY_TRANSITION);
	
		List<Set<State<NFAEdge_v1>>> seenSets = new LinkedList<>(); // do not store empty sets
		List<State<DFAEdge_v1>> dfaStates = new ArrayList<>(); //parallel  listing with seenSets as each set represents a dfa state

		//initial setup
		Set<State<NFAEdge_v1>> firstSet = this.epsilonClosure(new HashSet<>(Arrays.asList(nfa)));
		parallelAdd(seenSets, dfaStates, firstSet);
		List<Set<State<NFAEdge_v1>>> worklist = new LinkedList<>();
		worklist.add(firstSet);
		
		while (!worklist.isEmpty()) {
			Set<State<NFAEdge_v1>> currSet = worklist.remove(0);
			DFAState_v1 dfaSourceNode = (DFAState_v1) dfaStates.get(get(seenSets, currSet)); //grab the index then grab the dfa state
			for (char transitionVal : alphabet) {
				Set<State<NFAEdge_v1>> transitionedSet = epsilonClosure(delta(currSet, transitionVal));
				if (!transitionedSet.isEmpty()) {
					boolean isNew = parallelAdd(seenSets, dfaStates, transitionedSet);
					if (isNew) {
						worklist.add(transitionedSet);
					}
					
					DFAState_v1 transitionNode = (DFAState_v1) dfaStates.get(get(seenSets, transitionedSet));
					DFAEdge_v1 edge = getEdgeContaining(dfaSourceNode, transitionNode);
					if (edge == null) {
						dfaSourceNode.add(new DFAEdge_v1(dfaSourceNode, transitionNode, Edge.getChars(transitionVal, transitionVal)));
					} else {
						edge.getTransitionsChars().add(transitionVal); //This is used if there is a case: [a-zA-Z], as it will go through all letters and add it into the edge chars set
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
	protected Set<State<NFAEdge_v1>> epsilonClosure(Set<State<NFAEdge_v1>> nfa) {
		Set<State<NFAEdge_v1>> validStates = new HashSet<>();
		List<State<NFAEdge_v1>> worklist = new LinkedList<>();
		worklist.addAll(nfa);
		while (!worklist.isEmpty()) {
			State<NFAEdge_v1> node = worklist.remove(0);

			// avoids cycling issues
			if (!validStates.contains(node)) {
				for (NFAEdge_v1 edge : node.getEdges()) {
					// Each edge contains a set of characters, should only contain 1 which is the
					// empty
					if (edge.transitionChars.size() == 1
							&& edge.transitionChars.contains(NFAEdge_v1.EMPTY_TRANSITION)) {
						worklist.add(edge.next);
					}
				}
				validStates.add(node);
			}
		}
		return validStates;
	}

	/*
	 * Get all states that can be reached when transition using the transition value
	 * for each set of state
	 */
	protected Set<State<NFAEdge_v1>> delta(Set<State<NFAEdge_v1>> nfaStates, char transitionVal) {
		Set<State<NFAEdge_v1>> states = new HashSet<>();
		for (State<NFAEdge_v1> nfaState : nfaStates) {
			for (NFAEdge_v1 edge : nfaState.getEdges()) {				
				if (edge.getTransitionsChars().contains(transitionVal)) {
					states.add(edge.getNext());
				}
			}
		}
		return states;
	}
	
	private DFAEdge_v1 getEdgeContaining(State<DFAEdge_v1> source, State<DFAEdge_v1> target) {
		for (DFAEdge_v1 edge : source.getEdges()) {
			if (edge.getNext().equals(target)) {
				return edge;
			}
		}
		return  null;
	}

	/*
	 * If true for the first, then add for the second
	 */
	private <T extends Collection<State<NFAEdge_v1>>, U extends Collection<State<DFAEdge_v1>>> boolean parallelAdd(Collection<T> firstCollection, U secondCollection, T item) {
		Predicate<T> predicate = i -> get(firstCollection, i) == -1; // collection does not contain this item
		if (predicate.test(item)) {
			firstCollection.add(item);
			State<DFAEdge_v1> dfaNode = createDFA(item);
			secondCollection.add(dfaNode);
			return true;
		}
		return false;
	}

	/*
	 * Used to get the index of where it belongs, useful if there is parallel
	 * mapping of the collection
	 * 
	 * This is useful to check if a collection contains the expected value
	 */
	private static <T extends Collection<State<NFAEdge_v1>>> int get(Collection<T> collection, T value) {
		int index = 0;
		for (T item : collection) {
			if (isEqual(item, value)) {
				return index;
			}
			index++;
		}

		return -1;
	}

	private static <T> boolean isEqual(Collection<T> collection1, Collection<T> collection2) {
		if (collection1 == collection2) {
			return true;
		}

		if (collection1 == null || collection2 == null) {
			return false;
		}

		if (collection1.size() != collection2.size()) {
			return false;
		}

		return collection1.containsAll(collection2);
	}

	private <T extends State<NFAEdge_v1>> DFAState_v1 createDFA(Collection<T> nodes) {
		boolean isFinal = containsFinalState(nodes);
		DFAState_v1 dfaNode = new DFAState_v1(id, isFinal);
		id++;
		return dfaNode;

	}

	protected <T extends State<NFAEdge_v1>> boolean containsFinalState(Collection<T> nodes) {
		for (T node : nodes) {
			if (node.isFinal()) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * BFS representation
	 */
	@Override
	public void print(State<DFAEdge_v1> input) {
		Set<State<DFAEdge_v1>> seen = new HashSet<>();
		Queue<State<DFAEdge_v1>> nodes = new LinkedList<>();
		nodes.add(input);
		while (!nodes.isEmpty()) {
			State<DFAEdge_v1> node = nodes.remove();
			if (!seen.contains(node)) {
				System.out.println(node.toString());
				for (DFAEdge_v1 edge : node.getEdges()) {
					System.out.println("\t" + edge.toString());
					nodes.add(edge.next);
				}
				seen.add(node);
			}
		}
	}

	public static void main(String[] args) {
		String regex = "abc|bc|ad";
		//String regex = "a(b|c)*";
		//String regex = "fee|fie";
		NFAConverter nfaConv = new NFAConverter();
		State<NFAEdge_v1> nfa = nfaConv.convert(regex);
		//nfaConv.print(nfa);
		DFAConverter dfaConv = new DFAConverter();
		dfaConv.print(dfaConv.convert(nfa));
	}

}
