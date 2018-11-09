package ru.neodoc.content.utils.uml.search.helper;

import java.util.Collections;
import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;

public abstract class UMLSearchHelperInAssociation<TargetClass extends Element> extends UMLSearchHelperImpl<Association, TargetClass> {

	@Override
	protected List<? extends Association> getContainersFromContainer(Association container) {
		return Collections.<Association>emptyList();
	}


}
