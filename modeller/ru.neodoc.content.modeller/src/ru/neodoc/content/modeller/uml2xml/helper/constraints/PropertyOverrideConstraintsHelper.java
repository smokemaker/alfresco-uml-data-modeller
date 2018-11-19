package ru.neodoc.content.modeller.uml2xml.helper.constraints;

import org.alfresco.model.dictionary._1.PropertyOverride;
import org.alfresco.model.dictionary._1.PropertyOverride.Constraints;
import org.eclipse.uml2.uml.Dependency;
import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelperForContainerSingleton;
import ru.neodoc.content.modeller.uml2xml.helper.classes.PropertyOverrideHelper;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class PropertyOverrideConstraintsHelper 
		extends AbstractSubHelperForContainerSingleton<
			Dependency,
			PropertyOverride,
			Constraints
		> {

	static {
		HELPER_REGISTRY.register(PropertyOverrideConstraintsHelper.class, PropertyOverrideHelper.class);
	}

	@Override
	protected Constraints getSingleObjectFromContainer(PropertyOverride container) {
		return container.getConstraints();
	}

	@Override
	protected void addSingleObjectToContainer(Constraints object, PropertyOverride container) {
		container.setConstraints(object);
	}

	@Override
	protected Constraints doCreateObject(Dependency element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createPropertyOverrideConstraints();
	}

}
