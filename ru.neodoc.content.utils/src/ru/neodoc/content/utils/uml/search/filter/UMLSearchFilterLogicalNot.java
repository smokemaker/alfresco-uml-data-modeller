package ru.neodoc.content.utils.uml.search.filter;

import org.eclipse.uml2.uml.Element;

public class UMLSearchFilterLogicalNot<ElementType extends Element, ContainerType extends Element> 
		extends UMLSearchCompositeFilterImpl<ElementType, ContainerType> {

	public UMLSearchFilterLogicalNot() {
		super();
		this.multiple = false;
	}
	
	@Override
	public boolean matches(ElementType element, ContainerType container) {
		boolean result = true;

		if (filters.size()>0) {
			UMLSearchFilter<? super ElementType, ? super ContainerType, ? extends Object> f = filters.get(0);
			if (f!=null)
				result = !f.matches(element, container);
		}

		return result;
	}

}
