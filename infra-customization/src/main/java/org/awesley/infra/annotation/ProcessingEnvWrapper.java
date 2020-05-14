package org.awesley.infra.annotation;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.processing.ProcessingEnvironment;

public class ProcessingEnvWrapper {

	private ProcessingEnvironment processingEnv;
	private Class<? extends ProcessingEnvironment> processingEnvClass;

	public ProcessingEnvWrapper(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		processingEnvClass = processingEnv.getClass();
	}

	public ProcessingEnvironment getProcessingEnv() {
		return processingEnv;
	}

	public void setProcessingEnv(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}

	ContextWrapper getContext() {
		Object context = null;
		try {
			context = processingEnvClass.getMethod("getContext").invoke(processingEnv);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
		return new ContextWrapper(context);
	}

}
