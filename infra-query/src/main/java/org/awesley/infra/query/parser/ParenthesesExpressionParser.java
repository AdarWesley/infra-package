package org.awesley.infra.query.parser;

import org.awesley.infra.query.QueryExpression;

public class ParenthesesExpressionParser extends ExpressionParser {

	ExpressionParser subExpressionParser;
	
	public ParenthesesExpressionParser(String ex, int startOffset2) {
		super(ex, startOffset2);
	}

	public void Parse() {
		if (!isLookAhead('(')) {
			throw new ParseExpressionException("ParenthesesExpressionParser: invalid Parentheses Expression: " + stringExpression + " at: " + currentOffset);
		}
		currentOffset++;
		SkipWhitespace();
		
		int startingOffset = currentOffset;
		
		OrExpressionParser orExpressionParser = new OrExpressionParser(stringExpression, currentOffset);
		orExpressionParser.Parse();
		currentOffset = orExpressionParser.getCurrentOffset();
		subExpressionParser = orExpressionParser;
		
		if (startingOffset == currentOffset) {
			throw new ParseExpressionException("ParenthesesExpressionParser: empty parenthese expression: " + stringExpression + " at: " + currentOffset);
		}
		
		SkipWhitespace();
		
		if (isLookAhead(')')) {
			currentOffset++;
			SkipWhitespace();
		} else {
			throw new ParseExpressionException("ParenthesesExpressionParser: imbalanced parentheses: " + stringExpression + " at: " + currentOffset);
		}
	}

	@Override
	public QueryExpression getQueryExpression() {
		return subExpressionParser.getQueryExpression();
	}

}
