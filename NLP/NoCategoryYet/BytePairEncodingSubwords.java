package bpe;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//https://leimao.github.io/blog/Byte-Pair-Encoding/
//https://arxiv.org/pdf/2004.03720.pdf
// Good for no OOV
// ex: 1K, 2K, 5K, 10K and 20K
// RNN with gated switch, single layer GRU NLM built upon subword units learned from BPE
/*
 * a learning rate of 0.1, dropout of
0.5 [80] and a maximum of 50 epochs of stochastic gradient descent with a minibatch size of 32 (for the small training sets) or 64
(for the full training sets)

predict subwords
Autocompletion algorithms present a ranked list of k predicted tokens rather than a single best prediction

beam search algorithm
 */
public class BytePairEncodingSubwords {
	private static Map<String, Integer> getBasicTestVocab() {
		Map<String, Integer> dict = new HashMap<>();
		dict.put("l o w </w>", 5);
		dict.put("l o w e r </w>", 2);
		dict.put("n e w e s t </w>", 6);
		dict.put("w i d e s t </w>", 3);
		return dict;
	}

	private static Map<Collection<String>, Integer> getPairFreq(Map<String, Integer> dictionary) {
		Map<Collection<String>, Integer> pairsFreqs = new HashMap<>();
		for (Entry<String, Integer> wordFreq : dictionary.entrySet()) {
			String[] subwords = wordFreq.getKey().split(" ");
			int freq = wordFreq.getValue();
			for (int index = 0; index < subwords.length - 1; index++) {
				List<String> pairOfSubwords = Arrays.asList(subwords[index], subwords[index + 1]);
				if (pairsFreqs.containsKey(pairOfSubwords)) {
					pairsFreqs.put(pairOfSubwords, pairsFreqs.get(pairOfSubwords) + freq);
				} else {
					pairsFreqs.put(pairOfSubwords, freq);
				}
			}
		}
		return pairsFreqs;
	}

	private static Collection<String> getMax(Map<Collection<String>, Integer> pairFreqs) {
		if (pairFreqs.isEmpty()) {
			return null;
		}

		Entry<Collection<String>, Integer> maxFreqPair = null;
		for (Entry<Collection<String>, Integer> pair : pairFreqs.entrySet()) {
			if (maxFreqPair == null || pair.getValue() > maxFreqPair.getValue()) {
				maxFreqPair = pair;
			}
		}
		return maxFreqPair.getKey();
	}

	private static Map<String, Integer> merge(Collection<String> replacementPair, Map<String, Integer> dict) {
		Map<String, Integer> updatedDict = new HashMap<>(dict.size());
		String[] replacementPairInfo = replacementPair.toArray(new String[2]);
		String replacementPattern = replacementPairInfo[0] + " " + replacementPairInfo[1];
		String replacementSubword = replacementPairInfo[0] + replacementPairInfo[1];
		for (Entry<String, Integer> dictWord : dict.entrySet()) {
			String word = dictWord.getKey();

			String replacedWord = word.replaceAll(replacementPattern, replacementSubword);
			updatedDict.put(replacedWord, dictWord.getValue());
		}
		return updatedDict;
	}

	private static Map<String, Integer> encode(Map<String, Integer> dict, int iterations) {
		while (iterations > 0) {
			Map<Collection<String>, Integer> pairings = getPairFreq(dict);
			Collection<String> bestPair = getMax(pairings);
			if (bestPair == null) {
				System.out.println("here");
				break;
			}
			dict = merge(bestPair, dict);
			iterations--;
		}
		return dict;
	}

	private static Map<String, Integer> decode(Map<String, Integer> dict) {
		Map<String, Integer> decodedDict = new HashMap<>(dict.size());
		for (Entry<String, Integer> dictWord : dict.entrySet()) {
			String word = dictWord.getKey();
			String noSpaceWord = word.replaceAll(" ", "");
			decodedDict.put(noSpaceWord, dictWord.getValue());
		}
		return decodedDict;
	}

	public static void main(String[] args) {
		Map<String, Integer> dict = getBasicTestVocab();
		System.out.println(dict);
		dict = encode(dict, 10);
		System.out.println(dict);
		dict = decode(dict);
		System.out.println(dict);
	}
}