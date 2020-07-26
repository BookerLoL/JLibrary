package Parsers.Basic;

import java.util.List;

public class Rule {
	Symbol left;
	List<Symbol> right;
	
	public Rule(Symbol left, List<Symbol> right) {
		this.left = left;
		this.right = right;
	}
	
	public Symbol getLeftSide() {
		return left;
	}
	
	public List<Symbol> getRightSide() {
		return right;
	}
}
