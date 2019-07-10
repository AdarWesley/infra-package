package org.awesley.infra.query.parser;

import org.awesley.infra.query.QueryExpression;

public class QueryExpressionParser extends ExpressionParser {
	
	ExpressionParser subExpressionParser;
	
	public QueryExpressionParser(String filterCriteria) {
		super(filterCriteria, 0);
	}
	
	public void Parse() {
		OrExpressionParser orExParser = new OrExpressionParser(stringExpression, 0);
		orExParser.Parse();
		if (orExParser.getCurrentOffset() != stringExpression.length()) {
			throw new ParseExpressionException("Extra charactes at end of expression: " + stringExpression + " at: " + orExParser.getCurrentOffset());
		}
		subExpressionParser = orExParser;
	}

	@Override
	public QueryExpression getQueryExpression() {
		return subExpressionParser.getQueryExpression();
	}
}
