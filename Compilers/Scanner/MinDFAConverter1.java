package scanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Hopcroft’s Algorithm, Converts DFA into a Minimized DFA
public class MinDFAConverter1 extends Converter<State, State> {
	@Override
	public State convert(State input) {
		resetID(0);
		// dfas do not have empty transitions
		FiniteAutomaton fa = new FiniteAutomaton(input);
		Set<Character> alphabet = fa.getAlphabet();
		Set<State> finalStates = fa.getAcceptingStates();
		Set<State> nonFinalStates = Utility.Partitioner.setDifference(fa.getStates(), finalStates);
		PartitionState acceptingStates = (PartitionState) StateFactory.create(Type.PARTITION, id++, false, finalStates);
		PartitionState nonAcceptingStates = (PartitionState) StateFactory.create(Type.PARTITION, id++, false,
				nonFinalStates);

		Set<PartitionState> temp = new HashSet<>();
		Set<PartitionState> partitions = new HashSet<>();
		temp.add(acceptingStates);
		temp.add(nonAcceptingStates);

		while (!Utility.Partitioner.IS_EQUAL(temp, partitions)) {
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

	private boolean split(Set<PartitionState> allPartitions, PartitionState testPartition, Set<Character> alphabet,
			Collection<PartitionState> currList) {
		if (testPartition.getNodes().size() == 1) {
			return false;
		}

		PartitionState trackPartition = null;
		Set<State> samePartitionDFANodes = new HashSet<>();

		for (char letter : alphabet) {
			trackPartition = null;
			samePartitionDFANodes.clear();
			for (State dfaNode : testPartition.getNodes()) {
				State transitionedNode = dfaNode.transition(letter);
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

			if (!samePartitionDFANodes.isEmpty() && (samePartitionDFANodes.size() != testPartition.getNodes().size())) {
				Set<State> splitPartitionNodes = Utility.Partitioner.setDifference(testPartition.getNodes(),
						samePartitionDFANodes);
				testPartition.getNodes().removeAll(splitPartitionNodes);
				PartitionState splitPartition = (PartitionState) StateFactory.create(Type.PARTITION, id++, false,
						splitPartitionNodes);
				currList.add(splitPartition);
				return true;
			}
		}
		return false;
	}

	private State translatePartitionsToMinimizedDFA(Set<PartitionState> updatedPartitions, State startNode) {
		List<State> minimizedDFANodes = new ArrayList<>(updatedPartitions.size());
		for (int i = 0; i < updatedPartitions.size(); i++) {
			minimizedDFANodes.add(StateFactory.create(Type.DFA, i, false));
		}

		PartitionState startPartition = null;
		for (PartitionState partition : updatedPartitions) {
			if (partition.contains(startNode)) {
				startPartition = partition;
			}
			int dfaIndex = partition.getID();
			State dfaNode = minimizedDFANodes.get(dfaIndex);
			if (partition.isFinal()) {
				dfaNode.setIsFinal(true);
			}

			for (Edge pEdge : partition.getEdges()) {
				int dfaToIndex = pEdge.getNext().getID();
				State toNode = minimizedDFANodes.get(dfaToIndex);
				dfaNode.addEdge(EdgeFactory.create(Type.DFA, dfaNode, toNode, pEdge.getTransitions()));
			}
		}

		int dfaIndex = startPartition.getID();
		return minimizedDFANodes.get(dfaIndex);
	}

	private Set<PartitionState> createLinksAndUpdatePartitions(Set<PartitionState> partitions,
			Set<Character> alphabet) {
		for (PartitionState partition : partitions) {
			for (char letter : alphabet) {
				for (State dfaNode : partition.getNodes()) {
					if (dfaNode.isFinal()) {
						partition.setIsFinal(true);
					}

					State transitionedNode = dfaNode.transition(letter);
					if (transitionedNode != null) {
						State transitionedPartition = getPartitionContaining(transitionedNode, partitions);
						Edge edge = Edge.getEdgeContaining(partition, transitionedPartition);
						if (edge != null) {
							edge.getTransitions().add(letter);
						} else {
							edge = EdgeFactory.create(Type.PARTITION, partition, transitionedPartition,
									Utility.getChars(letter, letter));
							partition.addEdge(edge);
						}
					}
				}
			}
		}
		return partitions;
	}

	private PartitionState getPartitionContaining(State dfaNode, Collection<PartitionState> partitions) {
		for (PartitionState partition : partitions) {
			if (partition.contains(dfaNode)) {
				return partition;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "DFA to Minimized DFA";
	}
}
