package syntatic_analysis;

public abstract class Terminal extends AST {
	public String spelling;
	
	public Terminal(String spelling) {
		this.spelling = spelling;
	}
}
