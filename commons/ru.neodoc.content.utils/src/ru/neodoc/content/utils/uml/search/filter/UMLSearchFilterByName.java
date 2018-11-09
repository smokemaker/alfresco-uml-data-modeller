package ru.neodoc.content.utils.uml.search.filter;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;

public class UMLSearchFilterByName extends UMLSearchFilterImpl<NamedElement, Element, String> {

	@Override
	public boolean matches(NamedElement element, Element container) {
		return element.getName().equals(valueToCompare);
	}

}
