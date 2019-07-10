package org.awesley.infra.query;

public class NotQueryExpression extends QueryExpression {

	private QueryExpression queryExpression;

	public NotQueryExpression(QueryExpression queryExpression) {
		this.queryExpression = queryExpression;
	}

	public QueryExpression getQueryExpression() {
		return queryExpression;
	}

}
