package main.compiler.scanner.fa;

public class NFAState extends State {
	protected static final String PREFIX = "n";
	protected static final int ERROR_ID = -1;

	public NFAState(int id, boolean isFinal) {
		this(PREFIX + id, isFinal);
	}

	public NFAState(String name, boolean isFinal) {
		super(name, isFinal);
	}

	@Override
	public int getID() {
		return Integer.parseInt(getName().substring(PREFIX.length()));
	}
}
