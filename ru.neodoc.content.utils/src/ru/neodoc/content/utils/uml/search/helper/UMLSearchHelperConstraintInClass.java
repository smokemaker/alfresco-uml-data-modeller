package ru.neodoc.content.utils.uml.search.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Class;

public class UMLSearchHelperConstraintInClass extends UMLSearchHelperInClass<Constraint> {

	@Override
	protected List<? extends Constraint> getElementsFromContainer(Class container) {
		List<Constraint> result = new ArrayList<>();
		for (NamedElement ne: container.getOwnedMembers())
			if (ne instanceof Constraint)
				result.add((Constraint)ne);
		return result;
	}

}
