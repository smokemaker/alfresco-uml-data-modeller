package ru.neodoc.content.utils.uml.search.helper;

import java.util.Collections;
import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;

public class UMLSearchHelperAssociationInClass extends UMLSearchHelperInClass<Association> {

	@Override
	protected List<? extends Association> getElementsFromContainer(
			Class container) {
		List<Association> list = container.getAssociations(); 
		return list!=null?list:Collections.<Association>emptyList();
	}


}
