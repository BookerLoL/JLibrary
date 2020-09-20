package main.compiler.scanner.fa;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class FiniteAutomaton {
	private Set<State> states;
	private Set<Character> alphabet;
	private Set<Edge> transitions;
	private State startingState;
	private Set<State> acceptingStates;

	public FiniteAutomaton(Set<State> states, Set<Character> alphabet, Set<Edge> transitions, State startingState, Set<State> acceptingStates) {
		this.states = states;
		this.alphabet = alphabet;
		this.transitions = transitions;
		this.startingState = startingState;
		this.acceptingStates = acceptingStates;
	}
	
	//bfs approach to iterating through graph
	public FiniteAutomaton(State graph) {
		states = new HashSet<>();
		alphabet = new HashSet<>();
		transitions = new HashSet<>();
		acceptingStates = new HashSet<>();
		startingState = graph;
		
		Queue<State> worklist = new LinkedList<>();
		worklist.add(startingState);
		while (!worklist.isEmpty()) {
			State curr = worklist.remove();
			if (!states.contains(curr)) {
				states.add(curr);
				if (curr.isFinal()) {
					acceptingStates.add(curr);
				}
				
				for (Edge edge : curr.getEdges()) {
					transitions.add(edge);
					alphabet.addAll(edge.getTransitions());
					worklist.add(edge.getNext());
				}
			}
		}
	}
	
	public Set<State> getStates() {
		return states;
	}
	
	public Set<Character> getAlphabet() {
		return alphabet;
	}
	
	public Set<Edge> getTransitions() {
		return transitions;
	}
	
	public State getStartState() {
		return startingState;
	}
	
	public Set<State> getAcceptingStates() {
		return acceptingStates;
	}
}