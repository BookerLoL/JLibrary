package main.compiler.scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import main.compiler.scanner.fa.*;

/*
 * This is an example of how a TableDrivenScanner could work
 * 
 * There are some issues like rollback not being implemented nor returning an enum/value for a specific value for nextWord
 * but that's purely due to not knowing the values
 */
public class TableDrivenScanner {
	private static class Pair {
		State node;
		int pos;

		public Pair(State node, int pos) {
			this.node = node;
			this.pos = pos;
		}
	}

	private int inputPos;
	private List<List<Boolean>> failed; 

	private FiniteAutomaton fa;
	private List<List<State>> transitions; // rows are states, columns are chars
	private List<Set<Character>> charList;
	private Map<State, Integer> stateRowMapping;
	private State errorState;
	int index = 0;

	public TableDrivenScanner(String regex) {
		errorState = createErrorState();
		createFA(regex);
		createCharList();
		createStateMapping();
		createTransitionTable();
		compressTransitionTable();
		//transitions.forEach(list -> System.out.println(list));
	}

	// Doesn't work since there is no type to return
	public void nextWord(String word) {
		initScanner(word);
		State curr = fa.getStartState();
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
			sb.deleteCharAt(sb.length() - 1);
			// rollBack();
		}

		if (curr.isFinal()) {
			return; // Type[State]
		}
		return; // invalid
	}

	private void initScanner(String stream) {
		inputPos = 0;
		transitions = new ArrayList<>(stateRowMapping.size());
		for (int i = 0; i < stateRowMapping.size(); i++) {
			transitions.add(new ArrayList<>(stream.length()));
		}
		for (State state : fa.getStates()) {
			List<Boolean> row = getFailureRow(state);
			for (int i = 0; i < stream.length(); i++) {
				row.add(false);
			}
		}
	}

	private boolean getFailure(State node, int index) {
		return getFailureRow(node).get(index);
	}

	private List<Boolean> getFailureRow(State node) {
		return failed.get(node.getID());
	}

	private State transition(State node, char ch) {
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

	private List<State> getRow(State state) {
		return transitions.get(stateRowMapping.get(state));
	}

	private void createTransitionTable() {
		// Create rows, then columns
		Set<State> states = fa.getStates();
		transitions = new ArrayList<>(states.size());
		for (int i = 0; i < states.size(); i++) {
			transitions.add(new ArrayList<>(charList.size()));
		}
		for (State state : states) {
			List<State> row = getRow(state);
			for (int i = 0; i < charList.size(); i++) {
				// character set is only size 1
				Set<Character> ch = charList.get(i);
				for (char c : ch) {
					row.add(getValidState(state.transition(c)));
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

	private void removeColumn(List<List<State>> transitions, int column) {
		if (transitions.get(0).size() < column) {
			return;
		}
		transitions.forEach(list -> list.remove(column));
	}

	private boolean columnsAreEqual(int col1, int col2, List<List<State>> transitions) {
		for (int row = 0; row < transitions.size(); row++) {
			List<State> rowList = transitions.get(row);
			if (rowList.get(col1) != rowList.get(col2)) {
				return false;
			}
		}
		return true;
	}

	private void createStateMapping() {
		Set<State> states = fa.getStates();
		Map<State, Integer> mapStateToRow = new HashMap<>(states.size());
		states.forEach(state -> mapStateToRow.put(state, state.getID()));
		stateRowMapping = mapStateToRow;
		//System.out.println("Map size: " + stateRowMapping.size());
	}

	private void createFA(String regex) {
		NFAConverter nfaConv = new NFAConverter();
		DFAConverter dfaConv = new DFAConverter();
		MinDFAConverter1 minDfaConv = new MinDFAConverter1();

		State nfa = nfaConv.convert(regex);
		State dfa = dfaConv.convert(nfa);
		State minDfa = minDfaConv.convert(dfa);
		fa = new FiniteAutomaton(minDfa);
	}

	private void createCharList() {
		Set<Character> letters = fa.getAlphabet();
		List<Set<Character>> letterList = new ArrayList<>(letters.size());
		letters.forEach(ch -> letterList.add(new HashSet<>(Arrays.asList(ch))));
		charList = letterList;
		//System.out.println("chars size " + charList.size());
	}

	private State createErrorState() {
		State state = StateFactory.create(Type.DFA, -1, false);
		char start = 0;
		char end = 127;
		Edge edge = EdgeFactory.create(Type.DFA, state, state, Utility.getChars(start, end));
		state.addEdge(edge);
		return state;
	}

	private State getValidState(State node) {
		return node == null ? errorState : node;
	}
}
