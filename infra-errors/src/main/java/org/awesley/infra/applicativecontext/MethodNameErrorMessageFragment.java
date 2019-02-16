package org.awesley.infra.applicativecontext;

import org.aspectj.lang.Signature;

public class MethodNameErrorMessageFragment implements IErrorMessageFragment {

	public String create(JoinPointErrorContext jpec) {
		Signature sig = jpec.getJoinPoint().getSignature();
		return sig.getDeclaringType().getSimpleName() + "." + sig.getName();
	}

}
