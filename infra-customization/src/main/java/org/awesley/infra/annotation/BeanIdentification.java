package org.awesley.infra.annotation;

public class BeanIdentification {

	private String beanTypeName;
	private String beanName;

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getBeanTypeName() {
		return beanTypeName;
	}

	public void setBeanType(String beanTypeName) {
		this.beanTypeName = beanTypeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + ((beanName == null) ? 0 : beanName.hashCode());
		result = prime * result + ((beanTypeName == null) ? 0 : beanTypeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BeanIdentification)) {
			return false;
		}
		BeanIdentification other = (BeanIdentification) obj;
		if (beanName == null) {
			if (other.beanName != null) {
				return false;
			}
		} else if (!beanName.equals(other.beanName)) {
			return false;
		}
		if (beanTypeName == null) {
			if (other.beanTypeName != null) {
				return false;
			}
		} else if (!beanTypeName.equals(other.beanTypeName)) {
			return false;
		}
		return true;
	}

}
