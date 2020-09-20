package main.compiler.scanner.fa;

import java.util.Set;

public class PartitionState extends State {
	public static final String PREFIX = "p";
	private Set<State> nodes;

	public PartitionState(int id, boolean isFinal, Set<State> set) {
		super(PREFIX + id, isFinal);
		nodes = set;
	}

	public Set<State> getNodes() {
		return nodes;
	}

	public boolean contains(State node) {
		return getNodes().contains(node);
	}

	@Override
	public int getID() {
		return Integer.parseInt(getName().substring(PREFIX.length()));
	}

	public String toString() {
		return super.toString() + "\t" + getNodes();
	}
}
