package ru.neodoc.content.utils.uml.search.filter;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

public class UMLSearchFilterMemberEndByAggregation extends UMLSearchFilterImpl<Property, Association, AggregationKind> {

	@Override
	public boolean matches(Property element, Association container) {
		return element.getAggregation().equals(valueToCompare);
	}

	
	
}
