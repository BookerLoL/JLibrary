package main.nlp.needtosort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*
 * Obviously there are better ways to implement an NGramModel 
 * but I decided to go with something simplistic to use 
 * 
 * I will revisit and make a better implementation using different data structures to improve performance.
 */
public class NGramModel {
	NGram ngrammer;
	Map<String[], Integer> freqCount;
	Set<String> vocab;
	long totalFreqCount;

	public NGramModel(int ngram, String prefixTag, String suffixTag) {
		ngrammer = new NGram(ngram, prefixTag, suffixTag);
		freqCount = new HashMap<>();
		vocab = new HashSet<>();
	}

	public void trainSentence(String sentence) {
		List<String[]> ngrams = ngrammer.generateAList(sentence);
		totalFreqCount += ngrams.size();

		ngrams.forEach(seq -> {
			add(seq);
			Arrays.asList(seq).forEach(word -> vocab.add(word));
		});
	}

	private void add(String[] seq) {
		if (freqCount.containsKey(seq)) {
			freqCount.put(seq, freqCount.get(seq) + 1);
		} else {
			freqCount.put(seq, 1);
		}
	}

	private int[] getCountAndSubCount(String wordSeq) {
		String[] words = wordSeq.split(" ");

		int totalCount = 0;
		int directMatch = 0;
		int nGramLength = ngrammer.getNGramLength();
		for (Entry<String[], Integer> entry : freqCount.entrySet()) {
			String[] seq = entry.getKey();
			int countFreq = entry.getValue();
			
			for (int i = 0; i < seq.length; i++) {
				if (seq[i].equals(words[i])) {
					if (i == nGramLength - 2) {
						totalCount += countFreq;
					} else if (i == nGramLength - 1) {
						directMatch += countFreq;
					}
				} else {
					break;
				}
			}
		}

		return new int[] { directMatch, totalCount };
	}

	//MLE estimate
	public double relativeFrequency(String wordSeq) {
		if (ngrammer.getNGramLength() == 1) {
			return freqCount.getOrDefault(wordSeq, 0) / totalFreqCount;
		}
		int[] countInfo = getCountAndSubCount(wordSeq);
		return countInfo[0] / (double) countInfo[1];
	}

	// probability ^ (-1/nGramLength)
	public double perplexity(String wordSeq) {
		return Math.pow(relativeFrequency(wordSeq), -1.0 / (ngrammer.getNGramLength()));
	}

	// add-one smoothing
	public double laplaceSmoothing(String wordSeq) {
		return lidstoneLawSmoothing(wordSeq, 1.0);
	}
	
	//Additive smoothing or AddKSmoothing
	// based on https://www.csd.uwo.ca/courses/CS4442b/L9-NLP-LangModels.pdf
	// Deltaweight should be between > 0 and < 1
	public double lidstoneLawSmoothing(String wordSeq, double deltaWeight) {
		if (ngrammer.getNGramLength() == 1) {
			return (getCount(wordSeq) + deltaWeight) / (double) (totalFreqCount + (deltaWeight * vocab.size()));
		}
		int[] countInfo = getCountAndSubCount(wordSeq);
		return (countInfo[0] + deltaWeight) / (countInfo[1] +  (deltaWeight * getAllPossibleNGramVocab(ngrammer.getNGramLength())));
	}
	
	// Bigram can have V * V possible ngram combinations, trigram is V * V * V...
	private long getAllPossibleNGramVocab(int ngram) {
		long possibleNGrams = 1;
		for (; ngram > 0; ngram--, possibleNGrams *= vocab.size())
			;
		return possibleNGrams;
	}
	
	//ELE, expected likelihood estimation
	public double jeffreysPerksSmoothing(String wordSeq) {
		return lidstoneLawSmoothing(wordSeq, 0.5);
	}
	
	public double wittenBellSmoothing(String wordSeq) {
		throw new UnsupportedOperationException();
	}
	
	//assert that knowWeight < 1 && unknownWeight < 1 && knownWeight + unknownWeight = 1.0
	public double linearInterpolation(String wordSeq, double knownWeight, double unknownWeight) {
		throw new UnsupportedOperationException();
	}
	
	public double entropy(String[] corpusLines) {
		throw new UnsupportedOperationException();
	}

	public int getCount(String wordSeq) {
		String[] words = wordSeq.split(" ");
		return freqCount.containsKey(words) ? freqCount.get(words) : 0;
	}

	public static void main(String[] args) {
		NGramModel m = new NGramModel(3, "<s>", "</s>");
		m.trainSentence("this is a test to see if this works");
		m.trainSentence("this is my dog . he is called henry this is a cat this is a dog");
		m.trainSentence("homework is very time consuming");
		System.out.println(m.perplexity("is a dog"));
	}
}
