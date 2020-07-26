package Scanner;

import java.util.HashSet;
import java.util.Set;

public class Edge<T extends State<?>> {
	public static final char EMPTY_TRANSITION = '\0';

	public T from;
	public T next;
	public Set<Character> transitionChars;
	
	public Edge(T from, T next, Set<Character> transitionChars) {
		this.from = from;
		this.next = next; 
		this.transitionChars = transitionChars;
	}
	
	public static Set<Character> getChars(char from, char toInclusive) {
		Set<Character> transitions = new HashSet<>();
		char curr = from;
		while (curr <= toInclusive) {
			transitions.add(curr);
			curr++;
		}
		return transitions;
	}
	
	public T getFrom() {
		return from;
	}
	
	public T getNext() {
		return next;
	}
	
	public Set<Character> getTransitionsChars() {
		return transitionChars;
	}
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		 if (this == o)
	         return true;
	      if (o == null)
	         return false;
	      if (getClass() != o.getClass())
	         return false;
	      Edge<T> other = (Edge<T>) o;
	      return this.getFrom().equals(other.getFrom()) && this.getNext().equals(other.getNext());	  
	}
	

	public String toString() {
		return "From: " + from.getName() + " to " + next.getName() + " using: " + transitionChars.toString();
	}
}
