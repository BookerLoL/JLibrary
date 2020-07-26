import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//Simulates a simple Naive Bayes model using relative freqeuency
public class SimpleNaiveBayes {
	private class Document {
		private int docID;
		private String classification;
		private Map<String, Integer> wordCounts;
		private long totalWords = 0;

		public Document(int docID, String classification) {
			this.docID = docID;
			this.classification = classification;
			wordCounts = new HashMap<>();
		}

		public void addWord(String word) {
			wordCounts.compute(word, (k, v) -> v != null ? v + 1 : 1);
			totalWords++;
		}
	}

	// Not needed to do this but decided to make it quick
	private Map<Integer, Document> documents;
	private Set<String> classifications;
	private Set<String> vocab;

	public SimpleNaiveBayes() {
		documents = new HashMap<>();
		classifications = new HashSet<>();
		vocab = new HashSet<>();
	}

	public Document createDocument(int docID, String classification) {
		return new Document(docID, classification);
	}

	public void addDocument(int docID, String classification) {
		Document doc = new Document(docID, classification);
		documents.computeIfAbsent(doc.docID, k -> doc);
		classifications.add(classification);
	}

	public void addWordToDocument(int docID, List<String> words) {
		Document doc = documents.get(docID);
		if (doc != null) {
			words.forEach(word -> {
				doc.addWord(word);
				vocab.add(word);
			});
		}
	}

	public void addWordToDocuement(int docID, String[] words) {
		addWordToDocument(docID, Arrays.asList(words));
	}

	public void addWordToDocument(int docID, String word) {
		Document doc = documents.get(docID);
		if (doc != null) {
			doc.addWord(word);
			vocab.add(word);
		}
	}

	public String testClassify(List<String> words) {
		int totalClasses = classifications.size();

		String bestClassification = "";
		double highestProbability = Integer.MIN_VALUE;

		for (String classification : classifications) {
			List<Document> associatedClassDocs = documents.values().stream()
					.filter(doc -> doc.classification.equals(classification)).collect(Collectors.toList());
			double prior = associatedClassDocs.size() / (double) totalClasses; // P(c)

			int totalWordCountForClassification = 0; // Count(c)
			for (Document doc : associatedClassDocs) {
				totalWordCountForClassification += doc.totalWords;
			}

			double probability = 1.0;
			int wordCount = 0; // count(w, c)
			for (String word : words) {
				wordCount = 0;
				for (Document doc : associatedClassDocs) {
					wordCount += doc.wordCounts.getOrDefault(word, 0);
				}
				probability *= ((wordCount + 1) / (totalWordCountForClassification + vocab.size())); // P(Word, Class)
			}
			
			probability *= prior; 

			if (probability > highestProbability) {
				highestProbability = probability;
				bestClassification = classification;
			}
		}

		return bestClassification;
	}
}
