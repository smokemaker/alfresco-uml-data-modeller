package ru.neodoc.content.utils.uml.search.helper;

import java.util.Collections;
import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;

public abstract class UMLSearchHelperInClass<TargetClass extends Element> 
	extends UMLSearchHelperImpl<org.eclipse.uml2.uml.Class, TargetClass> {

	@Override
	protected List<Class> getContainersFromContainer(Class container) {
		return Collections.<Class>emptyList();
	}
	
}
