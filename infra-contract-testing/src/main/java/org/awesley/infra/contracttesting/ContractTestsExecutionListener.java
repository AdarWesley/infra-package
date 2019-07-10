package org.awesley.infra.contracttesting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.TestPropertySourceUtils;

public class ContractTestsExecutionListener extends AbstractTestExecutionListener {

	ConfigurableApplicationContext ctx;
	ContractTestHelper<?> contractTestHelper;
	
	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		InitializeSpecificTestContextListener(testContext);
		if (contractTestHelper != null) {
			contractTestHelper.beforeTestClass(this, testContext);
		}
	}
	
	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception {
		if (contractTestHelper != null) {
			contractTestHelper.prepareTestInstance(this, testContext);
		}
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		if (contractTestHelper != null) {
			contractTestHelper.beforeTestMethod(this, testContext);
		}
	}
	
	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		if (contractTestHelper != null) {
			contractTestHelper.afterTestMethod(this, testContext);
		}
	}
	
	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
		if (contractTestHelper != null) {
			contractTestHelper.afterTestClass(this, testContext);
		}
	}
	
	private void InitializeSpecificTestContextListener(TestContext testContext) {
		ctx = (ConfigurableApplicationContext)testContext.getApplicationContext();
		String beanName = testContext.getTestClass().getName() + "Helper";
		if (ctx.containsBean(beanName)) {
			contractTestHelper = (ContractTestHelper<?>)ctx.getBean(beanName);
		}
		
		if (contractTestHelper != null) {
			String[] additionalProperties = loadPropertiesFromAnnotations(contractTestHelper.getClass());
			if (additionalProperties != null && additionalProperties.length > 0) {
				TestPropertySourceUtils.addInlinedPropertiesToEnvironment(ctx, additionalProperties);
			}
		}
	}
	
	private String[] loadPropertiesFromAnnotations(Class<?> annotatedClass) {
		TestPropertySource[] testPropertySrouces = annotatedClass.getAnnotationsByType(TestPropertySource.class);
		
		List<String> properties = new ArrayList<String>();
		
		for (TestPropertySource tps : testPropertySrouces){
			for (String prop : tps.properties()) {
				properties.add(prop);
			}
		}
		
		return properties.toArray(new String[0]);
	}
}
