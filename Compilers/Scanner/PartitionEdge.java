package Scanner;

import java.util.Set;

public class PartitionEdge extends Edge<PartitionState> {
	public PartitionEdge(PartitionState from, PartitionState next, Set<Character> transitionChars) {
		super(from, next, transitionChars);
	}
}
