import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/*
 * Basic example of a UnigramModel
 * 
 * Could cache the probabalities then update them when needed to avoid recomputing for testing
 * 
 * Ignore word order due to proabability
 * 
 * https://github.com/neubig/nlptutorial/blob/master/download/01-unigramlm/nlp-programming-en-01-unigramlm.pdf
 */
public class BigramModel {
	private String prefixTag;
	private String suffixTag;
	Map<String, Integer> countMap;
	private long vocabSize;
	private long totalCount;
	private double unknownWeight;
	private double knownWeight;

	public BigramModel() {
		this(0.95, "<w>", "</w>");
	}

	public BigramModel(String prefixTag, String suffixTag) {
		this(0.95, prefixTag, suffixTag);
	}

	public BigramModel(double knownWeight, String prefixTag, String suffixTag) {
		this.knownWeight = knownWeight;
		this.unknownWeight = 1.0 - knownWeight;
		this.prefixTag = prefixTag;
		this.suffixTag = suffixTag;
		countMap = new HashMap<>();
	}

	public void trainSentence(String sentence) {
		sentence = (prefixTag + " " + sentence + " " + suffixTag).trim();
		System.out.println(sentence + "\t" + Arrays.toString(sentence.split(" ")));
		for (String word : sentence.split(" ")) {
			addWord(word);
		}
	}

	public double sentenceProbability(String sentence) {
		if (hasNoTrainedData()) {
			return 0.0d;
		}

		double probability = 1;
		for (String word : sentence.split(" ")) {
			probability *= wordProbability(word);
		}
		return probability;
	}

	public double wordProbability(String word) {
		if (hasNoTrainedData()) {
			return 0.0d;
		}

		double wordCountProbability = (double) getWordCount(word) / totalCount;

		// known * probability + unknown * (1 / vocabSize)
		return (knownWeight * wordCountProbability) + (unknownWeight * (1.0 / vocabSize));
	}

	public boolean hasNoTrainedData() {
		return totalCount == 0;
	}

	private void addWord(String word) {
		if (countMap.containsKey(word)) {
			countMap.put(word, countMap.get(word) + 1);
		} else {
			countMap.put(word, 1);
		}
		totalCount++;
	}

	public int getWordCount(String word) {
		return countMap.containsKey(word) ? countMap.get(word) : 0;
	}

	public void setVocabSize(long vocabSize) {
		this.vocabSize = vocabSize;
	}
	
	public double maximumLikelihoodEstimation(String word, String given) {
		//P(x | b) = c(b x)/c(b) 
		return 0.0;
	}
	
	//context dependent smoothing
	//

	public double likelihood(String sequenceOfWords) {
		if (hasNoTrainedData()) {
			return 0.0d;
		}

		double probability = 0.0d;
		for (String word : sequenceOfWords.split(" ")) {
			probability += Math.log(wordProbability(word));
		}
		return probability;
	}

	//average negative log2 likelihood per word
	public double entropy(String[] corpusLines) {
		if (corpusLines.length == 0) {
			return 0.0d;
		}

		int totalWords = 0;
		double entropy = 0.0d;
		for (String line : corpusLines) {
			line += " " + suffixTag;
			String[] words = line.split(" ");
			for (String word : words) {
				entropy += -Log2(wordProbability(word));
			}
			totalWords += words.length;
		}

		return entropy / totalWords;
	}
	
	public static double Log2(double number) {
		return Math.log(number) / Math.log(2.0);
	}
	
	//includes sentence final 
	public double coverage(String[] corpusLines) {
		if (corpusLines.length == 0) {
			return 0.0d;
		}

		int containedWords = 0;
		int totalWords = 0;
		for (String line : corpusLines) {
			line += " " + suffixTag;
			String[] words = line.split(" ");
			for (String word : words) {
				if (countMap.containsKey(word)) {
					containedWords++;
				}
			}
			totalWords += words.length;
		}

		return containedWords / (double)totalWords;
	}
	
	

	public static void main(String[] args) {
		BigramModel um = new BigramModel("", "</s>");
		String[] trainData = { "a b c", "a b d" };
		Arrays.asList(trainData).forEach(sentence -> um.trainSentence(sentence));
		um.setVocabSize(1000000);

		System.out.println(um.entropy(new String[] { "a c", "e" }));
		System.out.println(um.coverage(new String[] { "a c", "e" }));
	}
}
