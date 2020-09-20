package main.nlp.ir;

import java.io.*;
import java.util.*;

public class IndexedQueryHandler {
	protected IncidentMatrix incidenceMatrix;
	protected IndexedDictionaryHandler dict;

	protected static final int NOT_FOUND = -1;

	private static final char ZERO = '0';
	private static final char ONE = '1';

	private static final String NOT_OP = "!";
	private static final String OR_OP = "|";
	private static final String AND_OP = "&";
	
	private static final String OPEN_PAREN_MARK = "(";
	private static final String CLOSE_PAREN_MARK = ")";

	private static final String IGNORE = "";

	public IndexedQueryHandler(List<String> longParagraphs) {
		Tokenizer tokenizer = new Tokenizer();
		Normalizer normalizer = new Normalizer();
		List<String> paragraphNames = createParagraphIds(longParagraphs.size());

		dict = new IndexedDictionaryHandler(paragraphNames);
		dict.processLongParagraphs(tokenizer, normalizer, longParagraphs);
		incidenceMatrix = dict.processIndicentMatrix();
	}

	private List<String> createParagraphIds(int numberOfParagraphs) {
		List<String> paragraphIds = new ArrayList<>(numberOfParagraphs);
		for (int id = 0; id < numberOfParagraphs; id++) {
			paragraphIds.add("Paragraph " + id);
		}
		return paragraphIds;
	}

	public IndexedQueryHandler(Tokenizer tokenizer, Normalizer normalizer, List<String> fileNames) throws IOException {
		dict = new IndexedDictionaryHandler(fileNames);
		dict.processFiles(tokenizer, normalizer);
		incidenceMatrix = dict.processIndicentMatrix();
	}

	// handles &, ||, !
	// ! word & word || ! word
	public String query(String request) {
		String[] wordsAndOps = request.split(" ");
		
		Stack<String> ops = new Stack<>();
		Stack<String> encodings = new Stack<>();
		
		for (int i = 0; i < wordsAndOps.length; i++) {
			if (wordsAndOps[i].equals(IGNORE)) {
				continue;
			} else if (wordsAndOps[i].equals(OPEN_PAREN_MARK)) {
				ops.push(wordsAndOps[i]);
			} else if (wordsAndOps[i].equals(CLOSE_PAREN_MARK)) {
				while (!ops.peek().equals(OPEN_PAREN_MARK)) {
					if (isUnaryOp(ops.peek())) {
						encodings.push(applyOp(ops.pop(), encodings.pop(), IGNORE));
					} else {
						encodings.push(applyOp(ops.pop(), encodings.pop(), encodings.pop()));
					}
				}
				ops.pop(); // ( 
			} else if (!isOperation(wordsAndOps[i])) {
				encodings.push(getEncoding(wordsAndOps[i]));
			} else {
				while (!ops.isEmpty() && hasPrecedence(wordsAndOps[i], ops.peek())) {
					if (isUnaryOp(ops.peek())) {
						encodings.push(applyOp(ops.pop(), encodings.pop(), IGNORE));
					} else {
						encodings.push(applyOp(ops.pop(), encodings.pop(), encodings.pop()));
					}
				}
				ops.push(wordsAndOps[i]);
			}
		}
		
		while (!ops.isEmpty()) {
			String op = ops.pop();
			if (isUnaryOp(op)) {
				encodings.push(applyOp(op, encodings.pop(), IGNORE));
			} else {
				encodings.push(applyOp(op, encodings.pop(), encodings.pop()));
			}
		}
		
		return encodings.pop();
	}
	
	/*
	 * Precedence Table
	 * 
	 * 1) (,  )
	 * 2) !
	 * 3) |, & 
	 *
	 */
	
	//Returns true if op2 has greater or equal precedence than op1
	private boolean hasPrecedence(String op1, String op2) {
		if (op2.equals(OPEN_PAREN_MARK) || op2.equals(CLOSE_PAREN_MARK)) {
			return false;
		} else if (op1.equals(NOT_OP) && (isBinaryOp(op2))) {
			return false;
		} else {
			return true;
		}
	}
	
	private String applyOp(String op, String encoding1, String encoding2) {
		if (isUnaryOp(op)) {
			if (op.equals(NOT_OP)) {
				return not(encoding1);
			}
		} else { //binary op 
			if (op.equals(OR_OP)) {
				return or(encoding1, encoding2);
			} else if (op.equals(AND_OP)) {
				return and(encoding1, encoding2);
			} 
		}
		return IGNORE;
	}
	
	private boolean isOperation(String input) {
		return isUnaryOp(input) || isBinaryOp(input);
	}
	private boolean isBinaryOp(String input) {
		return input.equals(OR_OP) || input.equals(AND_OP);
	}

	private boolean isUnaryOp(String input) {
		return input.equals(NOT_OP);
	}

	protected String getEncoding(String word) {
		return getEncoding(IGNORE, word);
	}

	protected String getEncoding(String prefixOp, String word) {
		int index = dict.getRowOfWord(word);

		if (index == NOT_FOUND) {
			return generateSpecialEncoding(ZERO, dict.getNumDoucments());
		}
		
		return incidenceEncoding(index);
	}

	protected String generateSpecialEncoding(char val, int repeat) {
		StringBuilder sb = new StringBuilder(repeat);
		for (int i = 0; i < repeat; i++) {
			sb.append(val);
		}
		return sb.toString();
	}

	private String incidenceEncoding(int row) {
		System.out.println(incidenceMatrix.getNumRows());
		boolean[] incidentRow = incidenceMatrix.get(row);
		StringBuilder sb = new StringBuilder(dict.getNumDoucments());
		for (boolean incident : incidentRow) {
			if (incident) {
				sb.append(ONE);
			} else {
				sb.append(ZERO);
			}
		}
		return sb.toString();
	}

	private String and(String result1, String result2) {
		StringBuilder sb = new StringBuilder(result1.length());
		for (int i = 0; i < result1.length(); i++) {
			char ch1 = result1.charAt(i);
			char ch2 = result2.charAt(i);

			if (ch1 == ch2 && ch1 == ONE) {
				sb.append(ONE);
			} else {
				sb.append(ZERO);
			}
		}
		return sb.toString();
	}

	private String or(String result1, String result2) {
		StringBuilder sb = new StringBuilder(result1.length());
		for (int i = 0; i < result1.length(); i++) {
			char ch1 = result1.charAt(i);
			char ch2 = result2.charAt(i);

			if (ch1 == ONE || ch2 == ONE) {
				sb.append(ONE);
			} else {
				sb.append(ZERO);
			}
		}
		return sb.toString();
	}

	private String not(String result1) {
		StringBuilder sb = new StringBuilder(result1.length());
		for (char ch : result1.toCharArray()) {
			if (ch == ZERO) {
				sb.append(ONE);
			} else {
				sb.append(ZERO);
			}
		}
		return sb.toString();
	}
}
