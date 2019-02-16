package org.awesley.infra.applicativecontext;

import org.awesley.infra.errors.ApplicationException;
import org.awesley.infra.errors.ErrorInfo;

public class InnerErrorMessageFragment implements IErrorMessageFragment {

	public String create(JoinPointErrorContext jpec) {
		if (ApplicationException.class.isAssignableFrom(jpec.getException().getClass())) {
			ApplicationException aex = (ApplicationException)jpec.getException();
			ErrorInfo innerErrorInfo = aex.getErrorInfo();
			if (innerErrorInfo != null) {
				return innerErrorInfo.getMessage();
			}
		}
		return jpec.getException().getMessage();
	}

}
