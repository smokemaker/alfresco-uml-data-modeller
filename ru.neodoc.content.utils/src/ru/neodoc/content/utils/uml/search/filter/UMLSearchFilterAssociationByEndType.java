package ru.neodoc.content.utils.uml.search.filter;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

public class UMLSearchFilterAssociationByEndType extends UMLSearchFilterImpl<Association, org.eclipse.uml2.uml.Class, org.eclipse.uml2.uml.Class> {

	@Override
	public boolean matches(Association element, org.eclipse.uml2.uml.Class container) {
		if (element.getMemberEnds().size()<1)
			return false;
		Property sourceME = element.getMemberEnds().get(1);
		Property targetME = element.getMemberEnds().get(0);
		
		Set<Type> types = new HashSet<>();
		types.add(sourceME.getType());
		types.add(targetME.getType());
		
		return types.contains(container) && types.contains(valueToCompare);
	}
	
}
