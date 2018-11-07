package ru.neodoc.content.modeller.uml2xml.helper.classes;

import org.alfresco.model.dictionary._1.Class.Properties;
import org.eclipse.uml2.uml.Class;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelperForContainerSingleton;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class PropertiesHelper extends 
		AbstractSubHelperForContainerSingleton<
			Class, 
			org.alfresco.model.dictionary._1.Class,
			org.alfresco.model.dictionary._1.Class.Properties> {
	
	static {
		HELPER_REGISTRY.register(PropertiesHelper.class, TypeHelper.class);
		HELPER_REGISTRY.register(PropertiesHelper.class, AspectHelper.class);
	}

	@Override
	protected Properties getSingleObjectFromContainer(org.alfresco.model.dictionary._1.Class container) {
		return container.getProperties();
	}

	@Override
	protected void addSingleObjectToContainer(Properties object, org.alfresco.model.dictionary._1.Class container) {
		container.setProperties(object);
	}

	@Override
	protected Properties doCreateObject(Class element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createClassProperties();
	}

	
	
}
