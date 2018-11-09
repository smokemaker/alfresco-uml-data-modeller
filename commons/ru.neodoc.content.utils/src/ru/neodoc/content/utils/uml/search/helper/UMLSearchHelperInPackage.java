package ru.neodoc.content.utils.uml.search.helper;

import java.util.List;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

public abstract class UMLSearchHelperInPackage<TargetClass extends Element> 
	extends UMLSearchHelperImpl<Package, TargetClass> {

	@Override
	protected List<Package> getContainersFromContainer(Package container) {
		return container.getNestedPackages();
	}
	
}
