package org.awesley.infra.query.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.awesley.infra.query.BinaryQueryExpression;
import org.awesley.infra.query.Operator;
import org.awesley.infra.query.QueryExpression;
import org.springframework.stereotype.Component;

@Component
public class GTOperatorBinaryQueryExpressionToJpaVisitor extends BinaryQueryExpressionToJpaVisitor {
	static {
		BinaryQueryExpressionToJpaVisitor.registerOperatorVisitor(Operator.GT, new GTOperatorBinaryQueryExpressionToJpaVisitor());
	}

	@Override
	protected <T> Predicate convert(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb,
			QueryExpression expression) {
		BinaryQueryExpression binaryExpression = (BinaryQueryExpression)expression;
		
		return cb.gt(root.get(binaryExpression.getLeftOperand()), Integer.parseInt(binaryExpression.getRightOperand()));
	}
}
