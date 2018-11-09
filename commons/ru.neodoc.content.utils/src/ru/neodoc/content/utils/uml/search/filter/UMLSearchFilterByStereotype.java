package ru.neodoc.content.utils.uml.search.filter;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.uml.UMLUtils;

public class UMLSearchFilterByStereotype extends UMLSearchFilterImpl<Element, Element, String> {

	@Override
	public boolean matches(Element element, Element container) {
		return element.isStereotypeApplied(UMLUtils.getStereotype(element, valueToCompare)); 
	}

}
