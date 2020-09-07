package scanner;

import java.util.Arrays;
import java.util.Collection;

//Behaves similar to a trie
public class BasicRecognizer {
	private int id = 0;
	private State startingState = StateFactory.create(Type.NFA, id, false);

	public void add(String word) {
		State currState = startingState, prevState = null;
		for (int charIdx = 0; charIdx < word.length(); charIdx++) {
			char transitionValue = word.charAt(charIdx);
			prevState = currState;
			currState = currState.transition(transitionValue);
			if (currState == null) {
				currState = StateFactory.create(Type.NFA, ++id, false);
				Edge transition = EdgeFactory.create(Type.NFA, prevState, currState, transitionValue);
				prevState.addEdge(transition);
			}
		}
		currState.setIsFinal(true);
	}

	public void addAll(Collection<String> words) {
		words.forEach(this::add);
	}

	public void addAll(String... words) {
		addAll(Arrays.asList(words));
	}

	public void printAllPaths() {
		printAllPathsHelper(startingState);
	}

	// DFS search to print each state
	private void printAllPathsHelper(State current) {
		System.out.println("Node name: " + current.getName() + " and is final: " + current.isFinal());

		for (Edge edge : current.getEdges()) {
			State nextState = edge.getNext();
			System.out.println("Transition needs: " + edge.getTransitions() + " from node: " + current.getName());
			printAllPathsHelper(nextState);
		}
	}

	public void printAllWords() {
		printAllWordsHelper(startingState, "");
	}

	private void printAllWordsHelper(State current, String result) {
		if (current.isFinal()) {
			System.out.println(result);
		}

		for (Edge edge : current.getEdges()) {
			State nextState = edge.getNext();
			printAllWordsHelper(nextState, result + (char) edge.getTransitions().toArray()[0]);
		}
	}

	public static void main(String[] args) {
		BasicRecognizer br = new BasicRecognizer();

		br.addAll("abstract", "assert", "continue", "for", "new", "switch", "default", "goto", "package", "sychronized",
				"boolean", "do", "if", "private", "this", "break", "double", "implements", "protected", "throw", "byte",
				"else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch",
				"extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", "finally",
				"long", "strictfp", "volatile", "const", "float", "native", "super", "while");
		br.printAllWords();
	}
}
