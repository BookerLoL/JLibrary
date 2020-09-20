package main.nlp.ir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Tokenizer {
	private static final String DEFAULT_DELIMITER = " ";
	private static final String NEWLINE_DELIMITER = "\n";

	protected String delimiter;

	public Tokenizer() {
		this(DEFAULT_DELIMITER);
	}

	public Tokenizer(String delimiter) {
		this.delimiter = delimiter;
	}

	protected List<String> tokenizeLine(String input) {
		String[] tokens = input.split(delimiter);
		return Arrays.asList(tokens);
	}
	
	protected List<String> tokenizeParagraph(String paragraph) {
		String[] lines = paragraph.split(NEWLINE_DELIMITER);
		List<String> tokenizedLines = new LinkedList<>();
		for (String line : lines) {
			tokenizedLines.addAll(tokenizeLine(line));
		}
		return tokenizedLines;
	}

	protected List<String> tokenizeFile(String filename) throws IOException {
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<String> words = new LinkedList<>();
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] lineOfWords = line.split(delimiter);
			words.addAll(Arrays.asList(lineOfWords));
		}
		br.close();
		return words;
	}
}
