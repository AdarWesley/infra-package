package org.awesley.infra.applicativecontext;

import org.aspectj.lang.JoinPoint;
import org.awesley.infra.errors.ErrorInfo;

public interface IErrorInfoCreator {

	ErrorInfo createErrorInfo(JoinPoint jp, Throwable ex);

}