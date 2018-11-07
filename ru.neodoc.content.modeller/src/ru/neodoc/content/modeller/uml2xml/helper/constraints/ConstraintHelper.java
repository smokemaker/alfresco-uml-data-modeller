package ru.neodoc.content.modeller.uml2xml.helper.constraints;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelper;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ListComparator;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotypeClassified;

public abstract class ConstraintHelper<
				ElementContainerType extends Element,
				ElementType extends Element,
				JAXBContainerType,
				StereotypeClass extends ProfileStereotypeClassified<? super ElementType>> 
			extends AbstractSubHelper<
				ElementContainerType,
				ElementType,
				JAXBContainerType,
				org.alfresco.model.dictionary._1.Constraint,
				StereotypeClass
			> {

	@Override
	public ListComparator<org.alfresco.model.dictionary._1.Constraint> getComparator() {
		return new CommonUtils.BaseListComparator<org.alfresco.model.dictionary._1.Constraint>() {

			@Override
			public boolean equals(org.alfresco.model.dictionary._1.Constraint item1,
					org.alfresco.model.dictionary._1.Constraint item2) {
				// always false
				return false;
			}
			
			@Override
			public String itemHash(org.alfresco.model.dictionary._1.Constraint item) {
				if (item==null)
					return "";
				return CommonUtils.getValueable(item.getName(), "noname")
						+ "::"
						+ CommonUtils.getValueable(item.getType(), "untyped")
						+ "::"
						+ CommonUtils.getValueable(item.getRef(), "noref");
			}
		};
	}

	@Override
	protected org.alfresco.model.dictionary._1.Constraint doCreateObject(ElementType element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createConstraint();
	}

	@Override
	protected void updateProperties(org.alfresco.model.dictionary._1.Constraint objectToUpdate,
			ObjectContainer<org.alfresco.model.dictionary._1.Constraint> newObjectContainer) {
		objectToUpdate.setName(newObjectContainer.getObject().getName());
		objectToUpdate.setRef(newObjectContainer.getObject().getRef());
		objectToUpdate.setType(newObjectContainer.getObject().getType());
	}

	
}
