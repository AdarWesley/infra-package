package org.awesley.infra.applicativecontext;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.awesley.infra.errors.ApplicationException;
import org.awesley.infra.errors.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class ApplicativeContextAspect {
	
	private static IErrorInfoCreator errorInfoCreator;
	
	public IErrorInfoCreator getErrorInfoCreator() {
		return errorInfoCreator;
	}

	@Autowired
	public void setErrorInfoCreator(IErrorInfoCreator errorInfoCreator) {
		ApplicativeContextAspect.errorInfoCreator = errorInfoCreator;
	}

	public ApplicativeContextAspect() {
		super();
	}
	
	@Pointcut("execution(* org.awesley.digital..resources.implementation.delegate..*(..))") 
	public void resourceDelegate(){
		
	}
	
	@Before("resourceDelegate()")
	public void beforeResourceDelegate(JoinPoint jp) {
		System.out.println("XXXXX: " + jp.getSignature().getDeclaringTypeName() + " XXXXX");
		ApplicativeContextEntities.clear();
	}
	
	@AfterThrowing(pointcut = "resourceDelegate()", throwing = "ex")
	public void afterThrowingResourceDelegate(JoinPoint jp, Throwable ex) {
		ErrorInfo errorInfo = errorInfoCreator.createErrorInfo(jp, ex);
		ApplicativeContextEntities.clear();
		throw new ApplicationException(errorInfo, ex);
	}

	@AfterReturning(pointcut = "resourceDelegate()")
	public void afterResourceDelegate(JoinPoint jp) {
		ApplicativeContextEntities.clear();
	}
}
