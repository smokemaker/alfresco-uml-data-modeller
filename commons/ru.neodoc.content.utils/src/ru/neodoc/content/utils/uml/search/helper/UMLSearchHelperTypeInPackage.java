package ru.neodoc.content.utils.uml.search.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Type;


public class UMLSearchHelperTypeInPackage extends UMLSearchHelperInPackage<Type> {

	@Override
	protected List<Type> getElementsFromContainer(Package container) {
		List<PackageableElement> elements = container.getPackagedElements();
		List<Type> result = new ArrayList<Type>();
		for (PackageableElement pe: elements)
			if (pe instanceof Type)
				result.add((Type)pe);
		return result;
	}
}
