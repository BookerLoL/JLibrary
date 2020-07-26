package not_sure;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NGram {
	public static final int DEFAULT_LENGTH = 1;
	public static final String DEFAULT_PREFIX_TAG = "";
	public static final String DEFAULT_SUFFIX_TAG = "";
	public static final String DEFAULT_DELIMITER = " ";
	public static final String CHAR_DELIMITER = "";

	private int length;
	private String prefixTag;
	private String suffixTag;
	private String delimiter;

	public NGram(int length) {
		this(length, DEFAULT_PREFIX_TAG, DEFAULT_SUFFIX_TAG, DEFAULT_DELIMITER);
	}
	
	public NGram(int length, String delimiter) {
		this(length, DEFAULT_PREFIX_TAG, DEFAULT_SUFFIX_TAG, delimiter);
	}

	public NGram(int length, String prefixTag, String suffixTag) {
		this(length, prefixTag, suffixTag, DEFAULT_DELIMITER);
	}

	public NGram(int length, String prefixTag, String suffixTag, String delimiter) {
		checkValid(prefixTag);
		checkValid(suffixTag);
		checkValid(delimiter);
		
		this.length = getValid(length);
		this.prefixTag = prefixTag;
		this.suffixTag = suffixTag;
		this.delimiter = delimiter;
	}

	private static int getValid(int nGramLength) {
		return nGramLength < DEFAULT_LENGTH ? DEFAULT_LENGTH : nGramLength;
	}

	private static void checkValid(String input) {
		if (input == null) {
			throw new IllegalArgumentException("Input is null");
		}
	}

	public String getDelimiter() {
		return delimiter;
	}

	public String getPrefixTag() {
		return prefixTag;
	}

	public String getSuffixTag() {
		return suffixTag;
	}

	public int getNGramLength() {
		return length;
	}

	public void setDelimiter(String newDelimiter) {
		checkValid(newDelimiter);
		delimiter = newDelimiter;
	}

	public void setPrefixTag(String newPrefixTag) {
		checkValid(newPrefixTag);
		prefixTag = newPrefixTag;
	}

	public void setSuffixTag(String newSuffixTag) {
		checkValid(newSuffixTag);
		suffixTag = newSuffixTag;
	}

	public void setNGramLength(int newLength) {
		length = getValid(newLength);
	}

	public void useCharDelimiter() {
		setDelimiter(CHAR_DELIMITER);
	}
	
	public void useWordDelimiter() {
		setDelimiter(DEFAULT_DELIMITER);
	}

	private String addPaddingAffix(String text) {
		return (this.getPrefixTag() + delimiter + text + delimiter + this.getSuffixTag()).trim();
	}

	public List<List<String>> generateLList(String text) {
		List<List<String>> nGrams = new LinkedList<>();
		if (text == null || text == "") {
			return nGrams;
		}

		text = addPaddingAffix(text);
		String[] words = text.split(getDelimiter());
		
		if (words.length < getNGramLength()) {
			return nGrams;
		}

		int nGramsLength = getNGramLength();
		int lastWordIdx;

		for (int i = 0; i < words.length - nGramsLength + 1; i++) {
			lastWordIdx = i + nGramsLength - 1;
			List<String> nGramWords = new LinkedList<>();
			for (int wordIdx = i; wordIdx <= lastWordIdx; wordIdx++) {
				nGramWords.add(words[wordIdx]);
			}
			nGrams.add(nGramWords);
		}

		return nGrams;
	}

	public List<String[]> generateAList(String text) {
		List<String[]> nGrams = new LinkedList<>();
		if (text == null || text == "") {
			return nGrams;
		}

		text = addPaddingAffix(text);
		String[] words = text.split(getDelimiter());
		
		if (words.length < getNGramLength()) {
			return nGrams;
		}

		int nGramsLength = getNGramLength();
		int lastWordIdx;

		for (int i = 0; i <= words.length - nGramsLength; i++) {
			lastWordIdx = i + nGramsLength - 1;
			String[] nGramWords = new String[nGramsLength];
			for (int wordIdx = i; wordIdx <= lastWordIdx; wordIdx++) {
				nGramWords[wordIdx - i] = words[wordIdx];
			}
			nGrams.add(nGramWords);
		}

		return nGrams;
	}

	public List<String> generateSList(String text) {
		List<String> nGrams = new LinkedList<>();
		if (text == null || text == "") {
			return nGrams;
		}

		text = addPaddingAffix(text);
		String[] words = text.split(getDelimiter());
		
		if (words.length < getNGramLength()) {
			return nGrams;
		}

		StringBuilder sb = new StringBuilder();
		int nGramsLength = getNGramLength();
		int lastWordIdx;

		for (int i = 0; i <= words.length - nGramsLength; i++) {
			lastWordIdx = i + nGramsLength - 1;
			for (int wordIdx = i; wordIdx <= lastWordIdx; wordIdx++) {
				sb.append(words[wordIdx]);
				if (delimiter != CHAR_DELIMITER && wordIdx != lastWordIdx) {
					sb.append(DEFAULT_DELIMITER);
				}
			}
			nGrams.add(sb.toString());
			sb.setLength(0);
		}

		return nGrams;
	}

	public String[] generateArray(String text) {
		if (text == null || text == "") {
			return new String[0];
		}

		text = addPaddingAffix(text);
		String[] words = text.split(getDelimiter());
		
		if (words.length < getNGramLength()) {
			return new String[0];
		}

		StringBuilder sb = new StringBuilder();
		int nGramsLength = getNGramLength();
		int lastWordIdx;
		String[] nGrams = new String[words.length - nGramsLength + 1]; // off by 1 case, otherwise miss out on data

		for (int i = 0; i <= words.length - nGramsLength; i++) {
			lastWordIdx = i + nGramsLength - 1;
			for (int wordIdx = i; wordIdx <= lastWordIdx; wordIdx++) {
				sb.append(words[wordIdx]);
				if (delimiter != CHAR_DELIMITER && wordIdx != lastWordIdx) {
					sb.append(DEFAULT_DELIMITER);
				}
			}
			nGrams[i] = sb.toString();
			sb.setLength(0);
		}

		return nGrams;
	}

	public static void main(String[] args) {
		NGram n = new NGram(3, "");
		String input = "Testing the char options";
		System.out.println(Arrays.toString(n.generateArray(input)));
	}
}
