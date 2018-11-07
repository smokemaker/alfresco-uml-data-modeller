package ru.neodoc.content.utils.uml.search.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Package;

public class UMLSearchHelperPackageInPackage extends UMLSearchHelperInPackage<Package> {

	@Override
	protected List<Package> getElementsFromContainer(Package container) {
		List<Package> result = new ArrayList<Package>();
		if (includeStartingPointInSearch && container.equals(startingPoint))
			result.add(startingPoint);
		result.addAll(container.getNestedPackages());
		return result;
	}
}
