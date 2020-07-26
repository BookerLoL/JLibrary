package Scanner;

import java.util.Set;

public class DFAEdge_v1 extends Edge<DFAState_v1> {
	public DFAEdge_v1(DFAState_v1 from, DFAState_v1 next, Set<Character> transitionChars) {
		super(from, next, transitionChars);
	}
}
