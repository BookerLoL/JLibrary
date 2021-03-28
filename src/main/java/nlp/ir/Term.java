package main.nlp.ir;

public class Term {
	public String term;
	public int docID;

	public Term(String term, int docID) {
		this.term = term;
		this.docID = docID;
	}

	public String getTerm() {
		return term;
	}

	public int getDocID() {
		return docID;
	}
	
	public String toString() {
		return "Term: " + term + " docID: " + docID; 
	}
}
