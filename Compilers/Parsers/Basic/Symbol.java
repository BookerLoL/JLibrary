package Parsers.Basic;

public class Symbol {
	String name;
	boolean isTerminal;
	
	public Symbol(String name, boolean isTerminal) {
		this.name = name;
		this.isTerminal = isTerminal;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isTerminal() {
		return isTerminal;
	}
}
