package ru.neodoc.content.utils.uml.search.filter;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

public class UMLSearchFilterAssociationByTarget extends UMLSearchFilterImpl<Association, org.eclipse.uml2.uml.Class, org.eclipse.uml2.uml.Class> {

	@Override
	public boolean matches(Association element, org.eclipse.uml2.uml.Class container) {
		if (element.getMemberEnds().size()<1)
			return false;
		Property targetME = element.getMemberEnds().get(0);
		Type type = targetME.getType(); 
		return (type==null && valueToCompare==null) 
				|| (valueToCompare!=null && valueToCompare.equals(type));
	}
	
}
