package grammar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * Will ignore whitespaces and empty lines
 * 
 * White lines delimit each token
 * 
 * Examples of expected types of input:  
 * 					
 * 					phrase -> Verb Noun | terminal Verb | ε
 * 					Noun -> thing
 * 				  	Verb -> nt
 * 					Verb -> nt2 
 * 					Verb -> ε
 */
public class GrammarReader {
	public static final String ENCODING_UTF8 = "UTF-8";
	public static final String ENCODING_UTF16 = "UTF-16";
	private List<Map.Entry<String, List<List<String>>>> productionRuleInfo; //Nonterminal name as key, list of production rules to late be figured out
	private String filename;

	public GrammarReader(String filename) {
		this.filename = filename;
		productionRuleInfo = new LinkedList<>();
	}

	//2 lists, first list is the left side of the arrow, second list is right side side of the terminal delimited by nulls if there is an | symbol
	public void produceRulesInfo(String encoding) {
		System.out.println('\u03B5');
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), encoding))) {
			System.out.println("Reading from file: " + filename);
			int lineNumber = 1;
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() != 0) {
					System.out.println("Reading line #: " + lineNumber);
					getRuleInfoFromLine(line);
				} else {
					System.out.println("Line #: " + lineNumber + " is empty");
				}
				lineNumber++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Map.Entry<String, List<List<String>>> getRuleInfo(String keyname) {
		for (Map.Entry<String, List<List<String>>> entry : productionRuleInfo) {
			if (entry.getKey().equals(keyname)) {
				return entry;
			}
		}
		return null;
	}

	private void getRuleInfoFromLine(String input) {
		Map.Entry<String, List<List<String>>> entry = null;
		List<String> ruleInfo = new LinkedList<>();
		String productionName = null;
		boolean isNew = false;
		
		StringBuilder sb = new StringBuilder();
		int index = 0;
		final int length = input.length();
		
		//find the production name 
		while (index < length && input.charAt(index) != GrammarRuleFormat.arrow) {
			if (input.charAt(index) != ' ') {
				sb.append(input.charAt(index));
			}
			index++;
		}
		
		if (sb.length() >= 1) {
			productionName = sb.toString();
			sb.setLength(0); //clear
			entry = getRuleInfo(productionName);
			if (entry == null) {
				entry = Map.entry(productionName, new LinkedList<>());
				isNew = true;
			}
		} else {
			System.out.println("There is no production rule name error");
			return;
		}
		
		//find the transition arrow 
		if (index < length && input.charAt(index) == GrammarRuleFormat.arrow) {
			index++;
		} else {
			System.out.println("There is no transition arrow: " + GrammarRuleFormat.arrow);
			return;
		}
		

		char lastValidChar = '\0';
		while (index < length) {
			char ch = input.charAt(index);
			if (ch == GrammarRuleFormat.or) {
				lastValidChar = ch;
				if (ruleInfo.isEmpty()) {
					System.out.println("This rule has an empty right side production rule");
				} else {
					entry.getValue().add(ruleInfo);
					ruleInfo = new LinkedList<>();
				}
			} else if (ch == GrammarRuleFormat.space) {
				if (sb.length() > 0) {
					ruleInfo.add(sb.toString());
					sb.setLength(0);
				}
			} else {
				lastValidChar = ch;
				sb.append(ch);
			}
			index++;
		}
		
		if (lastValidChar == GrammarRuleFormat.or && ruleInfo.isEmpty() ) {
			System.out.println("This rule has en empty right side production rule");
		} else {
			if (sb.length() > 0) {
				ruleInfo.add(sb.toString());
			}
			entry.getValue().add(ruleInfo);
			if (isNew) {
				productionRuleInfo.add(entry);
			}
		}
	}
	
	public void setFilename(String newFilename) {
		filename = newFilename;
	}

	public List<Map.Entry<String, List<List<String>>>> getProductionRules() {
		return productionRuleInfo;
	}

	public static void main(String[] args) {
		String file = "test";
		//String file = "grammar_rules";
		GrammarReader gr = new GrammarReader(file);
		gr.produceRulesInfo(GrammarReader.ENCODING_UTF16);
		ContextFreeGrammar cfg = new ContextFreeGrammar(gr.getProductionRules());
		cfg.printAll();
	}
}
