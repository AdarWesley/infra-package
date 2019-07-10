package org.awesley.infra.query;

import java.util.List;

public class OrQueryExpression extends QueryExpression {

	private List<QueryExpression> subQueryExpressions;

	public OrQueryExpression(List<QueryExpression> subQueryExpressions) {
		this.subQueryExpressions = subQueryExpressions;
	}

	public List<QueryExpression> getSubQueryExpressions() {
		return subQueryExpressions;
	}

}
