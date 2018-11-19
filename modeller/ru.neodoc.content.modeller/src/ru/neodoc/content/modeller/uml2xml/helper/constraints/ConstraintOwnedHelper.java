package ru.neodoc.content.modeller.uml2xml.helper.constraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;

public abstract class ConstraintOwnedHelper<ElementContainerType extends Element, JAXBContainerType> 
			extends ConstraintHelper<
				ElementContainerType,
				Constraint,
				JAXBContainerType,
				AlfrescoProfile.ForConstraint.ConstraintMain
			> {

	@Override
	public List<Constraint> getSubElements(ElementContainerType container) {
		AlfrescoProfile.ForNamedElement.ConstraintedObject<?> constrainted
			= AbstractProfile.asUntyped(container).get(AlfrescoProfile.ForNamedElement.ConstraintedObject.class);
		if (constrainted == null)
			return Collections.emptyList();
		List<Constraint> result = new ArrayList<>();
		for (AlfrescoProfile.ForDependency.ConstraintedInline constraintedInline: constrainted.getInlineConstraints()) {
			Constraint constraint = constraintedInline.getConstraint().getElementClassified();
			if (!result.contains(constraint))
				result.add(constraint);
		}
		return result;
	}
	
	@Override
	protected Class<ConstraintMain> getStereotypeClass() {
		return ConstraintMain.class;
	}

	@Override
	protected org.alfresco.model.dictionary._1.Constraint doFillObjectProperties(
			org.alfresco.model.dictionary._1.Constraint object, Constraint element, ConstraintMain stereotyped) {
		object.setName(stereotyped.getPrfixedName());
		if (stereotyped.has(AlfrescoProfile.ForConstraint.ConstraintCustom.class))
			object.setType(stereotyped.get(AlfrescoProfile.ForConstraint.ConstraintCustom.class).getClassName());
		else
			object.setType(stereotyped.getConstraintType().name());
		return object;
	}



}
