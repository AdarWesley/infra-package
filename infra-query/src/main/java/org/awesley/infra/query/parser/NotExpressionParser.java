package org.awesley.infra.query.parser;

import org.awesley.infra.query.NotQueryExpression;
import org.awesley.infra.query.QueryExpression;

public class NotExpressionParser extends ExpressionParser {

	ExpressionParser subExpressionParser;
	
	public NotExpressionParser(String ex, int startOffset2) {
		super(ex, startOffset2);
	}

	public void Parse() {
		if (isLookAhead('!')) {
			throw new ParseExpressionException("NotExpressionParser: invalid Not Expression: " + stringExpression + " at: " + currentOffset);
		}
		currentOffset++;
		SkipWhitespace();

		if (isLookAhead('(')) {
			ParenthesesExpressionParser parenExpressionParser = new ParenthesesExpressionParser(stringExpression, currentOffset);
			parenExpressionParser.Parse();
			currentOffset = parenExpressionParser.getCurrentOffset();
			subExpressionParser = parenExpressionParser;
		} else {
			SimpleExpressionParser simpleExpressionParser = new SimpleExpressionParser(stringExpression, currentOffset);
			simpleExpressionParser.Parse();
			currentOffset = simpleExpressionParser.getCurrentOffset();
			subExpressionParser = simpleExpressionParser;
		}
		SkipWhitespace();
	}

	@Override
	public QueryExpression getQueryExpression() {
		return new NotQueryExpression(subExpressionParser.getQueryExpression());
	}

}
