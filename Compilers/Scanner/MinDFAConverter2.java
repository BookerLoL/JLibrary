package Scanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MinDFAConverter2 implements Convertible<State<DFAEdge_v1>, State<NFAEdge_v1>>, Printable<State<DFAEdge_v1>> {
	//Uses Brzozowski's DFA minimization algorithm
	//takes NFA to min DFA
	//avoids using partitions
	DFAConverter subsetConstructor;
	
	public MinDFAConverter2() {
		subsetConstructor = new DFAConverter();
	}
	
	private State<DFAEdge_v1> brzozowskisMinimization(State<NFAEdge_v1> input)  {
		return subsetConstructor.convert(reverse(cast(subsetConstructor.convert(reverse(input)))));
	}
	
	private State<NFAEdge_v1> cast(State<DFAEdge_v1> dfa) {
		State<NFAEdge_v1> start = create(dfa.getID(), dfa.isFinal());
		Set<State<DFAEdge_v1>> seen = new HashSet<>();
		Queue<State<DFAEdge_v1>> worklist = new LinkedList<>();
		Map<Integer, State<NFAEdge_v1>> mapping = new HashMap<>();
		worklist.add(dfa);
		mapping.put(dfa.getID(), start);
		while (!worklist.isEmpty()) {
			State<DFAEdge_v1> node = worklist.remove();
			State<NFAEdge_v1> curr = mapping.get(node.getID());
			if (!seen.contains(node)) {
				for (DFAEdge_v1 edge : node.getEdges()) {
					worklist.add(edge.getNext());
					State<NFAEdge_v1> nextNFA = mapping.containsKey(edge.getNext().getID()) ? mapping.get(edge.getNext().getID()) : create(edge.getNext().getID(), edge.getNext().isFinal());
					if (!mapping.containsKey(edge.getNext().getID())) {
						mapping.put(edge.getNext().getID(), nextNFA);
					}
					curr.add(new NFAEdge_v1((NFAState_v1)curr, (NFAState_v1)nextNFA, edge.getTransitionsChars()));
				}
				seen.add(node);
			}
		}
		return start;
	}
	
	private State<NFAEdge_v1> create(int id, boolean isFinal) {
		return new NFAState_v1(id, isFinal);
	}
	
	//adds an extra node if there are multiple final states to ensure there is only 1 final state
	private State<NFAEdge_v1> reverse(State<NFAEdge_v1> nfa) {
		int maxID = -1;
		Set<State<NFAEdge_v1>> seen = new HashSet<>();
		List<Edge<NFAState_v1>> edges = new LinkedList<>();
		Queue<State<NFAEdge_v1>> worklist = new LinkedList<>();
		Queue<State<NFAEdge_v1>> finalList = new LinkedList<>();
		worklist.add(nfa);
		while (!worklist.isEmpty()) {
			State<NFAEdge_v1> state = worklist.remove();
			if (!seen.contains(state)) {
				for (NFAEdge_v1 edge : state.getEdges()) {
					worklist.add(edge.getNext());
					edges.add(edge);
				}
				state.getEdges().clear();
				
				if (state.isFinal()) {
					finalList.add(state);
				}
				
				if (state.getID() > maxID) {
					maxID = state.getID();
				}
			}
		}
		
		if (finalList.size() > 1) {
			State<NFAEdge_v1> newEnd = new NFAState_v1(maxID+1, true);
			while (!finalList.isEmpty()) {
				State<NFAEdge_v1> finalState = finalList.remove();	
				finalState.changeToFinal(false);
				NFAEdge_v1 newEndEdge = NFAEdge_v1.EMPTY_TRANSITION(finalState, newEnd);
				edges.add(newEndEdge);
			}
			finalList.add(newEnd);
		}
		
		for (Edge<NFAState_v1> edge : edges) {
			edge.getNext().getEdges().add(new NFAEdge_v1(edge.getNext(), edge.getFrom(), edge.getTransitionsChars()));
		}
		
		nfa.changeToFinal(true);
		return finalList.remove().changeToFinal(false);
	}
	
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

	@Override
	public State<DFAEdge_v1> convert(State<NFAEdge_v1> input) {
		return brzozowskisMinimization(input);
	}
	
	public static void main(String[] args) {
		//String regex = "a(b|c)*";
		String regex = "abc|bc|ad";
		NFAConverter nfaConv = new NFAConverter();
		MinDFAConverter2 mindfa = new MinDFAConverter2();
		mindfa.print(mindfa.convert(nfaConv.convert(regex)));
	}
}
