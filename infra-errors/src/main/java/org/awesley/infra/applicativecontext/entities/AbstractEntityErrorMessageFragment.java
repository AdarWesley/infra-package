package org.awesley.infra.applicativecontext.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AbstractEntityErrorMessageFragment implements IEntityErrorMessageFragment {

	@Autowired
	@Qualifier("default")
	IEntityErrorMessageFragment defaultEntityErrorMessageFragment;
	
	public String create(String entityType, long entityId) {
		return defaultEntityErrorMessageFragment.create(entityType, entityId);
	}
	
	public abstract String getEntityType();

	public String create(long entityId) {
		return create(getEntityType(), entityId);
	}
}
