package Scanner;

public class DirectCodedScanner {
	private FiniteAutomaton<DFAState_v1, DFAEdge_v1> fa;
	
	public DirectCodedScanner(String regex) {
		createFA(regex);
	}
	
	private void createFA(String regex) {
		NFAConverter nfaConv = new NFAConverter();
		DFAConverter dfaConv = new DFAConverter();
		MinimizedDFAConverter minDfaConv = new MinimizedDFAConverter();
		
		State<NFAEdge_v1> nfa = nfaConv.convert(regex);
		State<DFAEdge_v1> dfa = dfaConv.convert(nfa);
		DFAState_v1 minDfa = (DFAState_v1) minDfaConv.convert(dfa);
		minDfaConv.print(minDfa);
		fa = new FiniteAutomaton<DFAState_v1, DFAEdge_v1>(minDfa);
	}
	
	public void nextWord(String word) {
		//Basically just use labels and simulate the states
		
		//ex
		/*
		 * s0: 
		 * 		{
		 * 			nextChar();
		 * 			lexme += char;
		 * 			if state.isAccepting() 
		 * 				clear stack
		 *			if (char == 'r)
		 *				goto s1
		 *			else goto sout
 		 * 		}
 		 * 
 		 * sout:
 		 * 		while(!state.isAccepting() && state != bad) 
 		 * 			...
		 */
	}
}
