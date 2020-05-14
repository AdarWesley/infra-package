package org.awesley.infra.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public class LogWrapper {

	private Class<?> logClass;
	private Method instanceMethod;
	private Method flushMethod;
	private Object logObject;
	private Messager messager;

	public LogWrapper(Object context, Messager messager) {
		logClass = getLogClass();
		instanceMethod = getInstanceMethod(logClass);
		flushMethod = getFlushMethod(logClass);
		logObject = getLogObject(context, logClass);
		this.messager = messager;
	}

	private static Class<?> getLogClass() {
		Class<?> logClass = null;
		try {
			logClass = Class.forName("com.sun.tools.javac.util.Log");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return logClass;
	}

	private Method getInstanceMethod(Class<?> logClass) {
		Method instanceMethod = null;
		for (Method m : logClass.getMethods()) {
			if (m.getName() == "instance") {
				instanceMethod = m;
				break;
			}
		}
		return instanceMethod;
	}

	private Method getFlushMethod(Class<?> logClass) {
		Method flushMethod = null;
		try {
			flushMethod = logClass.getMethod("flush");
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flushMethod;
	}
	
	private Object getLogObject(Object context, Class<?> logClass) {
		Object logObject = null;
		try {
			logObject = instanceMethod.invoke(null, context);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			e.printStackTrace();
		}
		return logObject;
	}

	public void flush() {
		try {
			flushMethod.invoke(logObject);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public void printMessage(Kind kind, String msg) {
		messager.printMessage(kind, msg);
		flush();
	}

	public void printMessage(Kind kind, String msg, Element element) {
		messager.printMessage(kind, msg, element);
	}
}
