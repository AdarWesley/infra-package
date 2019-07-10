package org.awesley.infra.contracttesting;

import java.util.Map;

import org.springframework.test.context.TestContext;

public interface ContractTestHelper<BASETESTType> {
	void beforeTestClass(ContractTestsExecutionListener executionListener, TestContext testContext) throws Exception;
	void prepareTestInstance(ContractTestsExecutionListener executionListener, TestContext testContext) throws Exception;
	void beforeTestMethod(ContractTestsExecutionListener executionListener, TestContext testContext) throws Exception;
	void afterTestMethod(ContractTestsExecutionListener executionListener, TestContext testContext) throws Exception;
	void afterTestClass(ContractTestsExecutionListener executionListener, TestContext testContext) throws Exception;
	void doValidate(Map<String, Object> response) throws Exception;
	void setTestInstance(BASETESTType testInstance);
	BASETESTType getTestInstance();
}
