package org.awesley.infra.query.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.awesley.infra.query.AndQueryExpression;
import org.awesley.infra.query.QueryExpression;

public class AndExpressionParser extends ExpressionParser {

	private List<ExpressionParser> subExpressionParsers = new ArrayList<ExpressionParser>();
	
	public AndExpressionParser(String stringExpression, int currentOffset) {
		super(stringExpression, currentOffset);
	}

	public void Parse() {
		SkipWhitespace();
		while (subExpressionParsers.size() == 0 || isLookAhead(',')) {
			if (isLookAhead(',')) {
				currentOffset++;
				SkipWhitespace();
			}
			
			int startingOffset = currentOffset;
			
			if (isLookAhead('!')) {
				NotExpressionParser notExpressionParser = new NotExpressionParser(stringExpression, currentOffset);
				notExpressionParser.Parse();
				currentOffset = notExpressionParser.getCurrentOffset();
				subExpressionParsers.add(notExpressionParser);
			} else if (isLookAhead('(')) {
				ParenthesesExpressionParser parenExpressionParser = new ParenthesesExpressionParser(stringExpression, currentOffset);
				parenExpressionParser.Parse();
				currentOffset = parenExpressionParser.getCurrentOffset();
				subExpressionParsers.add(parenExpressionParser);
			} else {
				SimpleExpressionParser simpleExpressionParser = new SimpleExpressionParser(stringExpression, currentOffset);
				simpleExpressionParser.Parse();
				currentOffset = simpleExpressionParser.getCurrentOffset();
				subExpressionParsers.add(simpleExpressionParser);
			}
			SkipWhitespace();
			
			if (startingOffset == currentOffset) {
				// didn't advance at all in this loop.  Break out.
				break;
			}
		}
	}

	@Override
	public QueryExpression getQueryExpression() {
		if (subExpressionParsers.size() == 0) {
			throw new ParseExpressionException("AndExpressionParser empty expression: " + stringExpression + " at: " + currentOffset);
		}
		if (subExpressionParsers.size() == 1) {
			return subExpressionParsers.get(0).getQueryExpression();
		}
		
		List<QueryExpression> subQueryExpressions = 
				subExpressionParsers.stream().map(subExp -> subExp.getQueryExpression()).collect(Collectors.toList());
		
		return new AndQueryExpression(subQueryExpressions);
	}

}
