package ru.neodoc.content.utils.uml.search.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;

public class UMLSearchHelperConstraintInPackage extends UMLSearchHelperInPackage<Constraint> {

	@Override
	protected List<? extends Constraint> getElementsFromContainer(Package container) {
		List<Constraint> result = new ArrayList<>();
		for (NamedElement ne: container.getOwnedMembers())
			if (ne instanceof Constraint)
				result.add((Constraint)ne);
		return result;
	}

}
