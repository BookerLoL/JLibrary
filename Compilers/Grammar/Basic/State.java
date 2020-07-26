package grammar;

/*
 * Lets assume all nonterminals will be in uppercase
 * 
 * transitions are going to be lowercase char values
 * 
 * State can be a list due to having a transition field
 */
public class State {	
	protected static class Transition {
		public static final String EMPTY = "ε";
		State next;
		String transitionVal; 
		
		public Transition(State next) {
			this(next, EMPTY);
		}
		
		public Transition(State next, String transitionVal) {
			this.next = next;
			this.transitionVal = transitionVal;
		}
		
		public Transition(String transitionVal) {
			this(new State(State.END, true), transitionVal);
		}
		
		public String toString() {
			return "transition uses: " + transitionVal + " to " + next.toString();
		}
		
		public State getNext() {
			return next;
		}
		
		public String getTransitionValue() {
			return transitionVal;
		}
	}

	public static final String END = "End"; //terminal
	public static final String NO_NAME = ""; //A -> a1a2
	
	protected String name;
	protected boolean isTerminal;
	protected Transition transition;
	
	public State(String name, boolean isTerminal) {
		this(name, isTerminal, null);
	}
	
	
	public State(String name, boolean isTerminal, Transition transition) {
		this.name = name;
		this.isTerminal = isTerminal;
		this.transition = transition;
	}
	
	public boolean hasTransition() {
		return transition != null;
	}
	
	public boolean isTerminal() {
		return isTerminal;
	}
	
	public String getName() {
		return name;
	}
	
	public void setTransition(Transition transition) {
		this.transition = transition;
	}
	
	public Transition getTransition() {
		return transition;
	}
	
	public State getLast() {
		State current = this;
		while (current.hasTransition()) {
			current = current.getTransition().getNext();
		}
		return current;
	}
	
	public String toString() {
		String terminalMsg = isTerminal ? "T" : "NT";
		String transitionMsg= hasTransition() ? transition.toString() : " no transition ";
		return name + " is " + terminalMsg + " has " + transitionMsg;
	}
}
