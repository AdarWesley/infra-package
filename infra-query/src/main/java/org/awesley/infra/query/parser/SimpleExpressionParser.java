package org.awesley.infra.query.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.awesley.infra.query.BinaryQueryExpression;
import org.awesley.infra.query.Operator;
import org.awesley.infra.query.QueryExpression;

public class SimpleExpressionParser extends ExpressionParser {

	private String leftOperand;
	private Operator operator;
	private String rightOperand;

	public SimpleExpressionParser(String ex, int startOffset2) {
		super(ex, startOffset2);
	}

	public void Parse() {
		Pattern separatorsRegexp = Pattern.compile("[\\(\\),;!]");
		Matcher m = separatorsRegexp.matcher(stringExpression);
		String simpleExpressionString;
		if (m.find(currentOffset)) {
			simpleExpressionString = stringExpression.substring(currentOffset, m.start());
		} else {
			simpleExpressionString = stringExpression.substring(currentOffset);
		}
		
		String opRegExp = Operator.getRegExp();
		String regexPattern = "^(?<left>.+)(?<!" + opRegExp + ")(?<op>"+ opRegExp + ")(?!" + opRegExp + ")(?<right>.+)$";
		Pattern expressionPattern = Pattern.compile(regexPattern);
		Matcher matcher = expressionPattern.matcher(simpleExpressionString);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("SimpleExpressionParser: failed to match expression: " + stringExpression + " at: " + currentOffset);
		}
		
		currentOffset += simpleExpressionString.length();
		
		leftOperand = matcher.group("left");
		operator = Operator.valueOfOp(matcher.group("op"));
		rightOperand = matcher.group("right");
	}

	@Override
	public QueryExpression getQueryExpression() {
		return new BinaryQueryExpression(leftOperand, operator, rightOperand);
	}

}
