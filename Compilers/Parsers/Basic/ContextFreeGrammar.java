package Parsers.Basic;

import java.util.Set;

public class ContextFreeGrammar {
	protected Set<Symbol> terminals;
	protected Set<Symbol> nonterminals;
	protected Symbol start;
	protected Set<Rule> productionRules;
	
	public ContextFreeGrammar(Symbol start, Set<Symbol> terminals, Set<Symbol> nonterminals, Set<Rule> productionRules) {
		this.start = start;
		this.terminals = terminals;
		this.nonterminals = nonterminals;
		this.productionRules = productionRules;
	}
	
	public Symbol getStartingSymbol() {
		return start;
	}
	
	public Set<Rule> getProductionRules() {
		return productionRules;
	}
	
	public Set<Symbol> getTerminals() {
		return terminals;
	}
	
	public Set<Symbol> getNonTerminals() {
		return nonterminals;
	}
	
	public Symbol leftmostTopDownParsing() {
		
		return null;
	}
}
