package syntatic_analysis;

/*
 *  micro-English Grammar
 * 	Sentence ::= Subject Verb Object . 
 *  Subject ::= I | a Noun | the Noun
 *  Object ::= me | a Noun | the Noun
 *  Noun ::= cat | mat | rat
 *  Verb ::= like | is | see | sees
 *  
 *  105
 */
public class MicroEnglishParser {
	private String currentTerminal;
	
	public void parse() {
		currentTerminal = "firstInputTerminal"; 
		parseSentence();
		//check no terminal follows sentence
	}
	
	private void accept(String expectedTerminal) {
		if (currentTerminal.equals(expectedTerminal)) {
			currentTerminal = "nextInputTerminal"; 
		} else {
			System.out.println("Syntactic Error, expected: " + expectedTerminal + " but got: " + currentTerminal);
		}
	}
	
	private void parseNoun() {
		if (currentTerminal.matches("cat|mat|rat")) {
			accept(currentTerminal);
		} else {
			System.out.println("Syntactic Error, symbol is not a noun, got: " + currentTerminal);
		}
	} 
	
	private void parseVerb() {
		if (currentTerminal.matches("like|is|see|sees")) {
			accept(currentTerminal);
		} else {
			System.out.println("Syntactic Error, symbol is not a verb, got: " + currentTerminal);
		}
	}
	
	private void parseSubject() {
		if (currentTerminal.equals("I")) {
			accept("I");
		} else if (currentTerminal.matches("a|the")) {
			accept(currentTerminal);
			parseNoun();
		} else {
			System.out.println("Syntactic Error, symbol is not a subject, got: " + currentTerminal);
		}
	}
	private void parseObject() {
		if (currentTerminal.equals("me")) {
			accept("me");
		} else if (currentTerminal.matches("a|the")) {
			accept(currentTerminal);
			parseNoun();
		} else {
			System.out.println("Syntactic Error, symbol is not a object, got: " + currentTerminal);
		}
	}
	
	private void parseSentence() {
		parseSubject();
		parseVerb();
		parseObject();
		accept(".");
	}
	
}
