package ru.neodoc.content.modeller.uml2xml.helper.constraints;

import java.util.List;

import org.alfresco.model.dictionary._1.PropertyOverride.Constraints;
import org.eclipse.uml2.uml.Dependency;

import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;

public class PropertyOverrideConstraintHelper
		extends ConstraintOwnedHelper<Dependency, org.alfresco.model.dictionary._1.PropertyOverride.Constraints> {

	static {
		HELPER_REGISTRY.register(PropertyOverrideConstraintHelper.class, PropertyOverrideConstraintsHelper.class).asContained();
	}
	
	@Override
	protected List<ObjectContainer<org.alfresco.model.dictionary._1.Constraint>> addObjectsToContainer(
			ObjectContainer<Constraints> container,
			List<ObjectContainer<org.alfresco.model.dictionary._1.Constraint>> objectsToAdd) {
		getObjects(container.getObject()).addAll(ObjectContainer.FACTORY.extractTyped(objectsToAdd));
		return ObjectContainer.FACTORY.createListTyped(getObjects(container.getObject()));
	}

	@Override
	protected List<org.alfresco.model.dictionary._1.Constraint> getOrCreateObjects(Constraints container) {
		return getObjects(container);
	}

	@Override
	public List<org.alfresco.model.dictionary._1.Constraint> getObjects(Constraints container) {
		return container.getConstraint();
	}

}
