package ru.neodoc.content.utils.uml.search.filter;

import org.eclipse.uml2.uml.Element;

public interface UMLSearchCompositeFilter<ElementType extends Element, ContainerType extends Element> extends
		UMLSearchFilter<ElementType, ContainerType, Object>{

	public abstract UMLSearchCompositeFilter<ElementType, ContainerType> filter(
			UMLSearchFilter<? super ElementType, ? super ContainerType, ? extends Object> f);

}
