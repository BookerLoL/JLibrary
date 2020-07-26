package Scanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class MinimizedDFAConverter
		implements Convertible<State<DFAEdge_v1>, State<DFAEdge_v1>>, Printable<State<DFAEdge_v1>> {
	private static class Partitioner {
		public static final BiPredicate<Collection<? extends State<?>>, Collection<? extends State<?>>> IS_EQUAL = (
				left, right) -> left == right || (left.size() == right.size() && left.containsAll(right));

		// S1 - S2
		public static Set<? extends State<?>> setDifference(Collection<? extends State<?>> left,
				Collection<? extends State<?>> right) {
			return left.stream().filter(item -> !right.contains(item)).collect(Collectors.toSet());
		}
	}

	int id = 0;

	//creates nonfinal partitions
	private PartitionState createPartition(Set<? extends State<DFAEdge_v1>> nodes) {
		PartitionState partition = new PartitionState(id, false, nodes);
		id++;
		return partition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public State<DFAEdge_v1> convert(State<DFAEdge_v1> input) {
		id = 0;
		// dfas do not have empty transitions
		FiniteAutomaton<DFAState_v1, DFAEdge_v1> fa = new FiniteAutomaton<>((DFAState_v1) input);
		Set<Character> alphabet = fa.getAlphabet();
		
		Set<DFAState_v1> finalStates = fa.getAcceptingStates();
		Set<DFAState_v1> nonFinalStates = (Set<DFAState_v1>) Partitioner.setDifference(fa.getStates(), finalStates);
		PartitionState acceptingStates = createPartition(finalStates);
		PartitionState nonAcceptingStates = createPartition(nonFinalStates);
		Set<PartitionState> temp = new HashSet<>();
		Set<PartitionState> partitions = new HashSet<>();
		temp.add(acceptingStates);
		temp.add(nonAcceptingStates);

		while (!Partitioner.IS_EQUAL.test(temp, partitions)) {
			partitions.addAll(temp);
			temp.clear();

			for (PartitionState partition : partitions) {
				temp.add(partition);
				if (split(partitions, partition, alphabet, temp)) {
					break;
				}
			}
		}

		partitions = createLinksAndUpdatePartitions(partitions, alphabet);
		return translatePartitionsToMinimizedDFA(partitions, fa.getStartState());
	}

	@SuppressWarnings("unchecked")
	private boolean split(Set<PartitionState> allPartitions, PartitionState testPartition, Set<Character> alphabet, Collection<PartitionState> currList) {
		if (testPartition.getNodes().size() == 1) {
			return false;
		}
		
		PartitionState trackPartition = null;
		Set<State<DFAEdge_v1>> samePartitionDFANodes = new HashSet<>(); 
																		
		for (char letter : alphabet) {
			trackPartition = null;
			samePartitionDFANodes.clear();
			for (State<DFAEdge_v1> dfaNode : testPartition.getNodes()) {
				State<DFAEdge_v1> transitionedNode = dfaNode.transition(letter);
				if (transitionedNode != null) {
					PartitionState transitionedPartition = getPartitionContaining(transitionedNode, allPartitions);
					if (trackPartition == null) {
						trackPartition = transitionedPartition;
					}

					if (trackPartition.equals(transitionedPartition)) {
						samePartitionDFANodes.add(dfaNode);
					}
				}
			}

			//Protects against null cases
			if (!samePartitionDFANodes.isEmpty() &&  (samePartitionDFANodes.size() != testPartition.getNodes().size())) { 
				Set<State<DFAEdge_v1>> splitPartitionNodes = (Set<State<DFAEdge_v1>>) Partitioner.setDifference(testPartition.getNodes(), samePartitionDFANodes);
				testPartition.getNodes().removeAll(splitPartitionNodes);
				PartitionState splitPartition = createPartition(splitPartitionNodes);
				currList.add(splitPartition);
				return true;
			}
		}
		return false;
	}
	
	private State<DFAEdge_v1> translatePartitionsToMinimizedDFA(Set<PartitionState> updatedPartitions, State<DFAEdge_v1> startNode) {	
		List<State<DFAEdge_v1>> minimizedDFANodes = new ArrayList<>(updatedPartitions.size());
		for (int i = 0; i < updatedPartitions.size(); i++) {
			minimizedDFANodes.add(new DFAState_v1(i, false));
		}
		
		PartitionState startPartition = null;
		for (PartitionState partition : updatedPartitions) {
			if (partition.contains(startNode)) {
				startPartition = partition;
			}
			int dfaIndex = partition.getID();
			State<DFAEdge_v1> dfaNode =  minimizedDFANodes.get(dfaIndex);
			if (partition.isFinal()) {
				dfaNode.changeToFinal(true);
			}
			
			for (PartitionEdge pEdge: partition.getEdges()) {
				int dfaToIndex = pEdge.getNext().getID();
				State<DFAEdge_v1> toNode = minimizedDFANodes.get(dfaToIndex);
				dfaNode.add(new DFAEdge_v1((DFAState_v1) dfaNode, (DFAState_v1) toNode, pEdge.getTransitionsChars()));
			}
		}
	
		int dfaIndex = startPartition.getID();
		return minimizedDFANodes.get(dfaIndex);
	}
	

	private Set<PartitionState> createLinksAndUpdatePartitions(Set<PartitionState> partitions, Set<Character> alphabet) {
		for (PartitionState partition : partitions) {
			for (char letter : alphabet) {
				for (State<DFAEdge_v1> dfaNode : partition.getNodes()) {
					if (dfaNode.isFinal()) {
						partition.changeToFinal(true);
					}
					
					State<DFAEdge_v1> transitionedNode = dfaNode.transition(letter);
					if (transitionedNode != null) {
						PartitionState transitionedPartition = getPartitionContaining(transitionedNode, partitions);
						PartitionEdge edge = getTransition(partition, transitionedPartition);
						if (edge != null) {
							edge.getTransitionsChars().add(letter);
						} else {
							edge = new PartitionEdge(partition, transitionedPartition, Edge.getChars(letter, letter));
							partition.add(edge);
						}
					}
				}
			}
		}
		return partitions;
	}
	
	private PartitionEdge getTransition(PartitionState from, PartitionState to) {
		for (PartitionEdge edge : from.getEdges()) {
			if (edge.getNext().equals(to)) {
				return edge;
			}
		}
		return null;
	}
	
	private PartitionState getPartitionContaining(State<DFAEdge_v1> dfaNode, Collection<PartitionState> partitions) {
		for (PartitionState partition : partitions) {
			if (partition.contains(dfaNode)) {
				return partition;
			}
		}
		System.out.println(partitions);
		System.out.println("Failed to find: " + dfaNode);
		return null;
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

	public static void main(String[] args) {
		//String regex = "a(b|c)*|who|what|where|abs|fee|fie";
		String regex = "ab|(ab)*c";
		//String regex = "(a)c?";
		//String regex = "who|what|where";
		NFAConverter nfaConv = new NFAConverter();
		State<NFAEdge_v1> nfa = nfaConv.convert(regex);
		DFAConverter dfaConv = new DFAConverter();
		MinimizedDFAConverter mdfa = new MinimizedDFAConverter();
		State<DFAEdge_v1> dfa = dfaConv.convert(nfa);
		mdfa.print(mdfa.convert(dfa));
	}
}
