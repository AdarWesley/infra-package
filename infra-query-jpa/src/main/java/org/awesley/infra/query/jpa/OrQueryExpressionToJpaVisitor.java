package org.awesley.infra.query.jpa;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.awesley.infra.query.OrQueryExpression;
import org.awesley.infra.query.QueryExpression;
import org.springframework.stereotype.Component;

@Component
public class OrQueryExpressionToJpaVisitor extends QueryExpressionToJpaVisitor {

	static {
		QueryExpressionToJpaVisitor.registerVisitor(OrQueryExpression.class, new OrQueryExpressionToJpaVisitor());
	}

	@Override
	protected <T> Predicate convert(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb,
			QueryExpression expression) {
		OrQueryExpression orQueryExpression = (OrQueryExpression)expression;
		
		List<Predicate> subPredicates = orQueryExpression.getSubQueryExpressions().stream()
				.map(se -> QueryExpressionToJpaVisitor.baseConvert(root, query, cb, se))
				.collect(Collectors.toList());
		return cb.or(subPredicates.toArray(new Predicate[0]));
	}
}
