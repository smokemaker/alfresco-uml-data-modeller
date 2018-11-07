package ru.neodoc.content.modeller.uml2xml.helper.classes;

import org.alfresco.model.dictionary._1.Class;
import org.alfresco.model.dictionary._1.Class.MandatoryAspects;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelperForContainerSingleton;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class MandatoryAspectsHelper extends 
		AbstractSubHelperForContainerSingleton<
			org.eclipse.uml2.uml.Class, 
			org.alfresco.model.dictionary._1.Class,
			org.alfresco.model.dictionary._1.Class.MandatoryAspects> {

	static {
		HELPER_REGISTRY.register(MandatoryAspectsHelper.class, TypeHelper.class);
		HELPER_REGISTRY.register(MandatoryAspectsHelper.class, AspectHelper.class);
	}

	@Override
	protected MandatoryAspects getSingleObjectFromContainer(Class container) {
		return container.getMandatoryAspects();
	}

	@Override
	protected void addSingleObjectToContainer(MandatoryAspects object, Class container) {
		container.setMandatoryAspects(object);
		
	}

	@Override
	protected MandatoryAspects doCreateObject(org.eclipse.uml2.uml.Class element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createClassMandatoryAspects();
	}
	
	
}
