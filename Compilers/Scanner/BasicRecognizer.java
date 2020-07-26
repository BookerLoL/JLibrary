package Scanner;

import java.util.Collection;
import java.util.LinkedList;

/*
 * Takes a collection of words to recognize as valid
 * 
 * Closure-Free Regular Expression
 */
public class BasicRecognizer {
	int id = 0;
	State<NFAEdge_v1> startingState = new NFAState_v1(id, false);

	public BasicRecognizer(Collection<String> words) {
		addAll(words);
	}

	public void add(String word) {
		State<NFAEdge_v1> currState = startingState;
		State<NFAEdge_v1> prevState = null;
		int length = word.length();
		for (int i = 0; i < length; i++) {
			char transitionValue = word.charAt(i);
			prevState = currState;
			currState = currState.transition(transitionValue);
			if (currState == null) {
				id++;
				NFAEdge_v1 newPath = new NFAEdge_v1((NFAState_v1) prevState, new NFAState_v1(id, false), transitionValue);
				prevState.edges.add(newPath);
				currState = newPath.next;
			}

			if (i == length - 1) {
				currState.isFinal = true;
			}
		}
	}

	public void addAll(Collection<String> words) {
		words.stream().forEach(word -> add(word));
	}

	public void printAllPaths() {
		printAllPathsHelper(startingState);
	}

	/*
	 * DFS search to print the paths
	 */
	private void printAllPathsHelper(State<NFAEdge_v1> current) {
		System.out.println("Node name: " + current.name + " and is final? " + current.isFinal);

		if (current.edges.isEmpty()) {
			System.out.println("\tHas no edges");
		} else {
			for (NFAEdge_v1 edge : current.edges) {
				NFAState_v1 next = edge.next;
				System.out.println("Transition needs: " + edge.transitionChars + " from node: " + current.name);
				printAllPathsHelper(next);
			}
		}
	}

	public static void main(String[] args) {
		Collection<String> words = new LinkedList<>();
		words.add("eed");
		words.add("eed");
		words.add("eed");
		BasicRecognizer br = new BasicRecognizer(words);
		br.printAllPaths();
	}
}
