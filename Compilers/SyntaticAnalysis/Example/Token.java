package syntatic_analysis;

public class Token {
	public enum Type {
		IDENTIFIER("<identifier>"),
		INT_LITERAL("<integer-literal>"), 
		OPERATOR("<operator>"), 
		BEGIN("begin"), 		// begin 
		CONST("const"), 		// const
		DO("do"),    		// do
		ELSE("else"),  		// else
		END("end"),   		// end
		IF("if"),    		// if
		IN("in"), 		// in
		LET("let"),        // let
		THEN("then"),       // then
		VAR("var"),        // var
		WHILE("while"),      // while
		SEMICOLON(";"),  // ;
		COLON(":"),      // :
		ASSIGNMENT(":="), // :=
		IS("~"), 		// ~
		LPAREN("("),     // (
		RPAREN(")"),     // )
		EOT("<eot>");        // end of text
		
		private String spelling;
		private Type(String spelling) {
			this.spelling = spelling;
		}
		
		public String getSpelling() {
			return spelling;
		}
		
	}
	
	public Type kind;
	public String name;
	
	public Token(Type kind, String name) {
		this.kind = kind;
		this.name = name;
	}
}
