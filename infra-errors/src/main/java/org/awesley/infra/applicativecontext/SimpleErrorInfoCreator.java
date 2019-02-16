package org.awesley.infra.applicativecontext;

import org.aspectj.lang.JoinPoint;
import org.awesley.infra.errors.ApplicationException;
import org.awesley.infra.errors.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;

public class SimpleErrorInfoCreator implements IErrorInfoCreator {

	@Autowired
	private IErrorMessageFormatter errorMessageFormatter;
	
	public SimpleErrorInfoCreator() {
	}

	public ErrorInfo createErrorInfo(JoinPoint jp, Throwable ex) {
		long errorCode = 0;
		ErrorInfo innerError = null;

		if (ApplicationException.class.isAssignableFrom(ex.getClass())) {
			ApplicationException aex = (ApplicationException)ex;
			innerError = aex.getErrorInfo();
			errorCode = innerError.getErrorCode();
		}
		
		String errorMessage = errorMessageFormatter.formatErrorMessage(new JoinPointErrorContext(jp, ex));
		return new ErrorInfo(errorCode, errorMessage, innerError);
	}
}
