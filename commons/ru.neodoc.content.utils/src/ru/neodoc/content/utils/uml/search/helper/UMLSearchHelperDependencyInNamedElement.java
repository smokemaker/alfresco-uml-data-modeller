package ru.neodoc.content.utils.uml.search.helper;

import java.util.Collections;
import java.util.List;

import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;

public class UMLSearchHelperDependencyInNamedElement extends UMLSearchHelperImpl<NamedElement, Dependency> {

	@Override
	protected List<? extends Dependency> getElementsFromContainer(
			NamedElement container) {
		List<Dependency> list = container.getClientDependencies(); 
		return list!=null?list:Collections.<Dependency>emptyList();
	}
	
	@Override
	protected List<NamedElement> getContainersFromContainer(NamedElement container) {
		return Collections.<NamedElement>emptyList();
	}
}
