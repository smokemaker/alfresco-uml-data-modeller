package ru.neodoc.content.utils.uml.search.helper;

import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

public class UMLSearchHelperMemberEndInAssociation extends UMLSearchHelperInAssociation<Property> {

	@Override
	protected List<? extends Property> getElementsFromContainer(Association container) {
		return container.getMemberEnds();
	}
	
}
