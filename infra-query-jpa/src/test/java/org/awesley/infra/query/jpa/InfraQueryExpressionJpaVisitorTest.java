package org.awesley.infra.query.jpa;

import static org.junit.Assert.*;

import java.util.List;

import org.awesley.infra.query.BinaryQueryExpression;
import org.awesley.infra.query.Operator;
import org.awesley.infra.query.QueryExpression;
import org.awesley.infra.query.jpa.entities.Entity1;
import org.awesley.infra.query.jpa.repositories.Entity1Repository;
import org.awesley.infra.query.parser.QueryExpressionParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {	InfraQueryJpaTestsConfig.class })
public class InfraQueryExpressionJpaVisitorTest {
	
	@Autowired
	private Entity1Repository entity1Repository;
	
	@Test
	public void canQueryEqualsExpression() {
		QueryExpression expression = new BinaryQueryExpression("name", Operator.EQUALS, "Entity1");
		List<? extends Entity1> entities = entity1Repository.findByQueryExpression(expression, 0, 1);
		
		assertNotNull(entities);
		assertEquals(1, entities.size());
		assertEquals((long)1L, (long)entities.get(0).getID());
	}
	
	@Test
	public void canQueryWithLessThanExpression() {
		QueryExpressionParser parser = new QueryExpressionParser("id<2");
		parser.Parse();
		QueryExpression expression = parser.getQueryExpression();
		
		List<? extends Entity1> entities = entity1Repository.findByQueryExpression(expression, 0, 2);
		
		assertNotNull(entities);
		assertEquals(1, entities.size());
		assertEquals((long)1L, (long)entities.get(0).getID());
	}
	
	@Test
	public void canQueryWithLessEqualExpression() {
		QueryExpressionParser parser = new QueryExpressionParser("id<=2");
		parser.Parse();
		QueryExpression expression = parser.getQueryExpression();
		
		List<? extends Entity1> entities = entity1Repository.findByQueryExpression(expression, 0, 3);
		
		assertNotNull(entities);
		assertEquals(2, entities.size());
		assertEquals((long)1L, (long)entities.get(0).getID());
		assertEquals((long)2L, (long)entities.get(1).getID());
	}
	
	@Test
	public void canQueryWithGreaterThanExpression() {
		QueryExpressionParser parser = new QueryExpressionParser("id>2");
		parser.Parse();
		QueryExpression expression = parser.getQueryExpression();
		
		List<? extends Entity1> entities = entity1Repository.findByQueryExpression(expression, 0, 2);
		
		assertNotNull(entities);
		assertEquals(1, entities.size());
		assertEquals((long)3L, (long)entities.get(0).getID());
	}
	
	@Test
	public void canQueryWithGreaterEqualExpression() {
		QueryExpressionParser parser = new QueryExpressionParser("id>=2");
		parser.Parse();
		QueryExpression expression = parser.getQueryExpression();
		
		List<? extends Entity1> entities = entity1Repository.findByQueryExpression(expression, 0, 3);
		
		assertNotNull(entities);
		assertEquals(2, entities.size());
		assertEquals((long)2L, (long)entities.get(0).getID());
		assertEquals((long)3L, (long)entities.get(1).getID());
	}
	
	@Test
	public void canQueryWithLikeExpression() {
		QueryExpressionParser parser = new QueryExpressionParser("name%Entity");
		parser.Parse();
		QueryExpression expression = parser.getQueryExpression();
		
		List<? extends Entity1> entities = entity1Repository.findByQueryExpression(expression, 0, 3);
		
		assertNotNull(entities);
		assertEquals(2, entities.size());
		assertEquals((long)1L, (long)entities.get(0).getID());
		assertEquals((long)3L, (long)entities.get(1).getID());
	}
	
	@Test
	public void canQueryWithNotLikeExpression() {
		QueryExpressionParser parser = new QueryExpressionParser("!name%Entity");
		parser.Parse();
		QueryExpression expression = parser.getQueryExpression();
		
		List<? extends Entity1> entities = entity1Repository.findByQueryExpression(expression, 0, 3);
		
		assertNotNull(entities);
		assertEquals(1, entities.size());
		assertEquals((long)2L, (long)entities.get(0).getID());
	}
	
	@Test
	public void canQueryWithAndExpression() {
		QueryExpressionParser parser = new QueryExpressionParser("id<=2,!name%Entity");
		parser.Parse();
		QueryExpression expression = parser.getQueryExpression();
		
		List<? extends Entity1> entities = entity1Repository.findByQueryExpression(expression, 0, 3);
		
		assertNotNull(entities);
		assertEquals(1, entities.size());
		assertEquals((long)2L, (long)entities.get(0).getID());
	}
	
	@Test
	public void canQueryWithOrExpression() {
		QueryExpressionParser parser = new QueryExpressionParser("id<2;!name%Entity");
		parser.Parse();
		QueryExpression expression = parser.getQueryExpression();
		
		List<? extends Entity1> entities = entity1Repository.findByQueryExpression(expression, 0, 3);
		
		assertNotNull(entities);
		assertEquals(2, entities.size());
		assertEquals((long)1L, (long)entities.get(0).getID());
		assertEquals((long)2L, (long)entities.get(1).getID());
	}
	
	@Test
	public void canQueryWithComplexExpression() {
		QueryExpressionParser parser = new QueryExpressionParser("name%Entity,!(id<2;name=Entity1)");
		parser.Parse();
		QueryExpression expression = parser.getQueryExpression();
		
		List<? extends Entity1> entities = entity1Repository.findByQueryExpression(expression, 0, 3);
		
		assertNotNull(entities);
		assertEquals(1, entities.size());
		assertEquals((long)3L, (long)entities.get(0).getID());
	}
}
