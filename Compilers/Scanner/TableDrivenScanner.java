package Scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class TableDrivenScanner {
	private static class Pair {
		DFAState_v1 node;
		int pos;
		
		public Pair(DFAState_v1 node, int pos) {
			this.node = node;
			this.pos = pos;
		}
	}
	//used to avoid terrible rollbakcs
	private int inputPos; 
	private List<List<Boolean>> failed;
	
	private FiniteAutomaton<DFAState_v1, DFAEdge_v1> fa;
	private List<List<DFAState_v1>> transitions; //rows are states, columns are chars
	private List<Set<Character>> charList;
	private Map<DFAState_v1, Integer> stateRowMapping;
	private DFAState_v1 errorState;
	int index = 0;
	
	public TableDrivenScanner(String regex) {
		errorState = createErrorState();
		createFA(regex);
		createCharList();
		createStateMapping();
		createTransitionTable();
		compressTransitionTable();
		transitions.forEach(list -> System.out.println(list));
	}
	
	public void nextWord(String word) {
		initScanner(word);
		DFAState_v1 curr = fa.getStartState();
		StringBuilder sb = new StringBuilder(word.length());
		Stack<Pair> stack = new Stack<>();
		stack.push(new Pair(errorState, -1));
		index = 0;
		
		while (curr != errorState) {
			char ch = word.charAt(index);
			sb.append(ch);
			index++;
			inputPos++;
			
			if (getFailure(curr, inputPos)) {
				break;
			}
			
			if (curr.isFinal()) {
				stack.clear();
			}
			stack.push(new Pair(curr, inputPos));
			curr = transition(curr, ch);
		}
		
		while (!curr.isFinal() && curr != errorState) {
			getFailureRow(curr).set(inputPos, true);
			Pair p = stack.pop();
			curr = p.node;
			inputPos = p.pos;
			sb.deleteCharAt(sb.length()-1);
			//rollBack();
		}
		
		if (curr.isFinal()) {
			return; //Type[State]
		}
		return; //invalid
	}
	
	private void initScanner(String stream) {
		inputPos = 0;
		transitions = new ArrayList<>(stateRowMapping.size());
		for (int i = 0; i < stateRowMapping.size(); i++) {
			transitions.add(new ArrayList<>(stream.length()));
		}
		for (DFAState_v1 state : fa.getStates()) {
			List<Boolean> row = getFailureRow(state);
			for (int i = 0; i < stream.length(); i++) {
				row.add(false);
			}
		}
	}
	
	private boolean getFailure(DFAState_v1 node, int index) {
		return getFailureRow(node).get(index);
	}
	
	private List<Boolean> getFailureRow(DFAState_v1 node) {
		return failed.get(node.getID());
	}
		
	private DFAState_v1 transition(DFAState_v1 node, char ch) {
		return getRow(node).get(getCharCategory(ch));
	}
	
	private int getCharCategory(char ch) {
		for (int i = 0; i < charList.size(); i++) {
			if (charList.get(i).contains(ch)) {
				return i;
			}
		}
		return -1;
	}
	
	private List<DFAState_v1> getRow(DFAState_v1 state) {
		return transitions.get(stateRowMapping.get(state));
	}
	
	private void createTransitionTable() {
		//Create rows, then columns
		Set<DFAState_v1> states = fa.getStates();
		transitions = new ArrayList<>(states.size());
		for (int i = 0; i < states.size(); i++) {
			transitions.add(new ArrayList<>(charList.size()));
		}
		for (DFAState_v1 state : states) {
			List<DFAState_v1> row = getRow(state);
			for (int i = 0; i < charList.size(); i++) {
				//character set is only size 1
				Set<Character> ch = charList.get(i);
				for (char c : ch) {
					row.add(getValidDFAState((DFAState_v1) state.transition(c)));
				}
			}
			
		}
	}
	
	private void compressTransitionTable() {
		for (int i = 0; i < charList.size(); i++) {
			for (int j = i + 1; j < charList.size(); j++) {
				if (columnsAreEqual(i, j, transitions)) {
					charList.get(i).addAll(charList.remove(j));
					removeColumn(transitions, j);
					j--;
				}
			}
		}
	}
	
	private void removeColumn(List<List<DFAState_v1>> transitions, int column) {
		if (transitions.get(0).size() < column) {
			return;
		}
		transitions.forEach(list -> list.remove(column));
	}
	
	private boolean columnsAreEqual(int col1, int col2, List<List<DFAState_v1>> transitions) {
		for (int row = 0; row < transitions.size(); row++) {
			List<?> rowList = transitions.get(row);
			if (rowList.get(col1) != rowList.get(col2)) {
				return false;
			}
		}
		return true;
	}
	
	private void createStateMapping() {
		Set<DFAState_v1> states = fa.getStates();
		Map<DFAState_v1, Integer> mapStateToRow = new HashMap<>(states.size());
		states.forEach(state -> mapStateToRow.put(state, state.getID()));
		stateRowMapping = mapStateToRow;
		System.out.println("Map size: " + stateRowMapping.size());
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
	
	private void createCharList() {
		Set<Character> letters = fa.getAlphabet();
		List<Set<Character>> letterList = new ArrayList<>(letters.size());
		letters.forEach(ch -> letterList.add(new HashSet<>(Arrays.asList(ch))));
		charList =  letterList;
		System.out.println("chars size " + charList.size());
	}
	
	private DFAState_v1 createErrorState() {
		DFAState_v1 state = new DFAState_v1(-1, false);
		char start = 0;
		char end = 127;
		DFAEdge_v1 edge = new DFAEdge_v1(state, state, Edge.getChars(start, end));
		state.add(edge);
		return state;
	}
	
	private DFAState_v1 getValidDFAState(DFAState_v1 node) {
		return node == null ? errorState : node;
	}
	
	public static void main(String[] args) {
		String regex = "r(0|1|2|3|4|5|6|7|8|9)+";
		TableDrivenScanner scanner = new TableDrivenScanner(regex);
	}
}
