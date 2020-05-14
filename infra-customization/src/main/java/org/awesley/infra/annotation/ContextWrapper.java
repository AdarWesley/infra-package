package org.awesley.infra.annotation;

import javax.annotation.processing.Messager;

public class ContextWrapper {

	private Object context;

	public ContextWrapper(Object context) {
		this.context = context;
	}

	public OptionsWrapper getOptionsWrapper() {
		return new OptionsWrapper(this.context);
	}

	public LogWrapper getLogWrapper(Messager messager) {
		return new LogWrapper(this.context, messager);
	}
}
