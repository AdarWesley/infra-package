package org.awesley.infra.query;

public class BinaryQueryExpression extends QueryExpression {

	private String leftOperand;
	private Operator operator;
	private String rightOperand;
	
	public BinaryQueryExpression(String leftOperand, Operator operator, String rightOperand) {
		this.leftOperand = leftOperand;
		this.operator = operator;
		this.rightOperand = rightOperand;
	}

	public Operator getOperator() {
		return operator;
	}

	public String getLeftOperand() {
		return leftOperand;
	}

	public String getRightOperand() {
		return rightOperand;
	}
}
