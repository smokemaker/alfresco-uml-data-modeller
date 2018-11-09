package ru.neodoc.content.modeller.uml2xml.helper.classes;

import org.alfresco.model.dictionary._1.Class;
import org.alfresco.model.dictionary._1.Class.Overrides;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelperForContainerSingleton;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class OverridesHelper extends AbstractSubHelperForContainerSingleton<
			org.eclipse.uml2.uml.Class,
			org.alfresco.model.dictionary._1.Class,
			org.alfresco.model.dictionary._1.Class.Overrides
		> {

	static {
		HELPER_REGISTRY.register(OverridesHelper.class, TypeHelper.class);
		HELPER_REGISTRY.register(OverridesHelper.class, AspectHelper.class);
	}

	@Override
	protected Overrides getSingleObjectFromContainer(Class container) {
		return container.getOverrides();
	}

	@Override
	protected void addSingleObjectToContainer(Overrides object, Class container) {
		container.setOverrides(object);
	}

	@Override
	protected Overrides doCreateObject(org.eclipse.uml2.uml.Class element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createClassOverrides();
	}
	
	
}
