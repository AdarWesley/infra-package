package org.awesley.infra.applicativecontext;

public class ContextEntityInfo {

	private String entityType;
	private long id;

	public ContextEntityInfo(String entityType, long id) {
		this.entityType = entityType;
		this.id = id;
	}
	
	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ContextEntityInfo [entityType=" + entityType + ", id=" + id + "]";
	}

}
