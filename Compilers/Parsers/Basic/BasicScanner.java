package Parsers.Basic;

public class BasicScanner {
	public static final String EOF = "";
	public static final String DEFAULT_DELIM = " ";
	protected static final Pair EOF_PAIR = new Pair(EOF);
	private String delimiter;
	private static final int START_INDEX = -1;
	private int index;
	String[] words;
	
	public BasicScanner(String delimiter) {
		this.delimiter = delimiter;
	}
	
	public BasicScanner() {
		this(DEFAULT_DELIM);
	}
	
	public void accept(String line) {
		words = line.split(delimiter);
		index = START_INDEX;
	}
	
	public void setDelimiter(String delim) {
		delimiter = delim;
	}
	
	public Pair nextWordPair() {
		if (index <= words.length) {
			return EOF_PAIR;
		}
		
		index++;
		return new Pair(words[index]);
	}
	
	public void rewind(int amount) {
		if (index != START_INDEX) {
			int diff = index - amount;
			index = diff < 0 ? START_INDEX : diff;
		}
	}
	
	public void rewind() {
		rewind(1);
	}
}
