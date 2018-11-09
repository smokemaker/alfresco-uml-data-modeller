package ru.neodoc.content.utils.uml.search.filter;

import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;

public class UMLSearchFilterDependencyByClient extends UMLSearchFilterImpl<Dependency, org.eclipse.uml2.uml.Element, org.eclipse.uml2.uml.Element> {

	@Override
	public boolean matches(Dependency element, org.eclipse.uml2.uml.Element container) {
		if (element.getClients().size()<1)
			return false;
		NamedElement client = element.getClients().get(0);
		return (client==null && valueToCompare==null) 
				|| (valueToCompare!=null && valueToCompare.equals(client));
	}
	
}
