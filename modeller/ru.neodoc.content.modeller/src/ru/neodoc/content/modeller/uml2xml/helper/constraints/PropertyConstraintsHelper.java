package ru.neodoc.content.modeller.uml2xml.helper.constraints;

import org.alfresco.model.dictionary._1.Property.Constraints;
import org.eclipse.uml2.uml.Property;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelperForContainerSingleton;
import ru.neodoc.content.modeller.uml2xml.helper.classes.PropertyHelper;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class PropertyConstraintsHelper extends AbstractSubHelperForContainerSingleton<
			Property, 
			org.alfresco.model.dictionary._1.Property, 
			Constraints> {

	static {
		HELPER_REGISTRY.register(PropertyConstraintsHelper.class, PropertyHelper.class);
	}
	
	@Override
	protected Constraints getSingleObjectFromContainer(org.alfresco.model.dictionary._1.Property container) {
		return container.getConstraints();
	}

	@Override
	protected void addSingleObjectToContainer(Constraints object, org.alfresco.model.dictionary._1.Property container) {
		container.setConstraints(object);
	}

	@Override
	protected Constraints doCreateObject(Property element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createPropertyConstraints();
	}

}
