package org.awesley.infra.applicativecontext;

import java.util.ArrayList;
import java.util.Collection;

import org.awesley.infra.applicativecontext.entities.IEntityErrorMessageFragment;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ConfigurableApplicationContext;

public class ContextEntitiesErrorMessageFragment implements IErrorMessageFragment {

	@Autowired
	private ConfigurableApplicationContext ctx;
	
	public String create(JoinPointErrorContext jpec) {
		Collection<ArrayList<ContextEntityInfo>> contextEntities = ApplicativeContextEntities.getContextEntities();
		if (contextEntities == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for (ArrayList<ContextEntityInfo> list : contextEntities) {
			for (ContextEntityInfo entityInfo : list) {
				IEntityErrorMessageFragment eemf = getEntityErrorMessageFragmentBean(entityInfo);
				sb.append(eemf.create(entityInfo.getEntityType(), entityInfo.getId()));
			}
		}
		
		return sb.toString();
	}

	private IEntityErrorMessageFragment getEntityErrorMessageFragmentBean(ContextEntityInfo entityInfo) {
		IEntityErrorMessageFragment eemf = null;
		try {
			eemf = BeanFactoryAnnotationUtils.qualifiedBeanOfType(ctx.getBeanFactory(), IEntityErrorMessageFragment.class, entityInfo.getEntityType());
		}
		catch (NoSuchBeanDefinitionException ex) { // suppress exception if not found, eemf is still null
		}
		
		if (eemf == null) {
			eemf = BeanFactoryAnnotationUtils.qualifiedBeanOfType(ctx.getBeanFactory(), IEntityErrorMessageFragment.class, "default");
		}
		
		return eemf;
	}
}
