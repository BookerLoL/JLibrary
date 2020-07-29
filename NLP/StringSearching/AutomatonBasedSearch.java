package string_searching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * Constructs a DFA to walk through the String
 * 
 * https://www.cs.auckland.ac.nz/courses/compsci369s1c/lectures/GG-notes/CS369-StringAlgs.pdf
 */
public class AutomatonBasedSearch extends Matcher {	
	protected static class State {
		protected String name; 
		protected boolean isFinal;
		protected Map<Character, State> edges;
		
		public State(String name, boolean isFinal) {
			this.name = name;
			this.isFinal = isFinal;
			edges = new HashMap<>();
		}
		
		public void addEdge(char transitionCh, State destination) {
			edges.put(transitionCh, destination);
		}
				
		public State transition(char transition) {
			return edges.get(transition);
		}
		
		public static State getEmptyState() {
			return new State("", false);
		}
		
		public String toString() {
			return name + " is final: " + isFinal;
		}
		
	}

	protected static class DFA {
		/*
		 * Ordered states with names from:
		 * ""
		 * n1
		 * n1n2
		 * n1n2n3
		 * ...
		 */
		private List<State> states;
		private State current;
		
		public DFA(String pattern) {
			buildDFA(pattern);
		}
		
		private void buildDFA(String pattern) {
			states = new ArrayList<>(pattern.length() + 1); //Account for Empty State
			states.add(State.getEmptyState()); 
			
			Set<Character> alphabet = new HashSet<>();
			StringBuilder sb = new StringBuilder(pattern.length());
			
			//Create the alphabet and states
			for (int i = 0; i < pattern.length(); i++) {
				char curr = pattern.charAt(i);
				alphabet.add(curr);
				sb.append(curr);
				states.add(new State(sb.toString(), false));	
			}
			sb = null; 
			states.get(states.size()-1).isFinal = true; //Make last state as final
			current = getEmptyState(); //initialize current
			
			//set up edges now
			for (State state : states) {
				for (char letter : alphabet) {
					String nextName = state.name + letter;
					State transitionState = contains(nextName);
					if (transitionState != null) {
						state.addEdge(letter, transitionState);
					} else { //find longest suffix which is prefix 
						State bestDestination = getEmptyState();
						State curr;
						String suffix = "";
						for (int i = nextName.length()-1; i > 0; i--) {
							suffix = nextName.charAt(i) + suffix;
							curr = contains(suffix);
							if (curr != null) {
								bestDestination = curr;
							}
						}
						state.addEdge(letter, bestDestination);
					}
				}
			}
			
			
		}
		
		//The list should be in order from shortest to longest length
		private State contains(String name) {
			if (name.length() >= states.size()) { 
				return null; 
			}
			
			State state = states.get(name.length());	
			return state.name.equals(name) ? state : null;
		}
		
		private State getEmptyState() {
			return states.get(0);
		}
		
		public void transition(char ch) { 
			current = current.transition(ch);
			if (current == null) { //encountered a character not in the alphabet, go reset
				current = getEmptyState();
			}
		}
			
		public boolean isMatching() {
			return current.isFinal;
		}
	}

	@Override
	public List<Integer> matches(String text, String pattern) {
		DFA dfa = new DFA(pattern);
		List<Integer> indices = new LinkedList<>();
		for (int i= 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			dfa.transition(ch);
			if (dfa.isMatching()) {
				indices.add(i - pattern.length() + 1);
			}
		}
		return indices;
	}


	@Override
	public boolean contains(String text, String pattern) {
		DFA dfa = new DFA(pattern);
		for (int i= 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			dfa.transition(ch);
			if (dfa.isMatching()) {
				return true;
			}
		}
		return false;
	}
}
