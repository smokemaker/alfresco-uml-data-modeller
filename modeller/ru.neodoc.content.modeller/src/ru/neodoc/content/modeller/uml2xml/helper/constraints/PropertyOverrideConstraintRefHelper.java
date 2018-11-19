package ru.neodoc.content.modeller.uml2xml.helper.constraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.uml2.uml.Dependency;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.Constrainted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForNamedElement.ConstraintedObject;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;

public class PropertyOverrideConstraintRefHelper extends ConstraintHelper<
		Dependency,
		Dependency,
		org.alfresco.model.dictionary._1.PropertyOverride.Constraints,
		AlfrescoProfile.ForDependency.Constrainted> {

	static {
		HELPER_REGISTRY.register(PropertyOverrideConstraintRefHelper.class, PropertyOverrideConstraintsHelper.class);
	}

	

	@Override
	public List<Dependency> getSubElements(Dependency container) {
		AlfrescoProfile.ForNamedElement.ConstraintedObject<?> constrainted
			= AbstractProfile.asUntyped(container).get(AlfrescoProfile.ForNamedElement.ConstraintedObject.class);
		if (constrainted == null)
			return Collections.emptyList();
		List<Dependency> result = new ArrayList<>();
		for (AlfrescoProfile.ForDependency.Constrainted constraintedRef: constrainted.getConstraintRefs()) {
			if (!result.contains(constraintedRef.getElementClassified()))
				result.add(constraintedRef.getElementClassified());
		}
		return result;
	}

	@Override
	protected List<ObjectContainer<org.alfresco.model.dictionary._1.Constraint>> addObjectsToContainer(
			ObjectContainer<org.alfresco.model.dictionary._1.PropertyOverride.Constraints> container,
			List<ObjectContainer<org.alfresco.model.dictionary._1.Constraint>> objectsToAdd) {
		getObjects(container.getObject()).addAll(ObjectContainer.FACTORY.extractTyped(objectsToAdd));
		return ObjectContainer.FACTORY.createListTyped(getObjects(container.getObject()));
	}

	@Override
	protected List<org.alfresco.model.dictionary._1.Constraint> getOrCreateObjects(
			org.alfresco.model.dictionary._1.PropertyOverride.Constraints container) {
		return getObjects(container);
	}

	@Override
	public List<org.alfresco.model.dictionary._1.Constraint> getObjects(
			org.alfresco.model.dictionary._1.PropertyOverride.Constraints container) {
		return container.getConstraint();
	}

	@Override
	protected Class<Constrainted> getStereotypeClass() {
		return AlfrescoProfile.ForDependency.Constrainted.class;
	}


	@Override
	protected org.alfresco.model.dictionary._1.Constraint doFillObjectProperties(
			org.alfresco.model.dictionary._1.Constraint object, Dependency element, Constrainted stereotyped) {
		ConstraintedObject<?> constraintedObject = stereotyped.getConstraintedObject();
		PrefixedName pn = new PrefixedName("");
		if (constraintedObject.has(AlfrescoProfile.Internal.Namespaced.class)) {
			pn = new PrefixedName(constraintedObject.get(AlfrescoProfile.Internal.Namespaced.class).getPrfixedName());
		}
		PrefixedName pn1 = new PrefixedName(stereotyped.getElementClassified().getName());
		if (pn.isPrefixed())
			pn1.setPrefix(pn.getPrefix());

		object.setName(pn1.getFullName());
		object.setRef(stereotyped.getConstraint().getPrfixedName());
		
		return object;
	}	
}
