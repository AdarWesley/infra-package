package org.awesley.infra.contracttesting;

public interface SupportsTestHelper<BASETESTType> {
	ContractTestHelper<BASETESTType> getContractTestHelper();
	void setContractTestsHelper(ContractTestHelper<BASETESTType> contractTestHelper);
}
