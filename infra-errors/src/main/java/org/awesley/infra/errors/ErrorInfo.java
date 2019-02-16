package org.awesley.infra.errors;

public class ErrorInfo {
	private long errorCode;
	private String message;
	ErrorInfo[] innerErrors;
	
	public ErrorInfo(long errorCode, String message, ErrorInfo innerError) {
		super();
		this.errorCode = errorCode;
		this.message = message;
		this.innerErrors = new ErrorInfo[] { innerError };
	}
	
	public long getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	public ErrorInfo[] getInnerErrors() {
		return innerErrors;
	}
}
