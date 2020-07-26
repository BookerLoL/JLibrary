package grammar;

import java.util.List;

import grammar.State.Transition;

/*
 * Notation:
 * 
 * T means Terminal
 * NT means Nonterminal
 * 
 * TTT means sequence of T's
 * NTNT, sequence of NT's
 * TTNT, sequence of T's then ending with NT
 */
public class GrammarRuleFormat {
	public static final char arrow = '→';
	public static final char or = '|';
	public static final char space = ' ';
	
	// A -> empty
	public static Transition emptyTransition() {
		return new Transition(new State(State.END, true), Transition.EMPTY);
	}
	
	// A -> a
	public static Transition transitionT(String transitionVal) {
		return new Transition(new State(State.END, true), transitionVal);
	}
	
	// A -> B
	public static Transition transitionNT(String nonterminalName) {
		return new Transition(new State(nonterminalName, false), Transition.EMPTY);
	}
	
	// A -> a1a2..an
	public static Transition transitionTT(String[] transitionVals) {
		if (transitionVals.length == 1) {
			return transitionT(transitionVals[0]);
		}
		
		Transition startTransition = new Transition(new State(State.NO_NAME, false), transitionVals[0]);
		State next = startTransition.getNext();
		
		for (int i = 1; i < transitionVals.length - 1; i++) {
			next.setTransition(new Transition(new State(State.NO_NAME, false), transitionVals[i]));
			next = next.getTransition().getNext();
		}
		
		next.setTransition(new Transition(new State(State.END, true), transitionVals[transitionVals.length - 1]));
		return startTransition;
	}
	
	// A -> a1a2..an
		public static Transition transitionTT(List<String> transitionVals) {
			if (transitionVals.size()== 1) {
				return transitionT(transitionVals.get(0));
			}
			
			Transition startTransition = new Transition(new State(State.NO_NAME, false), transitionVals.get(0));
			State next = startTransition.getNext();
			
			for (int i = 1; i < transitionVals.size() - 1; i++) {
				next.setTransition(new Transition(new State(State.NO_NAME, false), transitionVals.get(i)));
				next = next.getTransition().getNext();
			}
			
			next.setTransition(new Transition(new State(State.END, true), transitionVals.get(transitionVals.size()-1)));
			return startTransition;
		}
	
	// A -> aB
	public static Transition transitionTNT(String transitionVal, String nonterminalName) {
		return new Transition(new State(nonterminalName, false), transitionVal);
	}
	
	// A -> a1a2..anB
	public static Transition transitionTTNT(String[] transitionVals, String nonterminalName) {
		Transition transition = transitionTT(transitionVals);
		
		//modify last State
		State next = transition.getNext();
		while (!next.isTerminal()) {
			next = next.getTransition().getNext();
		}
		next.isTerminal = false;
		next.name = nonterminalName;
		
		return transition;
	}
	
	// A -> a1a2..anB
		public static Transition transitionTTNT(List<String> transitionVals, String nonterminalName) {
			Transition transition = transitionTT(transitionVals);
			
			//modify last State
			State next = transition.getNext();
			while (!next.isTerminal()) {
				next = next.getTransition().getNext();
			}
			next.isTerminal = false;
			next.name = nonterminalName;
			
			return transition;
		}
	
	// A -> B C D
	public static Transition transitionNTNT(String[] nonterminalNames) {
		if (nonterminalNames.length == 1) {
			return transitionNT(nonterminalNames[0]);
		}
		
		Transition startTransition = new Transition(new State(nonterminalNames[0], false), State.Transition.EMPTY);
		State next = startTransition.getNext();
		
		for (int i = 1; i < nonterminalNames.length; i++) {
			next.setTransition(new Transition(new State(nonterminalNames[i], false), State.Transition.EMPTY));
			next = next.getTransition().getNext();
		}
		
		return startTransition;
	}
	
	// A -> B C D
	public static Transition transitionNTNT(List<String> nonterminalNames) {
		if (nonterminalNames.size() == 1) {
			return transitionNT(nonterminalNames.get(0));
		}
		
		Transition startTransition = new Transition(new State(nonterminalNames.get(0), false), State.Transition.EMPTY);
		State next = startTransition.getNext();
		
		for (int i = 1; i < nonterminalNames.size(); i++) {
			next.setTransition(new Transition(new State(nonterminalNames.get(i), false), State.Transition.EMPTY));
			next = next.getTransition().getNext();
		}
		
		return startTransition;
	}
	
	public static void main(String[] args) {
		System.out.println(GrammarRuleFormat.emptyTransition());
		System.out.println(GrammarRuleFormat.transitionT("a"));
		System.out.println(GrammarRuleFormat.transitionNT("A"));
		System.out.println(GrammarRuleFormat.transitionTT(new String[]{"a", "b", "c"}));
		System.out.println(GrammarRuleFormat.transitionTNT("a", "B"));
		System.out.println(GrammarRuleFormat.transitionTTNT(new String[]{"a", "b", "c"}, "B"));
		System.out.println(GrammarRuleFormat.transitionNTNT(new String[]{"A", "B", "C"}));
	}
}
