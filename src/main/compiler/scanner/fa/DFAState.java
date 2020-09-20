package main.compiler.scanner.fa;

public class DFAState extends State {
	public static final String PREFIX = "d";

	public DFAState(int id, boolean isFinal) {
		this(PREFIX + id, isFinal);
	}
	
	public DFAState(String name, boolean isFinal) {
		super(name, isFinal);
	}

	public int getID() {
		return Integer.parseInt(getName().substring(PREFIX.length()));
	}
}
