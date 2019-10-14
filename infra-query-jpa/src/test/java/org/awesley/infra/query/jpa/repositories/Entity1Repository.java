package org.awesley.infra.query.jpa.repositories;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.awesley.infra.query.QueryExpression;
import org.awesley.infra.query.jpa.QueryExpressionToJpaVisitor;
import org.awesley.infra.query.jpa.entities.Entity1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface Entity1Repository extends CrudRepository<Entity1, Long>, JpaSpecificationExecutor<Entity1> {

	default List<? extends Entity1> findByQueryExpression(QueryExpression expression, Integer startIndex, Integer pageSize){
		
		Specification<Entity1> querySpecification = new Specification<Entity1>() {

			@Override
			public Predicate toPredicate(Root<Entity1> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return QueryExpressionToJpaVisitor.baseConvert(root, query, cb, expression);
			}
			
		};
		
		Page<Entity1> page = findAll(querySpecification, new PageRequest(startIndex / pageSize, pageSize));
		return page.getContent();
	}
	
}
