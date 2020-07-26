package boolean_retrieval;

import java.util.LinkedList;
import java.util.List;

public class BooleanMatrixTester {

	public static void main(String[] args) {
		//This tests the indexed boolean matrix 
		List<String> pgs = getLongParagraphs(2);
		IndexedQueryHandler qh = new IndexedQueryHandler(pgs);
		System.out.println(qh.query("for & ! ( drug | approach ) "));
	}

	protected static List<String> getLongParagraphs(int paragraph) {
		List<String> paragraphs = new LinkedList<>();
		if (paragraph == 1) {
			paragraphs.add(doc1());
			paragraphs.add(doc2());
			paragraphs.add(doc3());
			paragraphs.add(doc4());
		} else if (paragraph == 2) {
			paragraphs.add(doc5());
			paragraphs.add(doc6());
			paragraphs.add(doc7());
			paragraphs.add(doc8());
		}
		
		return paragraphs;
	}

	private static String doc1() {
		return "new home sales top forecasts";
	}

	private static String doc2() {
		return "home sales rise in july";
	}

	private static String doc3() {
		return "increase in home sales in july";
	}

	private static String doc4() {
		return "july new home sales rise";
	}

	private static String doc5() {
		return "breakthrough drug for schizophrenia";
	}

	private static String doc6() {
		return "new schizophrenia drug";
	}

	private static String doc7() {
		return "new approach for treatment of schizophrenia";
	}

	private static String doc8() {
		return "new hopes for schizophrenia patients";
	}
}
