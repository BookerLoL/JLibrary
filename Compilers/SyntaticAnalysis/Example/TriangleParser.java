package syntatic_analysis;

/*
 *    		Program ::= single-Command
            Command ::= single-Command (; single-Command)*
            single-Command ::= identifier (:= Expression | (Expression))
                            | ...
                            | ... 
            Expression ::= primary-Expression (Operator primary-Expression)*
            primary-Expression ::= ... | ... 
            Declaration := single-Declaration (; single-Declaration)*
            single-Declaration ::= const identifier ~ Expression
                                | var idenfier : Type-denoter
            Type-denoter ::= Identifier 
            Identifier ::= Letter | Identifier Letter | Identifier Digit
 */
public class TriangleParser {
	private Token currentToken;
	
	private void acceptIt() {
		currentToken = null; //scanner.scan();
	}
	private void accept(Token.Type kind) {
		if (currentToken.kind == kind) {
			currentToken = null; //scanner.scan();
		} else {
			System.out.println("Syntactic Error, expected: " + kind + " but got: " + currentToken.kind);
		}
	}
	
	public Program parse() {
		currentToken = null; //scanner.scan();
		Program program = parseProgram();
		if (currentToken.kind != Token.Type.EOT) {
			//report error
		}
		return program;
	}
	
	private Program parseProgram() {
		Command command = parseSingleCommand();
		return new Program(command);
	}
	private Command parseCommand() {
		Command commandAST = parseSingleCommand(); //Command ::= single-Command 
		while (currentToken.kind == Token.Type.SEMICOLON) {  //(; single-Command)*
			acceptIt();
			Command secondCommand = parseSingleCommand();
			commandAST = new SequentialCommand(commandAST, secondCommand);
		}
		return commandAST;
	}
	
	private Command parseSingleCommand() {
		Command commandAST = null;
		switch (currentToken.kind) {
			case IDENTIFIER:
				{
					Identifier idenAST = parseIdentifier();
					switch (currentToken.kind) {
						case ASSIGNMENT:
							{
								acceptIt();
								Expression exprAST = parseExpression();
								commandAST = new AssignCommand(new SimpleVName(idenAST), exprAST);
							}
							break;
						case LPAREN: 
							{
								acceptIt();
								Expression exprAST = parseExpression();
								accept(Token.Type.RPAREN);
								commandAST = new CallCommand(idenAST, exprAST);
							}
							break;
						default:
							System.out.println("Syntactic Error, "  + "wrong type for identifier: got: " + currentToken.kind);
					}
				}
				break;
			case IF:
				{
					acceptIt();
					Expression exprAST = parseExpression();
					accept(Token.Type.THEN);
					Command c1AST = parseSingleCommand();
					accept(Token.Type.ELSE);
					Command c2AST = parseSingleCommand();
					commandAST = new IfCommand(exprAST, c1AST, c2AST);
				}
				break;
			case WHILE:
				{
					acceptIt();
					Expression exprAST = parseExpression();
					accept(Token.Type.DO);
					Command c1AST = parseSingleCommand();
					commandAST = new WhileCommand(exprAST, c1AST);
				}
				break;
			case LET:
				{
					acceptIt();
					Declaration declrAST = parseDeclaration();
					accept(Token.Type.IN);
					Command c1AST = parseSingleCommand();
					commandAST = new LetCommand(declrAST, c1AST);
				}
				break;
			case BEGIN:
				{
					acceptIt();
					commandAST = parseCommand();
					accept(Token.Type.END);
				}
				break;
			default: 
				System.out.println("Syntactic Error, "  + "wrong type for single command: got: " + currentToken.kind);
		}
		return commandAST;
	}
	private Expression parseExpression() {
		Expression exprAST = parsePrimaryExpression();
		while (currentToken.kind == Token.Type.OPERATOR) {  //(; single-Command)*
			acceptIt();
			Expression nextExprAST =  parsePrimaryExpression();
			exprAST = new BinaryExpression(exprAST, nextExprAST, null); // shouldn't be mull
		}
		return exprAST;
	}
	private Declaration parseDeclaration() {
		Declaration declrAST = parseSingleDeclaration();
		while (currentToken.kind == Token.Type.SEMICOLON) {  //(; single-Command)*
			acceptIt();
			Declaration nextDeclrAST = parseSingleDeclaration();
			declrAST = new SequentialDeclaration(declrAST, nextDeclrAST);
		}
		return declrAST;
	}
	
	
	private TypeDenoter parseTypeDenoter() {
		return new SimpleTypeDenoter(parseIdentifier());
	}
	
	private Identifier parseIdentifier() {
		Identifier idenAST = null;
		if (currentToken.kind == Token.Type.IDENTIFIER) {
			idenAST = new Identifier(currentToken.name);
			currentToken = null; //scanner.scan();
		} else {
			//report error
		}
		return idenAST;
	}
	
	private Operator parseOperator() {
		Operator op = null;
		if (currentToken.kind == Token.Type.OPERATOR) {
			op = new Operator(currentToken.name);
			currentToken = null; //scanner.scan();
		} else {
			//report error
		}
		return op;
	}
	
	private IntegerLiteral parseIntegerLiteral() {
		IntegerLiteral intLit = null;
		if (currentToken.kind == Token.Type.INT_LITERAL) {
			intLit = new IntegerLiteral(currentToken.name);
			currentToken = null; //scanner.scan();
		} else {
			//report error
		}
		return intLit;
	}
	
	private Expression parsePrimaryExpression() {
		Expression primaryExpr = null;
		switch (currentToken.kind) {
		case INT_LITERAL: 
			IntegerLiteral intLit = parseIntegerLiteral();
			break;
		case IDENTIFIER:
			Identifier idenAST = parseIdentifier();
			break;
		case OPERATOR:
			{
				Operator op = parseOperator();
				Expression exprTemp = parsePrimaryExpression();
				primaryExpr = new UnaryExpression(op, exprTemp);
			}
			break;
		case LPAREN:
			{
				acceptIt();
				primaryExpr = parseExpression();
				accept(Token.Type.RPAREN);
			}
			break;
		default: 
			System.out.println("Syntactic Error, "  + "wrong type for primaryExpression: got: " + currentToken.kind);
		}
		return primaryExpr;
	}
	
	
	private Declaration parseSingleDeclaration() {
		Declaration declAST = null;
		switch (currentToken.kind) {
		case CONST: 
			{
				acceptIt();
				Identifier idenAST = parseIdentifier();
				accept(Token.Type.IS);
				Expression exprAST = parseExpression();
				declAST = new ConstDeclaration(idenAST, exprAST);
			}
			break;	
		case VAR: 
			{
				acceptIt(); //accept(Token.Type.VAR) 
				Identifier idenAST = parseIdentifier();
				accept(Token.Type.COLON);
				TypeDenoter typeDeno = parseTypeDenoter();
				declAST = new VarDeclaration(idenAST, typeDeno);
			}
			break;
		default:
			System.out.println("Syntactic Error, "  + "wrong type for singleDeclaration: got: " + currentToken.kind);
			break;
		}
		return declAST;
	}
}
