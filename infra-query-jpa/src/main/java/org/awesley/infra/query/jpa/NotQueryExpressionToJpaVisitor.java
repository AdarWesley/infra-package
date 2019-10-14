package org.awesley.infra.query.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.awesley.infra.query.NotQueryExpression;
import org.awesley.infra.query.QueryExpression;
import org.springframework.stereotype.Component;

@Component
public class NotQueryExpressionToJpaVisitor extends QueryExpressionToJpaVisitor {

	static {
		QueryExpressionToJpaVisitor.registerVisitor(NotQueryExpression.class, new NotQueryExpressionToJpaVisitor());
	}

	@Override
	protected <T> Predicate convert(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb,
			QueryExpression expression) {
		NotQueryExpression notQueryExpression = (NotQueryExpression)expression;
		
		return cb.not(QueryExpressionToJpaVisitor.baseConvert(root, query, cb, notQueryExpression.getQueryExpression()));
	}
}
