package main.nlp.ir;

import java.util.*;

public class InvertedIndexMatrix {
	protected static class TermItem {
		protected String term;
		protected int frequency = 0;
		protected List<Integer> postings = new LinkedList<>();
		
		public TermItem(String term, int docID) {
			this.term = term;
			addPosting(docID);
		}

		// adds in order
		protected void addPosting(int docID) {
			if (postings.contains(docID)) {
				return;
			}

			int insertIndex = 0;
			while (insertIndex < postings.size() && postings.get(insertIndex) < docID) {
				insertIndex++;
			}

			postings.add(insertIndex, docID);
			frequency++;
		}

		protected int getPosting(int index) {
			if (index < 0 || index >= frequency) {
				return -1;
			}

			return postings.get(index);
		}

		public String getTerm() {
			return term;
		}

		public int getDocFrequency() {
			return frequency;
		}

		public String toString() {
			return "Term: " + getTerm() + " Doc Freq: " + getDocFrequency() + " postings: [" + postings.toString()
					+ "]";
		}
	}

	// Will be an ordered dictionary
	protected List<TermItem> dictionary;
	protected List<Integer> docIDs;

	public InvertedIndexMatrix() {
		dictionary = new ArrayList<>();
		docIDs =  new ArrayList<>();
	}

	public void addAll(List<Term> terms) {
		terms.stream().forEach(term -> add(term.getTerm(), term.getDocID()));
	}

	public void addAll(List<String> words, List<Integer> docIDs) {
		if (words.size() == docIDs.size()) {
			for (int i = 0; i < words.size(); i++) {
				add(words.get(i), docIDs.get(i));
			}
		}
	}
	
	private void addDocID(int docID) {
		if (!docIDs.contains(docID)) {
			docIDs.add(docID);
		}
	}
	
	public int getDocID(int index) {
		return docIDs.get(index);
	}
	
	public int totalDocIDs() {
		return docIDs.size();
	}

	public void add(String word, int docID) {
		if (contains(word)) {
			TermItem item = get(word);
			item.addPosting(docID);
			return;
		}

		//Could use iterative binary search
		char firstLetter = Character.toLowerCase(word.charAt(0));
		int insertIndex = -1;

		for (int i = 0; i < totalWords(); i++) {
			TermItem currItem = dictionary.get(i);
			String itemWord = currItem.getTerm();
			String itemWordLowercased = itemWord.toLowerCase();
			char itemFirstLetter = itemWordLowercased.charAt(0);

			if (itemFirstLetter < firstLetter) {
				continue;
			} else if (itemFirstLetter == firstLetter) {
				int comparison = itemWord.compareTo(word);
				if (comparison < 0) {
					continue;
				} else { //don't need to worry abotu checking if it's a match because that's already checked
					insertIndex = i;
					break;
				}
			} else {
				insertIndex = i;
				break;
			}
		}

		TermItem newItem = new TermItem(word, docID);
		insertIndex = insertIndex == -1 ? totalWords() : insertIndex;
		dictionary.add(insertIndex, newItem);
		addDocID(docID);
	}

	public int totalWords() {
		return dictionary.size();
	}

	public boolean contains(String word) {
		TermItem item = get(word);
		return item != null;
	}

	public int getDocFrequency(String word) {
		TermItem item = get(word);
		return item == null ? 0 : item.getDocFrequency();
	}

	private TermItem get(String word) {
		char firstLetter = word.toLowerCase().charAt(0);
		for (TermItem item : dictionary) {
			String term = item.getTerm();
			char termFirstLetter = term.toLowerCase().charAt(0);
			if (termFirstLetter < firstLetter) {
				continue;
			} else if (termFirstLetter == firstLetter) {
				if (term.equals(word)) {
					return item;
				}
			} else {
				break;
			}
		}
		return null;
	}

	public String toString() {
		String results = "Dictionary has " + totalWords() + " words\n";
		for (TermItem item : dictionary) {
			results += item.toString() + "\n";
		}
		return results;
	}
}
