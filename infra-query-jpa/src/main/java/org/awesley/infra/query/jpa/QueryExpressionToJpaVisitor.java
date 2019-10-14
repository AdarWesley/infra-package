package org.awesley.infra.query.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.awesley.infra.query.QueryExpression;

public abstract class QueryExpressionToJpaVisitor {

	private static Map<Class<? extends QueryExpression>, QueryExpressionToJpaVisitor> visitorsMap =
			new HashMap<Class<? extends QueryExpression>, QueryExpressionToJpaVisitor>();

	public static <T> Predicate baseConvert(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb,
			QueryExpression expression) {
		QueryExpressionToJpaVisitor visitor = visitorsMap.get(expression.getClass());
		return visitor.convert(root, query, cb, expression);
	}

	protected abstract <T> Predicate convert(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb, QueryExpression expression);

	public static void registerVisitor(Class<? extends QueryExpression> queryExpressionClass,
			QueryExpressionToJpaVisitor queryExpressionToJpaVisitor) {
		visitorsMap.put(queryExpressionClass, queryExpressionToJpaVisitor);
	}
}
