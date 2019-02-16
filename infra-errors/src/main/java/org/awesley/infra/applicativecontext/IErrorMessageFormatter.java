package org.awesley.infra.applicativecontext;

public interface IErrorMessageFormatter {
	String formatErrorMessage(JoinPointErrorContext jpec);
}
