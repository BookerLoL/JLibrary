package grammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContextFreeGrammar {
	protected State starting;
	protected List<State> productionRules;
	protected List<String> terminalSymbols;
	protected List<String> nonterminalSymbols;
	
	/*
	 * Assumes that the production rules start with the starting symbol
	 * 
	 * The rest of the information can be figured out
	 */
	public ContextFreeGrammar(List<Map.Entry<String, List<List<String>>>> productionRulesInfo) {
		constructCFG(productionRulesInfo);
	}
	
	public ContextFreeGrammar(List<State> productionRules, boolean startingIsFirst) {
		//derive info based on production rules
	}
	
	public ContextFreeGrammar(State starting, List<State> productionRules, List<String> temrinalSymbols, List<String> nonterminalSymbols) {
		//ensure each value is correct
	}
	
	private void constructCFG(List<Map.Entry<String, List<List<String>>>> productionRulesInfo) {
		Set<String> terminals = new HashSet<>();
		Set<String> nonterminals = new HashSet<>();
		int numRules = 0;
		for (Map.Entry<String, List<List<String>>> entry : productionRulesInfo) {
			nonterminals.add(entry.getKey());
			numRules += entry.getValue().size();
		}
		
		productionRules = new ArrayList<>(numRules);
		for (Map.Entry<String, List<List<String>>> entry : productionRulesInfo) {	
			for (List<String> info: entry.getValue()) {
				State start = new State(entry.getKey(), false);
				State currLast = start;
				List<String> terminalsStrs = new LinkedList<>();
				List<String> nonterminalsStrs = new LinkedList<>();
				for (String symbol : info) {
					boolean isNonTerminalSymbol = nonterminals.contains(symbol);
					if (isNonTerminalSymbol) {
						if (terminalsStrs.size() >= 1) {
							currLast.setTransition(GrammarRuleFormat.transitionTTNT(terminalsStrs, symbol));
							currLast = currLast.getLast();
							terminalsStrs.clear();
						} else {
							nonterminalsStrs.add(symbol);
						}
					} else {
						terminals.add(symbol);
						if (nonterminalsStrs.size() >= 1) {
							currLast.setTransition(GrammarRuleFormat.transitionNTNT(nonterminalsStrs));
							currLast = currLast.getLast();
							nonterminalsStrs.clear();
						}
						terminalsStrs.add(symbol);
					}
				}
				
				//Only one or the other list has symbols
				if (terminalsStrs.size() >= 1) {
					currLast.setTransition(GrammarRuleFormat.transitionTT(terminalsStrs));
				} else if (nonterminalsStrs.size() >= 1) {
					currLast.setTransition(GrammarRuleFormat.transitionNTNT(nonterminalsStrs));
				}
				
				productionRules.add(start);
			}
		}
		
		starting = productionRules.get(0);
		terminalSymbols = new ArrayList<>(terminals);
		nonterminalSymbols = new ArrayList<>(nonterminals);
	}
	
	public void printAll() {
		System.out.println(starting);
		System.out.println("NonTerminals");
		nonterminalSymbols.forEach(symbol -> System.out.println(symbol));
		System.out.println("Terminals");
		terminalSymbols.forEach(symbol -> System.out.println(symbol));
		System.out.println("Production rules");
		productionRules.forEach(rule -> System.out.println(rule));
	}
}
