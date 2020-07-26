package Scanner;

import java.util.Set;

public class PartitionState extends State<PartitionEdge> {
	public static final String PREFIX = "p";
	protected Set<? extends State<DFAEdge_v1>> nodes;
	
	public PartitionState(int id, boolean isFinal, Set<? extends State<DFAEdge_v1>> set) {
		super(PREFIX + id, isFinal);
		nodes = set;
	}

	public Set<? extends State<DFAEdge_v1>> getNodes() {
		return nodes;
	}
	
	public boolean contains(State<? extends DFAEdge_v1> node) {
		return nodes.contains(node);
	}
	
	public String toString() {
		return super.toString() + "\t" + nodes.toString();
	}
	
	public int getID() {
		return Integer.parseInt(getName().substring(PREFIX.length()));
	}
}
