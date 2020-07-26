package boolean_retrieval;

import java.util.*;

public class InvertedIndexProcessor {
	List<Integer> docsProcessed = new LinkedList<>();
	List<Term> terms = new LinkedList<>();
	private boolean isSorted = false;

	public void clear() {
		terms = new LinkedList<>();
		docsProcessed = new LinkedList<>();
		isSorted = false;
	}

	public void process(List<String> tokens, int docID) {
		if (!docsProcessed.contains(docID)) {
			tokens.stream().forEach(token -> terms.add(new Term(token, docID)));
			isSorted = false;
		}
	}

	// sort aphebetically (order ex: a > I > i > Z) then by docID (greatest to
	// least)
	private void sortTerms() {
		Comparator<Term> comp = (o1, o2) -> {
			String firstTerm = o1.getTerm();
			String firstTermLower = firstTerm.toLowerCase();

			String secondTerm = o2.getTerm();
			String secondTermLower = secondTerm.toLowerCase();

			int comp1 = firstTermLower.compareTo(secondTermLower);
			if (comp1 != 0) {
				return comp1;
			}

			// want higher doc ID's to go first
			return o2.getDocID() - o1.getDocID();
		};

		Collections.sort(terms, comp);
	}

	public InvertedIndexMatrix create() {
		if (!isSorted) {
			sortTerms();
		}
		InvertedIndexMatrix matrix = new InvertedIndexMatrix();
		matrix.addAll(terms);
		return matrix;
	}
}