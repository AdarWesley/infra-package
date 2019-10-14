package org.awesley.infra;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.awesley.infra.query.AndQueryExpression;
import org.awesley.infra.query.BinaryQueryExpression;
import org.awesley.infra.query.NotQueryExpression;
import org.awesley.infra.query.Operator;
import org.awesley.infra.query.OrQueryExpression;
import org.awesley.infra.query.QueryExpression;
import org.awesley.infra.query.parser.QueryExpressionParser;
import org.junit.BeforeClass;
import org.junit.Test;

public class QueryExpressionParserTest {
	
	// private static QueryExpressionParser parser;

	private static Map<String, Operator> expressionToOpMap;
	
	@BeforeClass
	public static void setupClass() {
		expressionToOpMap = Stream.of(new Object[][] { 
		    { "aa=b", Operator.EQUALS }, 
		    { "aa<b", Operator.LT }, 
		    { "aa<=b", Operator.LE }, 
		    { "aa>=b", Operator.GE }, 
		    { "aa>b", Operator.GT }, 
		    { "aa%b", Operator.LIKE }, 
		}).collect(Collectors.toMap(data -> (String) data[0], data -> (Operator) data[1]));
	}
	
	@Test
	public void shouldParseExpressions() {
		for (Entry<String, Operator> exp : expressionToOpMap.entrySet()) {
			QueryExpressionParser parser = new QueryExpressionParser(exp.getKey());
			parser.Parse();
			QueryExpression expression = parser.getQueryExpression();
			assertBinaryExpression(expression, exp.getValue(), "aa", "b");
		}
	}
	
	@Test
	public void shouldParseAndExpression() {
		QueryExpressionParser parser = new QueryExpressionParser("aa=b,c<dd");
		parser.Parse();
		QueryExpression expression = parser.getQueryExpression();
		
		assertNotNull(expression);
		assertTrue(AndQueryExpression.class.isAssignableFrom(expression.getClass()));
		assertEquals(2, ((AndQueryExpression)expression).getSubQueryExpressions().size());
		assertBinaryExpression(((AndQueryExpression)expression).getSubQueryExpressions().get(0), Operator.EQUALS, "aa", "b");
		assertBinaryExpression(((AndQueryExpression)expression).getSubQueryExpressions().get(1), Operator.LT, "c", "dd");
	}
	
	@Test
	public void shouldParseAndandNotExpression() {
		QueryExpressionParser parser = new QueryExpressionParser("aa=b,!c<dd");
		parser.Parse();
		QueryExpression expression = parser.getQueryExpression();
		
		assertNotNull(expression);
		assertTrue(AndQueryExpression.class.isAssignableFrom(expression.getClass()));
		assertEquals(2, ((AndQueryExpression)expression).getSubQueryExpressions().size());
		assertBinaryExpression(((AndQueryExpression)expression).getSubQueryExpressions().get(0), Operator.EQUALS, "aa", "b");
		assertTrue(NotQueryExpression.class.isAssignableFrom(((AndQueryExpression)expression).getSubQueryExpressions().get(1).getClass()));
		NotQueryExpression notSubExpression = (NotQueryExpression)((AndQueryExpression)expression).getSubQueryExpressions().get(1);
		assertBinaryExpression(notSubExpression.getQueryExpression(), Operator.LT, "c", "dd");
	}
	
	@Test
	public void shouldParseMultipleSubExpressions() {
		QueryExpressionParser parser = new QueryExpressionParser("(aa=b,c<dd;(ee%fff;(gg>=hh,ii=jj);kk=ll;(mm=nn))),o=ppp");
		parser.Parse();
		QueryExpression expression = parser.getQueryExpression();
		
		assertNotNull(expression);
		assertTrue(AndQueryExpression.class.isAssignableFrom(expression.getClass()));
		assertEquals(2, ((AndQueryExpression)expression).getSubQueryExpressions().size());
		QueryExpression expLeftParen = ((AndQueryExpression)expression).getSubQueryExpressions().get(0);
		assertTrue(OrQueryExpression.class.isAssignableFrom(expLeftParen.getClass()));
		assertEquals(2, ((OrQueryExpression)expLeftParen).getSubQueryExpressions().size());
		QueryExpression andAAtoDD = ((OrQueryExpression)expLeftParen).getSubQueryExpressions().get(0);
		assertEquals(2, ((AndQueryExpression)andAAtoDD).getSubQueryExpressions().size());
		assertBinaryExpression(((AndQueryExpression)andAAtoDD).getSubQueryExpressions().get(0), Operator.EQUALS, "aa", "b");
		assertBinaryExpression(((AndQueryExpression)andAAtoDD).getSubQueryExpressions().get(1), Operator.LT, "c", "dd");
		QueryExpression orEEtoNN = ((OrQueryExpression)expLeftParen).getSubQueryExpressions().get(1);
		assertTrue(OrQueryExpression.class.isAssignableFrom(orEEtoNN.getClass()));
		assertEquals(4, ((OrQueryExpression)orEEtoNN).getSubQueryExpressions().size());
		assertBinaryExpression(((OrQueryExpression)orEEtoNN).getSubQueryExpressions().get(0), Operator.LIKE, "ee", "fff");
		QueryExpression andGGtoJJ = ((OrQueryExpression)orEEtoNN).getSubQueryExpressions().get(1);
		assertTrue(AndQueryExpression.class.isAssignableFrom(andGGtoJJ.getClass()));
		assertEquals(2, ((AndQueryExpression)andGGtoJJ).getSubQueryExpressions().size());
		assertBinaryExpression(((AndQueryExpression)andGGtoJJ).getSubQueryExpressions().get(0), Operator.GE, "gg", "hh");
		assertBinaryExpression(((AndQueryExpression)andGGtoJJ).getSubQueryExpressions().get(1), Operator.EQUALS, "ii", "jj");
		assertBinaryExpression(((OrQueryExpression)orEEtoNN).getSubQueryExpressions().get(2), Operator.EQUALS, "kk", "ll");
		assertBinaryExpression(((OrQueryExpression)orEEtoNN).getSubQueryExpressions().get(3), Operator.EQUALS, "mm", "nn");
		assertBinaryExpression(((AndQueryExpression)expression).getSubQueryExpressions().get(1), Operator.EQUALS, "o", "ppp");
	}
	
	private void assertBinaryExpression(QueryExpression expression, Operator operator, String expectedLeft,
			String expectedRight) {
		assertNotNull(expression);
		assertTrue(BinaryQueryExpression.class.isAssignableFrom(expression.getClass()));
		assertEquals(operator, ((BinaryQueryExpression)expression).getOperator());
		assertEquals(expectedLeft, ((BinaryQueryExpression)expression).getLeftOperand());
		assertEquals(expectedRight, ((BinaryQueryExpression)expression).getRightOperand());
	}
}
