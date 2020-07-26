package Scanner;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class FiniteAutomaton<S extends State<T>, T extends Edge<S>> {
	private Set<S> states;
	private Set<Character> alphabet;
	private Set<T> transitions;
	private S startingState;
	private Set<S> acceptingStates;

	public FiniteAutomaton(Set<S> states, Set<Character> alphabet, Set<T> transitions, S startingState, Set<S> acceptingStates) {
		this.states = states;
		this.alphabet = alphabet;
		this.transitions = transitions;
		this.startingState = startingState;
		this.acceptingStates = acceptingStates;
	}
	
	//bfs approach to iterating through graph
	public FiniteAutomaton(S graph) {
		states = new HashSet<>();
		alphabet = new HashSet<>();
		transitions = new HashSet<>();
		acceptingStates = new HashSet<>();
		
		startingState = graph;
		Queue<S> worklist = new LinkedList<>();
		worklist.add(graph);
		while (!worklist.isEmpty()) {
			S curr = worklist.remove();
			if (!states.contains(curr)) {
				states.add(curr);
				if (curr.isFinal()) {
					acceptingStates.add(curr);
				}
				for (T edge : curr.getEdges()) {
					transitions.add(edge);
					worklist.add(edge.getNext());
					alphabet.addAll(edge.getTransitionsChars());
				}
			}
		}
	}
	
	public Set<S> getStates() {
		return states;
	}
	
	public Set<Character> getAlphabet() {
		return alphabet;
	}
	
	public Set<T> getTransitions() {
		return transitions;
	}
	
	public S getStartState() {
		return startingState;
	}
	
	public Set<S> getAcceptingStates() {
		return acceptingStates;
	}
}
