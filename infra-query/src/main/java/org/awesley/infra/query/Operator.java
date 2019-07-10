package org.awesley.infra.query;

import java.util.HashMap;
import java.util.Map;

public enum Operator {
	EQUALS("="), LT("<"), LE("<="), GE(">="), GT(">"), LIKE("%");
	
	private static final Map<String, Operator> BY_OP = new HashMap<>();
	
	static {
		for (Operator operator : values()) {
			BY_OP.put(operator.op, operator);
		}
	}
	
	public final String op;
	
	private Operator(String op) {
		this.op = op;
	}
	
    public static Operator valueOfOp(String op) {
        return BY_OP.get(op);
    }

	public static String getRegExp() {
		StringBuilder b = new StringBuilder();
		for (Operator operator : values()) {
			if (b.length() != 0) {
				b.append("|");
			}
			b.append(operator.op);
		}
		return b.toString();
	}
}
