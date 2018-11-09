package ru.neodoc.content.modeller.uml2xml.helper.constraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Property.Constraints;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Property;

import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.Constrainted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForNamedElement.ConstraintedObject;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;

public class PropertyConstraintRefHelper extends ConstraintHelper<
		Property,
		Dependency,
		org.alfresco.model.dictionary._1.Property.Constraints,
		AlfrescoProfile.ForDependency.Constrainted> {

	static {
		HELPER_REGISTRY.register(PropertyConstraintRefHelper.class, PropertyConstraintsHelper.class);
	}

	

	@Override
	public List<Dependency> getSubElements(Property container) {
		AlfrescoProfile.ForProperty.Property umlProperty 
			= AbstractProfile.asUntyped(container).get(AlfrescoProfile.ForProperty.Property.class);
		if (umlProperty==null)
			return Collections.emptyList();
		List<Dependency> result = new ArrayList<>();
		for (Constrainted constrainted: umlProperty.getConstraintRefs()) {
			if (!result.contains(constrainted.getElementClassified()))
				result.add(constrainted.getElementClassified());
		}
		return result;
	}


	@Override
	protected List<ObjectContainer<org.alfresco.model.dictionary._1.Constraint>> addObjectsToContainer(
			ObjectContainer<Constraints> container,
			List<ObjectContainer<org.alfresco.model.dictionary._1.Constraint>> objectsToAdd) {
		container.getObject().getConstraint().addAll(ObjectContainer.FACTORY.extractTyped(objectsToAdd));
		return ObjectContainer.FACTORY.createListTyped(container.getObject().getConstraint());
	}


	@Override
	protected List<org.alfresco.model.dictionary._1.Constraint> getOrCreateObjects(Constraints container) {
		return getObjects(container);
	}


	@Override
	public List<org.alfresco.model.dictionary._1.Constraint> getObjects(Constraints container) {
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
