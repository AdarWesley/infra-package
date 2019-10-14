package org.awesley.infra.query.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.awesley.infra.query.BinaryQueryExpression;
import org.awesley.infra.query.Operator;
import org.awesley.infra.query.QueryExpression;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BinaryQueryExpressionToJpaVisitor extends QueryExpressionToJpaVisitor {

	static {
		QueryExpressionToJpaVisitor.registerVisitor(BinaryQueryExpression.class, new BinaryQueryExpressionToJpaVisitor());
	}

	private static Map<Operator, QueryExpressionToJpaVisitor> operatorVisitorMap = 
			new HashMap<Operator, QueryExpressionToJpaVisitor>();
	
	@Override
	protected <T> Predicate convert(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb,
			QueryExpression expression) {
		BinaryQueryExpression binaryQueryExpression = (BinaryQueryExpression)expression;
		
		QueryExpressionToJpaVisitor operatorVisitor = operatorVisitorMap.get(binaryQueryExpression.getOperator());
		
		return operatorVisitor.convert(root, query, cb, binaryQueryExpression);
	}

	public static void registerOperatorVisitor(Operator operator,
			BinaryQueryExpressionToJpaVisitor binaryQueryExpressionToJpaVisitor) {
		operatorVisitorMap.put(operator, binaryQueryExpressionToJpaVisitor);
	}

}
