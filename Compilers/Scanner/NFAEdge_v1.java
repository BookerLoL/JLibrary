package Scanner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NFAEdge_v1 extends Edge<NFAState_v1> {	
	public NFAEdge_v1(NFAState_v1 from, NFAState_v1 next, char transitionChar) {
		this(from, next, new HashSet<>(Arrays.asList(transitionChar)));
	}
	
	public NFAEdge_v1(NFAState_v1 from, NFAState_v1 next, Set<Character> transitionChars) {
		super(from, next, transitionChars);
	}
	
	public boolean equals(Object o) {
		 if (this == o)
	         return true;
	      if (o == null)
	         return false;
	      if (getClass() != o.getClass())
	         return false;
	      NFAEdge_v1 other = (NFAEdge_v1) o;
	      return getFrom().equals(other.getFrom()) && this.getNext().equals(other.getNext());	  
	}
	
	public static NFAEdge_v1 EMPTY_TRANSITION(State<NFAEdge_v1> from, State<NFAEdge_v1> next) {
		return new NFAEdge_v1((NFAState_v1) from, (NFAState_v1) next, EMPTY_TRANSITION);
	}
}
