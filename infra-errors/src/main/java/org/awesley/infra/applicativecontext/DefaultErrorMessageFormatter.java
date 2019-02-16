package org.awesley.infra.applicativecontext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class DefaultErrorMessageFormatter implements IErrorMessageFormatter {

	@Autowired
	@Qualifier("methodName")
	private IErrorMessageFragment methodName;
	
	@Autowired
	@Qualifier("contextEntities")
	private IErrorMessageFragment contextEntities;
	
	@Autowired
	@Qualifier("innerError")
	private IErrorMessageFragment innerError;
	
	public String formatErrorMessage(JoinPointErrorContext jpec) {
		String message = String.format("While executing %1$s on %2$s received error: %3$s", 
				methodName.create(jpec), contextEntities.create(jpec), innerError.create(jpec));
		return message;
	}

}
