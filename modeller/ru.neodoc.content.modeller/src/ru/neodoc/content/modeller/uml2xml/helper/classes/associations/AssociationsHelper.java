package ru.neodoc.content.modeller.uml2xml.helper.classes.associations;

import org.alfresco.model.dictionary._1.Class;
import org.alfresco.model.dictionary._1.Class.Associations;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelperForContainerSingleton;
import ru.neodoc.content.modeller.uml2xml.helper.classes.AspectHelper;
import ru.neodoc.content.modeller.uml2xml.helper.classes.TypeHelper;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class AssociationsHelper extends AbstractSubHelperForContainerSingleton<
			org.eclipse.uml2.uml.Class,
			org.alfresco.model.dictionary._1.Class,
			org.alfresco.model.dictionary._1.Class.Associations
		> {

	static {
		HELPER_REGISTRY.register(AssociationsHelper.class, TypeHelper.class);
		HELPER_REGISTRY.register(AssociationsHelper.class, AspectHelper.class);
	}
	
	@Override
	protected Associations getSingleObjectFromContainer(Class container) {
		return container.getAssociations();
	}

	@Override
	protected void addSingleObjectToContainer(Associations object, Class container) {
		container.setAssociations(object);
	}

	@Override
	protected Associations doCreateObject(org.eclipse.uml2.uml.Class element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createClassAssociations();
	}

	
	
}
