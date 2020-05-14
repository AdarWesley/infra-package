package org.awesley.infra.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OptionsWrapper {

	private Class<?> optionsClass;
	private Object options;
	private Method optionsGetMethod;

	public OptionsWrapper(Object context) {
		optionsClass = getOptionsClass();
		optionsGetMethod = getOptionsGetMethod(optionsClass);
		options = getOptionsInstance(context, optionsClass);
	}

	private static Class<?> getOptionsClass() {
		Class<?> optionsClass = null;
		try {
			optionsClass = Class.forName("com.sun.tools.javac.util.Options");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return optionsClass;
	}

	private static Object getOptionsInstance(Object context, Class<?> optionsClass) {
		Method instanceMethod = getInstanceMethod(optionsClass);
		Object options = getInstance(context, instanceMethod);
		return options;
	}

	private static Method getInstanceMethod(Class<?> optionsClass) {
		Method instanceMethod = null;
		for (Method m : optionsClass.getMethods()) {
			if (m.getName() == "instance") {
				instanceMethod = m;
				break;
			}
		}
		return instanceMethod;
	}

	private static Object getInstance(Object context, Method instanceMethod) {
		Object options = null;
		try {
			options = instanceMethod.invoke(null, context);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return options;
	}

	public String getOption(String optionName) {
		String optionValue = getOptionValue(options, optionName, optionsGetMethod);
		return optionValue;
	}

	private static Method getOptionsGetMethod(Class<?> optionsClass) {
		Method optionsGetMethod = null;
		try {
			optionsGetMethod = optionsClass.getMethod("get", String.class);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return optionsGetMethod;
	}

	private String getOptionValue(Object options, String optionName, Method optionsGetMethod) {
		String optionValue = null;
		try {
			optionValue = (String) optionsGetMethod.invoke(options, optionName);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return optionValue;
	}

}
