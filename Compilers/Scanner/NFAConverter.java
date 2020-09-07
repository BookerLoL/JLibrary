package scanner;

import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/*
 * Takes in regular expressions
 * 
 * Based on Thompson's construction to translate Regular Expression to NFA 	
 * 
 * Currently doesn't handle special characters \[
 * 
 * Operation hierachy (Highest priority at the top)
 * ( )  (ex: (...) ), parentheses; 
 * [ ]  (ex: [a-z]), set bracket
 *   *  (ex: a*), closure
 *   ?  (ex: a?), optional 
 *   +  (ex: a+), kleene plus
 *   |  (ex: a | b), alternation
 *  ab  (ex: ab) , concantenation 
 *   	
 */

/*
 * TO-DO LIST
 * finish  set bracket [a-z]
 */
public class NFAConverter extends Converter<String, State> {
	private static final char L_PAREN = '(';
	private static final char R_PAREN = ')';
	private static final char ALTERATION = '|';
	private static final char CLOSURE = '*';
	private static final char L_SET = '[';
	private static final char R_SET = ']';
	private static final char PLUS = '+';
	private static final char QUESTION_MARK = '?';

	public State convert(String input) {
		resetID(0);
		return construct(0, input.length(), input, new LinkedList<>());
	}

	private State construct(int fromIndex, int toIndex, String input, Deque<State> nfas) {
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
					index += constructSet(index, nfas, input);
				} else if (ch == PLUS) {
					constructPlus(nfas);
				} else if (ch == QUESTION_MARK) {
					constructQuestionMark(nfas);
				}
			} else {
				constructSingleCharNFA(ch, nfas);
			}
			index++;
		}
		return combineAll(nfas);
	}

	private boolean isOperator(char ch) {
		switch (ch) {
		case ALTERATION:
		case CLOSURE:
		case L_PAREN:
		case R_PAREN:
		case L_SET:
		case R_SET:
		case PLUS:
		case QUESTION_MARK:
			return true;
		default:
			return false;
		}
	}

	private int constructParentheses(int index, Deque<State> nfas, String input) throws IllegalStateException {
		int closingParenIdx = findClosing(index, L_PAREN, R_PAREN, input);
		String insideParen = input.substring(index + 1, closingParenIdx);
		State insideNFA = construct(0, insideParen.length(), insideParen, new LinkedList<>());
		nfas.add(insideNFA);
		return closingParenIdx - index;
	}

	private int constructAlternation(int startIndex, Deque<State> nfas, String input) {
		String afterAlternation = input.substring(startIndex + 1);
		State rightPart = construct(0, afterAlternation.length(), afterAlternation, new LinkedList<>());
		State leftPart = combineAll(nfas);
		State alternatedNFA = addAlternation(leftPart, rightPart);
		nfas.add(alternatedNFA);
		return afterAlternation.length();
	}

	private State addAlternation(State firstNFA, State secondNFA) {
		State newStart = StateFactory.create(Type.NFA, id++, false);
		State newEnd = StateFactory.create(Type.NFA, id++, true);
		State firstNFAEnd = getEnd(firstNFA).setIsFinal(false);
		State secondNFAEnd = getEnd(secondNFA).setIsFinal(false);

		Edge toFirstNFA = EdgeFactory.EMPTY_TRANSITION(newStart, firstNFA);
		Edge toSecondNFA = EdgeFactory.EMPTY_TRANSITION(newStart, secondNFA);
		Edge firstNFAtoNewEnd = EdgeFactory.EMPTY_TRANSITION(firstNFAEnd, newEnd);
		Edge secondNFAtoNewEnd = EdgeFactory.EMPTY_TRANSITION(secondNFAEnd, newEnd);

		newStart.addEdge(toFirstNFA);
		newStart.addEdge(toSecondNFA);
		firstNFAEnd.addEdge(firstNFAtoNewEnd);
		secondNFAEnd.addEdge(secondNFAtoNewEnd);

		return newStart;
	}

	private void constructClosure(Deque<State> nfas) {
		State mostRecent = nfas.removeLast();
		State closuredRecent = addClosure(mostRecent);
		nfas.addLast(closuredRecent);
	}

	private void constructQuestionMark(Deque<State> nfas) {
		State mostRecent = nfas.removeLast();
		State closuredRecent = addQuestionMark(mostRecent);
		nfas.addLast(closuredRecent);
	}

	private void constructPlus(Deque<State> nfas) {
		State mostRecent = nfas.removeLast();
		State closuredRecent = addPlus(mostRecent);
		nfas.addLast(closuredRecent);
	}

	private State addQuestionMark(State nfa) {
		State nfaEnd = getEnd(nfa).setIsFinal(false);
		State newStart = StateFactory.create(Type.NFA, id++, false);
		State newEnd = StateFactory.create(Type.NFA, id++, true);

		Edge startToNFA = EdgeFactory.EMPTY_TRANSITION(newStart, nfa);
		Edge startToEnd = EdgeFactory.EMPTY_TRANSITION(newStart, newEnd);
		Edge nfaToEnd = EdgeFactory.EMPTY_TRANSITION(nfaEnd, newEnd);

		newStart.addEdge(startToNFA);
		newStart.addEdge(startToEnd);
		nfaEnd.addEdge(nfaToEnd);
		return newStart;
	}

	private State addPlus(State nfa) {
		State nfaEnd = getEnd(nfa).setIsFinal(false);
		State newEnd = StateFactory.create(Type.NFA, id++, true);

		Edge nfaToEnd = EdgeFactory.EMPTY_TRANSITION(nfaEnd, newEnd);
		Edge nfaToStartNFA = EdgeFactory.EMPTY_TRANSITION(nfaEnd, nfa);

		nfaEnd.addEdge(nfaToEnd);
		nfaEnd.addEdge(nfaToStartNFA);
		return nfa;
	}

	private State combineAll(Deque<State> nfas) throws NoSuchElementException {
		while (nfas.size() > 1) {
			State secondNFA = nfas.removeLast();
			State firstNFA = nfas.removeLast();
			nfas.addLast(combine(firstNFA, secondNFA));
		}
		return nfas.remove();
	}

	// Combines two nfs together, first will start, second will end
	private State combine(State firstNFA, State secondNFA) {
		State firstEndHalf = getEnd(firstNFA).setIsFinal(false);
		Edge emptyTransitionEdge = EdgeFactory.EMPTY_TRANSITION(firstEndHalf, secondNFA);
		firstEndHalf.addEdge(emptyTransitionEdge);
		return firstNFA;
	}

	private int constructSet(int index, Deque<State> nfas, String input) {
		// int closingSetIdx = findClosing(index, L_SET, R_SET, input);
		throw new UnsupportedOperationException("NEED TO IMPLEMENT LATER");
	}

	private void constructSingleCharNFA(char ch, Deque<State> nfas) {
		nfas.add(create(ch));
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

		// if count is ever less than 1, the input is incorrect
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

		//if -1, couldn't find closing symbol
		return count == 0 ? index : -1;
	}

	private State addClosure(State nfa) {
		State nfaEnd = getEnd(nfa).setIsFinal(false);
		State newStart = StateFactory.create(Type.NFA, id++, false);
		State newEnd = StateFactory.create(Type.NFA, id++, true);

		Edge startToNFA = EdgeFactory.EMPTY_TRANSITION(newStart, nfa);
		Edge startToEnd = EdgeFactory.EMPTY_TRANSITION(newStart, newEnd);
		Edge nfaToEnd = EdgeFactory.EMPTY_TRANSITION(nfaEnd, newEnd);
		Edge nfaEndToNFA = EdgeFactory.EMPTY_TRANSITION(nfaEnd, nfa);

		newStart.addEdge(startToNFA);
		newStart.addEdge(startToEnd);
		nfaEnd.addEdge(nfaToEnd);
		nfaEnd.addEdge(nfaEndToNFA);

		return newStart;
	}

	private State create(Character... transitions) {
		State start = StateFactory.create(Type.NFA, id++, false);
		State end = StateFactory.create(Type.NFA, id++, true);
		Edge edge = EdgeFactory.create(Type.NFA, start, end, transitions);
		start.addEdge(edge);
		return start;
	}

	// NFA should be constructed to only have 1 final state in the entire graph
	private State getEnd(State start) {
		State curr = start;
		while (!curr.getEdges().isEmpty()) {
			curr = curr.getEdges().get(0).getNext();
		}
		return curr;
	}

	public String toString() {
		return "Regular Expression to NFA";
	}
}
