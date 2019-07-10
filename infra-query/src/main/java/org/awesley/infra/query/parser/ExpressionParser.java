package org.awesley.infra.query.parser;

import org.awesley.infra.query.QueryExpression;

public abstract class ExpressionParser {

	protected String stringExpression;
	protected int startOffset;
	protected int currentOffset;
	
	public ExpressionParser(String ex, int startOffset) {
		this.stringExpression = ex;
		this.startOffset = startOffset;
		this.currentOffset = startOffset;
	}
	
	public String getStringExpression() {
		return stringExpression;
	}

	public void setStringExpression(String stringExpression) {
		this.stringExpression = stringExpression;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	public int getCurrentOffset() {
		return currentOffset;
	}

	public void setCurrentOffset(int currentOffset) {
		this.currentOffset = currentOffset;
	}

	public abstract QueryExpression getQueryExpression();
	
	protected void SkipWhitespace() {
		for (; currentOffset < stringExpression.length() && 
			   Character.isWhitespace(stringExpression.charAt(currentOffset)); 
				currentOffset++) {
		}
	}

	protected boolean isLookAhead(char lookAheadChar) {
		if (currentOffset >= stringExpression.length() || currentOffset < 0) {
			return false;
		}
		return stringExpression.charAt(currentOffset) == lookAheadChar;
	}

}
