package ru.neodoc.content.utils.uml.search.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;

public class UMLSearchHelperClassInPackage extends UMLSearchHelperInPackage<Class> {

	@Override
	protected List<? extends Class> getElementsFromContainer(Package container) {
		List<Class> result = new ArrayList<>();
		for (NamedElement ne: container.getOwnedMembers())
			if (ne instanceof Class)
				result.add((Class)ne);
		return result;
	}

}
