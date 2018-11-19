package ru.neodoc.content.utils.uml.search.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;

public class UMLSearchHelperNamedElementInPackage<T extends NamedElement> extends UMLSearchHelperInPackage<T> {

	@SuppressWarnings("unchecked")
	@Override
	protected List<? extends T> getElementsFromContainer(Package container) {
		List<PackageableElement> elements = container.getPackagedElements();
		List<T> result = new ArrayList<T>();
		for (PackageableElement pe: elements)
			if (pe instanceof NamedElement)
				try {
					result.add((T)pe);
				} catch (Exception e) {
					
				}
		List<Constraint> constraints = container.getOwnedRules();
		for (Constraint c: constraints) {
			try {
				result.add((T)c);
			} catch (Exception e) {
				
			}
		}
		return result;
	}

	@Override
	protected List<Package> getContainersFromContainer(Package container) {
		// TODO Auto-generated method stub
		return super.getContainersFromContainer(container);
	}
}
