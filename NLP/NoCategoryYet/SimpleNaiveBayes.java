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

	private Map<Integer, Document> documents;
	private Set<String> classifications;
	private Set<String> vocab;

	public SimpleNaiveBayes() {
		documents = new HashMap<>();
		classifications = new HashSet<>();
		vocab = new HashSet<>();
	}

	public void addDocument(int docID, String classification) {
		Document doc = new Document(docID, classification);
		documents.computeIfAbsent(doc.docID, k -> doc);
		classifications.add(classification);
	}

	public void addWordToDocument(int docID, List<String> words) {
		documents.computeIfPresent(docID, (k, v) -> {
			words.forEach(word -> {
				v.addWord(word);
				vocab.add(word);
			});
			return v;
		});
	}

	public void addWordToDocuement(int docID, String[] words) {
		addWordToDocument(docID, Arrays.asList(words));
	}

	public void addWordToDocument(int docID, String word) {
		documents.computeIfPresent(docID, (k, v) -> {
			v.addWord(word);
			vocab.add(word);
			return v;
		});
	}

	public String testClassify(List<String> words) {
		String bestClassification = "";
		double highestProbability = Integer.MIN_VALUE, probability = 1.0;
		long totalWordCountForClassification = 0, wordCount = 0;

		for (String classification : classifications) {
			List<Document> associatedClassDocs = documents.values().stream().filter(doc -> doc.classification.equals(classification)).collect(Collectors.toList());

			totalWordCountForClassification = associatedClassDocs.stream().mapToLong(doc -> doc.totalWords).sum();
			
			probability = 1.0;
			for (String word : words) {
				wordCount = associatedClassDocs.stream().mapToLong(doc -> doc.wordCounts.getOrDefault(word, 0)).sum();
				probability *= ((wordCount + 1) / ((double) totalWordCountForClassification + vocab.size())); 
			}

			double prior = associatedClassDocs.size() / (double) documents.size(); 
			probability *= prior;

			if (probability > highestProbability) {
				highestProbability = probability;
				bestClassification = classification;
			}
		}

		return bestClassification;
	}

	public static void main(String[] args) {
		SimpleNaiveBayes snb = new SimpleNaiveBayes();
		snb.addDocument(0, "c");
		snb.addDocument(1, "c");
		snb.addDocument(2, "c");
		snb.addDocument(3, "j");
		snb.addWordToDocuement(0, new String[] { "Chinese", "Beijing", "Chinese" });
		snb.addWordToDocuement(1, new String[] { "Chinese", "Chinese", "Shanghai" });
		snb.addWordToDocuement(2, new String[] { "Chinese", "Macao" });
		snb.addWordToDocuement(3, new String[] { "Tokyo", "Japan", "Chinese" });
		System.out.println(snb.testClassify(Arrays.asList("Chinese", "Chinese", "Chinese", "Tokyo", "Japan")));
	}
}
