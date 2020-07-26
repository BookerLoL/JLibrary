package Parsers.Basic;

public class Pair {
	public static final String ADD_OP = "+";
	public static final String SUB_OP = "-";
	public static final String MUL_OP = "*";
	public static final String DIV_OP = "/";

	public enum Type {
		Identifier, Float, Integer, Operator, IGNORE;

		public static Type getType(String input) {
			if (input.matches("+|-|*|/")) {
				return Type.Operator;
			} else if (input.matches("-?\\d+.\\d+")) {
				return Type.Float;
			} else if (input.matches("-?\\d+")) {
				return Type.Integer;
			} else if (input.equals(BasicScanner.EOF)) {
				return Type.IGNORE;
			} else {
				return Type.Identifier;
			}
		}
	}

	final Type type;
	final String name;

	public Pair(String name) {
		this.name = name;
		type = Type.getType(name);
	}

	public Type getType() {
		return type;
	}
}
