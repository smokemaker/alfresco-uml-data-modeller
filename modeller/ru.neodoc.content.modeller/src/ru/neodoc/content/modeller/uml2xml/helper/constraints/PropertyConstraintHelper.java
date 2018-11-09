package ru.neodoc.content.modeller.uml2xml.helper.constraints;

import java.util.List;

import org.alfresco.model.dictionary._1.Property.Constraints;
import org.eclipse.uml2.uml.Property;

import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;

public class PropertyConstraintHelper extends ConstraintOwnedHelper<Property, org.alfresco.model.dictionary._1.Property.Constraints> {

	static {
		HELPER_REGISTRY.register(PropertyConstraintHelper.class, PropertyConstraintsHelper.class);
	}


/*	@Override
	public List<Constraint> getSubElements(Property container) {
		AlfrescoProfile.ForProperty.Property umlProperty 
			= AbstractProfile.asUntyped(container).get(AlfrescoProfile.ForProperty.Property.class);
		if (umlProperty==null)
			return Collections.emptyList();
		List<Constraint> result = new ArrayList<>();
		for (ConstraintedInline constraintedInline: umlProperty.getInlineConstraints()) {
			ConstraintMain constraintMain = constraintedInline.getConstraint();
			if (!result.contains(constraintMain.getElementClassified()))
				result.add(constraintMain.getElementClassified());
		}
		return result;
	}
*/

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

}
