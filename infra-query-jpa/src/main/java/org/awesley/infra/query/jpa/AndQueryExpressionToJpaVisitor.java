package org.awesley.infra.query.jpa;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.awesley.infra.query.AndQueryExpression;
import org.awesley.infra.query.QueryExpression;
import org.springframework.stereotype.Component;

@Component
public class AndQueryExpressionToJpaVisitor extends QueryExpressionToJpaVisitor {

	static {
		QueryExpressionToJpaVisitor.registerVisitor(AndQueryExpression.class, new AndQueryExpressionToJpaVisitor());
	}

	@Override
	protected <T> Predicate convert(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb,
			QueryExpression expression) {
		AndQueryExpression andQueryExpression = (AndQueryExpression)expression;
		
		List<Predicate> subPredicates = andQueryExpression.getSubQueryExpressions().stream()
				.map(se -> QueryExpressionToJpaVisitor.baseConvert(root, query, cb, se))
				.collect(Collectors.toList());
		return cb.and(subPredicates.toArray(new Predicate[0]));
	}
}
