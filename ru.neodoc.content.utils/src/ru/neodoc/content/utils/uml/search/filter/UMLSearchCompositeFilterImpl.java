package ru.neodoc.content.utils.uml.search.filter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Element;

public abstract class UMLSearchCompositeFilterImpl<ElementType extends Element, ContainerType extends Element> 
	implements UMLSearchCompositeFilter<ElementType, ContainerType> {

	protected List<UMLSearchFilter<? super ElementType, ? super ContainerType, ? extends Object>> filters
		= new ArrayList<UMLSearchFilter<? super ElementType, ? super ContainerType, ? extends Object>>();
	
	protected boolean multiple = true;
	
	@Override
	public UMLSearchCompositeFilter<ElementType, ContainerType> filter(
			UMLSearchFilter<? super ElementType, ? super ContainerType, ? extends Object> f) {
		if (multiple || filters.isEmpty())
			filters.add(f);
		else
			filters.set(0, f);
		
		return this;
	}

	@Override
	public UMLSearchFilter<ElementType, ContainerType, Object> value(
			Object val) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValueToCompare() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValueToCompare(Object valueToCompare) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public abstract boolean matches(ElementType element, ContainerType container);
	
}
