package org.awesley.infra.query;

import java.util.List;

public class AndQueryExpression extends QueryExpression {

	private List<QueryExpression> subQueryExpressions;

	public AndQueryExpression(List<QueryExpression> subQueryExpressions) {
		this.subQueryExpressions = subQueryExpressions;
	}

	public List<QueryExpression> getSubQueryExpressions() {
		return subQueryExpressions;
	}

}
