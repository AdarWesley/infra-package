package org.awesley.infra.errors;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = -4661379709129241303L;

	private ErrorInfo errorInfo;

	public ApplicationException(ErrorInfo errorInfo) {
		super(errorInfo.getMessage());
		this.errorInfo = errorInfo;
	}

	public ApplicationException(ErrorInfo errorInfo, Throwable cause) {
		super(errorInfo.getMessage(), cause);
		this.errorInfo = errorInfo;
	}

	public ErrorInfo getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(ErrorInfo errorInfo) {
		this.errorInfo = errorInfo;
	}
}
