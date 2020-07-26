package syntatic_analysis;

import syntatic_analysis.Token.Type;

public class Scanner {
	private char currentChar;
	private Type type;
	private StringBuffer currentSpelling;
	
	private void take(char expectedChar) {
		if (currentChar == expectedChar) {
			currentSpelling.append(currentChar);
			currentChar = '\0'; //next char
		} else {
			System.out.println("lexical error on char");
		}
	}
	
	private void takeIt() {
		currentSpelling.append(currentChar);
		currentChar = '\0'; //next char
	}
	
	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}
	
	private boolean isLetter(char c) {
		c = Character.toLowerCase(c);
		return c >= 'a' && c <= 'z';
	}
	
	private boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '<' || c == '>' || c == '=' || c == '\\';
	}
	
	private Type scanToken() {
		Type type = null;
		if (isLetter(currentChar)) {
			takeIt();
			while (isLetter(currentChar) || isDigit(currentChar)) {
				takeIt();
			}
			type = Type.IDENTIFIER;
		} else if (isDigit(currentChar)) {
			takeIt();
			while(isDigit(currentChar)) {
				takeIt();
			}
			type = Type.INT_LITERAL;
		} else if (isOperator(currentChar)) {
			takeIt();
			type = Type.OPERATOR;
		} else if (currentChar == ';') {
			takeIt();
			type = Type.SEMICOLON;
		} else if (currentChar ==':') {
			takeIt();
			if (currentChar == '=') {
				takeIt();
				type = Type.ASSIGNMENT;
			} else {
				type = Type.COLON;
			}
		} else if (currentChar == '~') {
			takeIt();
			type = type.IS;
		} else if (currentChar == '(') {
			takeIt();
			type = type.LPAREN;
		} else if (currentChar == ')') {
			takeIt();
			type = type.RPAREN;
		} else if (currentChar =='\000') {
			type = type.EOT;
		} else {
			System.out.println("Unrecognized character: " + currentChar);
		}
		
		return type;
	}
}
