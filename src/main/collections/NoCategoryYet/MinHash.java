package main.collections.NoCategoryYet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//b-bit minwise hashing can improve minhash
public class MinHash<T> {
	public interface Hasher {
		int hash(int item);
	}

	private int totalFeatures;
	private int totalDocuments;
	private int[][] binaryDocSet;
	private int[][] signatureMatrix;
	private Hasher[] hashers;

	@SafeVarargs
	public MinHash(Hasher[] hashers, Set<T>... sets) {
		build(hashers, sets);
	}

	@SuppressWarnings("unchecked")
	public void build(Hasher[] hashers, Set<T>... sets) {
		this.hashers = hashers;
		totalDocuments = sets.length;
		totalFeatures = getUniversalSetSize(sets);
		binaryDocSet = buildBinaryDocSet(sets);
		signatureMatrix = buildSignatureMatrix();
		binaryDocSet = null;
	}

	@SafeVarargs
	private int getUniversalSetSize(Set<T>... sets) {
		Set<T> universalSet = new HashSet<>();
		for (Set<T> set : sets) {
			universalSet.addAll(set);
		}
		return universalSet.size();
	}

	// Better to store in a sparse matrix
	@SafeVarargs
	private int[][] buildBinaryDocSet(Set<T>... sets) {
		int[][] binaryDocSet = new int[sets.length][totalFeatures()];

		for (int setIdx = 0; setIdx < sets.length; setIdx++) {
			for (T feature : sets[setIdx]) {
				binaryDocSet[setIdx][bound(feature.hashCode())] = 1;
			}
		}

		return binaryDocSet;
	}

	private int[][] buildSignatureMatrix() {
		int[][] sigMatrix = initSigMatrix();

		for (int feature = 0; feature < totalFeatures(); feature++) {
			for (int document = 0; document < totalDocuments(); document++) {
				if (binaryDocSet[document][feature] != 0) {
					computeSigHashes(sigMatrix, document, feature);
				}
			}
		}

		return sigMatrix;
	}

	private int[][] initSigMatrix() {
		int[][] sigMatrix = new int[totalHashFunctions()][totalDocuments()];
		for (int[] vector : sigMatrix) {
			Arrays.fill(vector, Integer.MAX_VALUE);
		}
		return sigMatrix;
	}

	private void computeSigHashes(int[][] sigMatrix, int document, int feature) {
		for (int hasher = 0; hasher < totalHashFunctions(); hasher++) {
			sigMatrix[hasher][document] = Math.min(sigMatrix[hasher][document], bound(hashers[hasher].hash(feature)));
		}
	}

	public double similarity(int docIndex1, int docIndex2) {
		int similar = 0;
		for (int hasher = 0; hasher < totalHashFunctions(); hasher++) {
			if (signatureMatrix[hasher][docIndex1] == signatureMatrix[hasher][docIndex2]) {
				similar++;
			}
		}

		return (double) similar / totalHashFunctions();
	}

	private int bound(int hash) {
		return hash % totalFeatures();
	}

	public int totalFeatures() {
		return totalFeatures;
	}

	public int totalDocuments() {
		return totalDocuments;
	}

	public int totalHashFunctions() {
		return hashers.length;
	}

	public static void main(String[] arsgs) {
		Hasher h1 = num -> (22 * num + 5) % 31;
		Hasher h2 = num -> (30 * num + 2) % 31;
		Hasher h3 = num -> (21 * num + 23) % 31;
		Hasher h4 = num -> (15 * num + 6) % 31;
		Hasher[] hashers = { h1, h2, h3, h4 };

		Set<Integer> doc1 = new HashSet<>(Arrays.asList(8, 15, 16));
		Set<Integer> doc2 = new HashSet<>(Arrays.asList(1, 5, 7, 10, 11, 15, 16, 17));
		Set<Integer> doc3 = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 7, 9, 12, 17));
		Set<Integer> doc4 = new HashSet<>(Arrays.asList(1, 5, 6, 13, 14, 15));
		Set<Integer> doc5 = new HashSet<>(Arrays.asList(1, 2, 5, 9, 13, 15, 18));

		MinHash<Integer> minhash = new MinHash<>(hashers, doc1, doc2, doc3, doc4, doc5);
		System.out.println(minhash.similarity(1, 2));
		System.out.println(minhash.similarity(3, 4));
		System.out.println(minhash.similarity(0, 2));
	}
}
