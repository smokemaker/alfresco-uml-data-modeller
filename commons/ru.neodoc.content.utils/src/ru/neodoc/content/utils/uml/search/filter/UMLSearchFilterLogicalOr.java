package ru.neodoc.content.utils.uml.search.filter;

import org.eclipse.uml2.uml.Element;

public class UMLSearchFilterLogicalOr extends UMLSearchCompositeFilterImpl<Element, Element> {

	@Override
	public boolean matches(Element element, Element container) {
		boolean result = false;
		
		for (UMLSearchFilter<? super Element, ? super Element, ? extends Object> f: filters){
			result = result || f.matches(element, container);
		}
		
		return result;
	}

}
