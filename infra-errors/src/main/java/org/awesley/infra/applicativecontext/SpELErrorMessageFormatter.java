package org.awesley.infra.applicativecontext;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.aspectj.lang.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpELErrorMessageFormatter implements IErrorMessageFormatter {
	
	@Autowired
	ResourceBundle errorFormatResources;
	
	@Autowired
	ApplicationContext ctx;
	
	public String formatErrorMessage(JoinPointErrorContext jpec) {
		String methodName = getMethodName(jpec);
		
		ExpressionParser parser = new SpelExpressionParser();
		String errorMessageExpression = getErrorMessageExpression(methodName);
		Expression expr = parser.parseExpression(errorMessageExpression);

		StandardEvaluationContext context = initializeContext(jpec);

		return expr.getValue(context, String.class);
	}

	private StandardEvaluationContext initializeContext(JoinPointErrorContext jpec) {
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("ctx", jpec);
		context.setVariable("args", jpec.getJoinPoint().getArgs());
		context.setBeanResolver(new BeanResolver() {
			
			public Object resolve(EvaluationContext evalCtx, String beanName) throws AccessException {
				return ctx.getBean(beanName);
			}
		});
		return context;
	}

	private String getMethodName(JoinPointErrorContext jpec) {
		Signature sig = jpec.getJoinPoint().getSignature();
		String methodName = sig.getDeclaringType().getSimpleName() + "." + sig.getName();
		return methodName;
	}

	private String getErrorMessageExpression(String methodName) {
		String errorMessageExpression;
		try {
			errorMessageExpression = errorFormatResources.getString(methodName);
		}
		catch (MissingResourceException ex) {
			errorMessageExpression = errorFormatResources.getString("DefaultErrorFormat");
		}
		
		return errorMessageExpression;
	}

}
