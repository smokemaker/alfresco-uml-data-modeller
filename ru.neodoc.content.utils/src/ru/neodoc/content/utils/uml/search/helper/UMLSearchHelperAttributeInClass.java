package ru.neodoc.content.utils.uml.search.helper;

import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

public class UMLSearchHelperAttributeInClass extends UMLSearchHelperInClass<Property> {

	@Override
	protected List<? extends Property> getElementsFromContainer(Class container) {
		return container.getOwnedAttributes();
	}

}
