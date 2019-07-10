package org.awesley.infra.contracttesting;

import java.util.Map;

import org.springframework.test.context.TestContext;

public abstract class AbstractContractTestHelper<BASETESTType extends SupportsTestHelper<BASETESTType>> 
	implements ContractTestHelper<BASETESTType> {

	private BASETESTType testInstance;

	@Override
	public void beforeTestClass(ContractTestsExecutionListener executionListener, TestContext testContext) throws Exception {
		/* no-op */
	}

	@SuppressWarnings("unchecked")
	@Override
	public void prepareTestInstance(ContractTestsExecutionListener executionListener, TestContext testContext) throws Exception {
		((BASETESTType)testContext.getTestInstance()).setContractTestsHelper(this);
		this.setTestInstance((BASETESTType)testContext.getTestInstance());
	}

	@Override
	public void setTestInstance(BASETESTType testInstance) {
		this.testInstance = testInstance;
	}

	@Override
	public BASETESTType getTestInstance() {
		return testInstance;
	}

	@Override
	public void beforeTestMethod(ContractTestsExecutionListener executionListener, TestContext testContext) throws Exception {
		/* no-op */
	}

	@Override
	public void afterTestMethod(ContractTestsExecutionListener executionListener, TestContext testContext) throws Exception {
		/* no-op */
	}

	@Override
	public void afterTestClass(ContractTestsExecutionListener executionListener, TestContext testContext) throws Exception {
		/* no-op */
	}
	
	@Override
	public void doValidate(Map<String, Object> response) throws Exception {
		/* no-op */
	}
}
