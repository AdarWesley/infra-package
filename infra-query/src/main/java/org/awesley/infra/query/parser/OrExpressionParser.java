package org.awesley.infra.query.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.awesley.infra.query.OrQueryExpression;
import org.awesley.infra.query.QueryExpression;

public class OrExpressionParser extends ExpressionParser {

	private List<ExpressionParser> subExpressionParsers = new ArrayList<ExpressionParser>();

	public OrExpressionParser(String ex, int startOffset) {
		super(ex, startOffset);
	}

	public void Parse() {
		SkipWhitespace();
		while (subExpressionParsers.size() == 0 || isLookAhead(';')) {
			if (isLookAhead(';')) {
				currentOffset++;
				SkipWhitespace();
			}
			
			int startingOffset = currentOffset;
			
			AndExpressionParser andExpressionParser = new AndExpressionParser(stringExpression, currentOffset);
			andExpressionParser.Parse();
			currentOffset = andExpressionParser.getCurrentOffset();
			subExpressionParsers.add(andExpressionParser);
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
			throw new ParseExpressionException("OrExpressionParser empty expression: " + stringExpression + " at: " + currentOffset);
		}
		if (subExpressionParsers.size() == 1) {
			return subExpressionParsers.get(0).getQueryExpression();
		}
		
		List<QueryExpression> subQueryExpressions = 
				subExpressionParsers.stream().map(subExp -> subExp.getQueryExpression()).collect(Collectors.toList());
		
		return new OrQueryExpression(subQueryExpressions);
	}
}
