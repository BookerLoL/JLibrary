package main.designpattern.interpreter;

public class NumberExpression extends Expression {
	private double value;
	
	public NumberExpression(double value) {
		this.value = value;
	}
	
	@Override
	public double evaluate() {
		return value;
	}

}
