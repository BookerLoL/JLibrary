package Scanner;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

/*
 * Takes in regular expressions
 * 
 * Based on Thompson's construction to translate Regular Expression to NFA 	
 * 
 * 
 * Currently doesn't handle cases where uses want to use the operators as transitions
 * but could easily be implemented by checking if there is an escape character before it '\'
 * 
 * It would be better to construct a tree in which operations should be operated first to make it more explict
 * 
 * Operation hierachy (execute first)
 * ( )  (ex: (...) ), parentheses; 
 * [ ]  (ex: [a-z]), set bracket
 *   *  (ex: a*), closure
 *   ?  (ex: a?), question mark, zero or one
 *   +  (ex: a+), plus, one or more
 *   |  (ex: a | b), alternation
 *      (ex: ab) , concantenation 
 *   	
 *   
 *   
 *  The class has been extended to support  + and ? operations
 */
public class NFAConverter implements Convertible<State<NFAEdge_v1>, String>, Printable<State<NFAEdge_v1>> {
	private static final char L_PAREN = '(';
	private static final char R_PAREN = ')';
	private static final char ALTERATION = '|';
	private static final char CLOSURE = '*';
	private static final char L_SET = '[';
	private static final char R_SET = ']';
	private static final char PLUS = '+';
	private static final char QUESTION_MARK = '?';
	
	private int id = 0;
	
	@Override
	public State<NFAEdge_v1> convert(String input) {
		resetIdCount();
		return construct(0, input.length(), input, new LinkedList<>());
	}
	
	private void resetIdCount() {
		id = 0;
	}
	
	private State<NFAEdge_v1> construct(int fromIndex, int toIndex, String input, Deque<State<NFAEdge_v1>> nfas) {
		int index = fromIndex;
		
		while (index < toIndex) {
			char ch = input.charAt(index);
			if (isOperator(ch)) {
				if (ch == ALTERATION) {
					index += constructAlternation(index, nfas, input);
				} else if (ch == CLOSURE) {
					constructClosure(nfas);
				} else if (ch == L_PAREN) {
					index += constructParentheses(index, nfas, input);
				} else if (ch == L_SET) {
					//finish  constructSet later, [a-z]
					index += constructSet(index, nfas, input);
				} else if (ch == PLUS) {
					constructPlus(nfas);
				} else if (ch == QUESTION_MARK) {
					constructQuestionMark(nfas);
				}
			} else { //create one character NFA
				constructSingleCharNFA(ch, nfas);
			}
			index++;
		}
		return combineAll(nfas);
	}
	
	private boolean isOperator(char ch) {
		return ch == ALTERATION || ch == CLOSURE || ch == L_PAREN || ch == R_PAREN || ch == L_SET || ch == R_SET || ch == PLUS || ch == QUESTION_MARK;
	}
	
	private NFAState_v1 createState(boolean isFinal) {
		NFAState_v1 state = new NFAState_v1(id, isFinal);
		id++;
		return state;
	}
	
	/*
	 *   "Left | Right" 
	 */
	private int constructAlternation(int startIndex, Deque<State<NFAEdge_v1>> nfas, String input) {
		String afterAlternation = input.substring(startIndex+1);
		State<NFAEdge_v1> rightPart = construct(0, afterAlternation.length(), afterAlternation, new LinkedList<>());
		State<NFAEdge_v1> leftPart = combineAll(nfas);
		State<NFAEdge_v1> alternatedNFA = addAlternation((NFAState_v1)leftPart, (NFAState_v1)rightPart);
		nfas.add(alternatedNFA);
		return afterAlternation.length();
	}
	
	private State<NFAEdge_v1> addAlternation(NFAState_v1 firstNFA, NFAState_v1 secondNFA) {	
		NFAState_v1  newStart = createState(false);
		NFAState_v1  newEnd = createState(true);
		State<NFAEdge_v1> firstNFAEnd = getEnd(firstNFA).changeToFinal(false);
		State<NFAEdge_v1>  secondNFAEnd = getEnd(secondNFA).changeToFinal(false);
	
		NFAEdge_v1 toFirstNFA = NFAEdge_v1.EMPTY_TRANSITION(newStart, firstNFA);
		NFAEdge_v1 toSecondNFA = NFAEdge_v1.EMPTY_TRANSITION(newStart, secondNFA);
		NFAEdge_v1 firstNFAtoNewEnd = NFAEdge_v1.EMPTY_TRANSITION(firstNFAEnd, newEnd);
		NFAEdge_v1 secondNFAtoNewEnd = NFAEdge_v1.EMPTY_TRANSITION(secondNFAEnd, newEnd);
		
		newStart.add(toFirstNFA);
		newStart.add(toSecondNFA);
		firstNFAEnd.add(firstNFAtoNewEnd);
		secondNFAEnd.add(secondNFAtoNewEnd);
		
		return newStart;
	}
			
	/*
	 *  X *
	 */
	private void constructClosure(Deque<State<NFAEdge_v1>> nfas) {
		State<NFAEdge_v1> mostRecent = nfas.removeLast();
		State<NFAEdge_v1> closuredRecent = addClosure(mostRecent);
		nfas.addLast(closuredRecent);
	}
	
	private void constructQuestionMark(Deque<State<NFAEdge_v1>> nfas) {
		State<NFAEdge_v1> mostRecent = nfas.removeLast();
		State<NFAEdge_v1> closuredRecent = addQuestionMark(mostRecent);
		nfas.addLast(closuredRecent);
	}
	
	private void constructPlus(Deque<State<NFAEdge_v1>> nfas) {
		State<NFAEdge_v1> mostRecent = nfas.removeLast();
		State<NFAEdge_v1> closuredRecent = addPlus(mostRecent);
		nfas.addLast(closuredRecent);
	}
	
	private State<NFAEdge_v1> addQuestionMark(State<NFAEdge_v1> nfa) {
		State<NFAEdge_v1> nfaEnd = getEnd(nfa).changeToFinal(false);
		NFAState_v1 newStart = createState(false);
		NFAState_v1 newEnd = createState(true);
		
		NFAEdge_v1 startToNFA = NFAEdge_v1.EMPTY_TRANSITION(newStart, nfa); // new start -> start of nfa
		NFAEdge_v1 startToEnd = NFAEdge_v1.EMPTY_TRANSITION(newStart, newEnd); // new start -> new end
		NFAEdge_v1 nfaToEnd = NFAEdge_v1.EMPTY_TRANSITION(nfaEnd, newEnd); // end of nfa -> new end
		
		newStart.add(startToNFA);
		newStart.add(startToEnd);
		nfaEnd.add(nfaToEnd);
		
		return newStart;
	}
	
	private State<NFAEdge_v1> addPlus(State<NFAEdge_v1> nfa) {
		State<NFAEdge_v1> nfaEnd = getEnd(nfa).changeToFinal(false);
		NFAState_v1 newEnd = createState(true);
		
		
		NFAEdge_v1 nfaToEnd = NFAEdge_v1.EMPTY_TRANSITION(nfaEnd, newEnd); // end of nfa -> new end
		NFAEdge_v1 nfaToStartNFA = NFAEdge_v1.EMPTY_TRANSITION(nfaEnd, nfa);
		
		nfaEnd.add(nfaToEnd);
		nfaEnd.add(nfaToStartNFA);
		
		return nfa;
	}
	
	private int constructParentheses(int index, Deque<State<NFAEdge_v1>> nfas, String input) throws IllegalStateException {
		int closingParenIdx = findClosing(index, L_PAREN, R_PAREN, input);
		if (closingParenIdx == -1) {
			throw new IllegalStateException("Parentheses are not closed properly");
		}
		
		String insideParen = input.substring(index +1, closingParenIdx);
		State<NFAEdge_v1> insideNFA = construct(0, insideParen.length(), insideParen, new LinkedList<>());
		nfas.add(insideNFA);
		return closingParenIdx - index;
	}
	

	private State<NFAEdge_v1> combineAll(Deque<State<NFAEdge_v1>> nfas) throws NoSuchElementException {
		while (nfas.size() > 1) {
			State<NFAEdge_v1> secondNFA = nfas.removeLast();
			State<NFAEdge_v1> firstNFA = nfas.removeLast();
			State<NFAEdge_v1> combined = combine(firstNFA, secondNFA);
			
			nfas.addLast(combined);
		}
		return nfas.remove();
	}
	
	/*
	 * Combines the end of the first NFA and removing the final state of it 
	 * to connect to start of the second NFA.
	 * 
	 * Adds an empty transition between the two
	 */
	private State<NFAEdge_v1> combine(State<NFAEdge_v1> firstNFA, State<NFAEdge_v1> secondNFA) {
		State<NFAEdge_v1> firstEndHalf = getEnd(firstNFA).changeToFinal(false);
		NFAEdge_v1 emptyTransitionEdge = NFAEdge_v1.EMPTY_TRANSITION(firstEndHalf, secondNFA);
		firstEndHalf.add(emptyTransitionEdge);
		return firstNFA;
	}
	
	//finish later
	private int constructSet(int index, Deque<State<NFAEdge_v1>> nfas, String input) {
		//int closingSetIdx = findClosing(index, L_SET, R_SET, input);

		return Integer.MIN_VALUE;
	}
	
	private void constructSingleCharNFA(char ch, Deque<State<NFAEdge_v1>> nfas) {
		nfas.add(singleCharNFA(ch));
	}
	
	/*
	 * Finds the index where the closing char is
	 * 
	 * returns -1 if not found or something the string is not formatted well.
	 * 
	 * If you want the substring, you will need to + 1 to th index
	 */
	protected static int findClosing(int startIndex, char open, char closing, String input) {
		int count = 0;
		int index = startIndex;
		
		//if count is ever less than 1, the input is incorrect
		while (index < input.length() && count >= 0) {
			char currCh = input.charAt(index);
			if (currCh == open) {
				count++;
			} else if (currCh == closing) {
				count--;
				if (count == 0) {
					break;
				}
			}
			index++;
		}
		
		return count == 0 ? index : -1;
	}
	
	private State<NFAEdge_v1> addClosure(State<NFAEdge_v1> nfa) {
		State<NFAEdge_v1> nfaEnd = getEnd(nfa).changeToFinal(false);
		NFAState_v1 newStart = createState(false);
		NFAState_v1 newEnd = createState(true);
		
		NFAEdge_v1 startToNFA = NFAEdge_v1.EMPTY_TRANSITION(newStart, nfa); // new start -> start of nfa
		NFAEdge_v1 startToEnd = NFAEdge_v1.EMPTY_TRANSITION(newStart, newEnd); // new start -> new end
		NFAEdge_v1 nfaToEnd = NFAEdge_v1.EMPTY_TRANSITION(nfaEnd, newEnd); // end of nfa -> new end
		NFAEdge_v1 nfaEndToNFA = NFAEdge_v1.EMPTY_TRANSITION(nfaEnd, nfa); // end of nfa -> start of nfa
		
		newStart.add(startToNFA);
		newStart.add(startToEnd);
		nfaEnd.add(nfaToEnd);
		nfaEnd.add(nfaEndToNFA);
		
		return newStart;
	}
	
	/*
	 * Creates an NFA with 2 states having a transition of the given chars
	 * 
	 * Ex:  S0 		--> 		S1
	 * 			transitions
	 */
	private NFAState_v1 create(Character[] transitions) {
		Set<Character> transitionSet = new HashSet<Character>(Arrays.asList(transitions));
		NFAState_v1 start = createState(false);
		NFAState_v1 end = createState(true);
		NFAEdge_v1 edge = new NFAEdge_v1(start, end, transitionSet);
		start.add(edge);
		return start;
	}
	
	private NFAState_v1 singleCharNFA(char ch) {
		return create(new Character[] {ch});
	}
	
	
	/*
	 * Assumes there is only 1 end state and there is a valid path from the first edge
	 * of each State
	 */
	private State<NFAEdge_v1> getEnd(State<NFAEdge_v1> start) {
		State<NFAEdge_v1> curr = start;
		while (!curr.getEdges().isEmpty()) {
			curr = curr.getEdges().get(0).next;
		}
		return curr;
	}

	public String toString() {
		return "Regular Expression to NFA converter";
	}
	
	
	/*
	 * BFS representation 
	 */
	@Override
	public void print(State<NFAEdge_v1> input) {
		Set<State<NFAEdge_v1>> seen = new HashSet<>();
		Queue<State<NFAEdge_v1>> nodes = new LinkedList<>();
		nodes.add(input);
		while (!nodes.isEmpty()) {
			State<NFAEdge_v1> node = nodes.remove();
			if (!seen.contains(node)) {
				System.out.println(node.toString());
				for (NFAEdge_v1 edge : node.getEdges()) {
					System.out.println("\t" + edge.toString());
					nodes.add(edge.getNext());
				}
				seen.add(node);
			}
		}	
	}
	
	/*
	 * Accepts chars that are only printable
	 */
	public static boolean isPrintable(char ch) {
		return ch > 31 && ch < 127;
	}
		
	public static void main(String[] args) {
		//String regex = "t(b(c|d))*|a|c*";
		String regex = "abc|bc|ad";
		//String regex = "a(b|c)*";
		NFAConverter nfaConverter = new NFAConverter();
		//NFAConverter nfaConv2 = new NFAConverter();
		State<NFAEdge_v1> nfa = nfaConverter.convert(regex);
		nfaConverter.print(nfa);
		//nfaConv2.print(nfaConv2.convert(regex));
	}
}
