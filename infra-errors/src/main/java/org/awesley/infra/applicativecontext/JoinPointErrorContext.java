package org.awesley.infra.applicativecontext;

import org.aspectj.lang.JoinPoint;

public class JoinPointErrorContext {
	private JoinPoint joinPoint;
	private Throwable exception;
	
	public JoinPointErrorContext(JoinPoint joinPoint, Throwable exception) {
		super();
		this.joinPoint = joinPoint;
		this.exception = exception;
	}

	public JoinPoint getJoinPoint() {
		return joinPoint;
	}

	public Throwable getException() {
		return exception;
	}
}
