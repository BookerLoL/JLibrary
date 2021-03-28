package main.nlp.ir;

import java.io.IOException;
import java.util.*;

//This could be implemented better
public class IndexedDictionaryHandler {
	protected int numDocuments;
	protected int numWords;

	protected List<String> fileNames;
	protected List<String> collectedWords; // ordered set list
	protected List<List<String>> filesProcessedWords;

	private boolean isProcessed;

	public IndexedDictionaryHandler(List<String> fileNames) {
		this.fileNames = fileNames;
		numDocuments = fileNames.size();
		initDefault();
	}

	private void initDefault() {
		numWords = 0;
		collectedWords = new ArrayList<>();
		filesProcessedWords = new ArrayList<>(numDocuments);
		isProcessed = false;
	}

	public int getNumDoucments() {
		return numDocuments;
	}

	public int getNumWords() {
		return numWords;
	}

	public List<String> getFileNames() {
		return fileNames;
	}

	public List<String> getCollectedWords() {
		return collectedWords;
	}

	public int getRowOfWord(String word) {
		return collectedWords.indexOf(word);
	}

	public void processLongParagraphs(Tokenizer tokenizer, Normalizer normalizer, List<String> longParagraphs) {
		if (longParagraphs.size() == getNumDoucments()) {
			processTokensWithoutFiles(tokenizer, normalizer, longParagraphs);
			collectedWords = filterAndSortUniqueTokens(collectedWords);
			numWords = collectedWords.size();
			isProcessed = true;
		}
	}

	public void processFiles(Tokenizer tokenizer, Normalizer normalizer) throws IOException {
		if (!isProcessed) {
			processTokensWithFiles(tokenizer, normalizer);
			collectedWords = filterAndSortUniqueTokens(collectedWords);
			numWords = collectedWords.size();
			isProcessed = true;
		}
	}

	public IncidentMatrix processIndicentMatrix() {
		IncidentMatrix matrix = null;

		if (isProcessed) {
			matrix = new IncidentMatrix(getNumWords(), getNumDoucments());

			for (int wordIdx = 0; wordIdx < getNumWords(); wordIdx++) {
				String word = collectedWords.get(wordIdx);
				for (int docIdx = 0; docIdx < getNumDoucments(); docIdx++) {
					if (filesProcessedWords.get(docIdx).contains(word)) {
						matrix.set(wordIdx, docIdx, true);
					} else {
						matrix.set(wordIdx, docIdx, false);
					}
				}
			}
		}

		return matrix;
	}

	public void clearProcessedInfo() {
		initDefault();
	}

	private void processTokensWithoutFiles(Tokenizer tokenizer, Normalizer normalizer, List<String> pargraphs) {
		for (String paragraph : pargraphs) {
			List<String> documentTokens = tokenizer.tokenizeParagraph(paragraph);
			List<String> normalizedTokens = normalizer.normalize(documentTokens);
			collectedWords.addAll(normalizedTokens);
			filesProcessedWords.add(normalizedTokens);
		}
	}

	private void processTokensWithFiles(Tokenizer tokenizer, Normalizer normalizer) throws IOException {
		for (String documentName : fileNames) {
			List<String> documentTokens = tokenizer.tokenizeFile(documentName);
			List<String> normalizedTokens = normalizer.normalize(documentTokens);
			collectedWords.addAll(normalizedTokens);
			filesProcessedWords.add(normalizedTokens);
		}
	}

	private List<String> filterAndSortUniqueTokens(Collection<String> tokens) {
		// Filter out repeated tokens and sort the tokens
		Set<String> uniqueTokens = new HashSet<>(tokens);
		List<String> collectedTokens = new ArrayList<>(uniqueTokens);
		Collections.sort(collectedTokens);
		return collectedTokens;
	}
}
