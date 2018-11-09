package ru.neodoc.content.utils.uml.search.filter;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;

public class SearchFilterFactory {

	public static UMLSearchFilter<NamedElement, Element, String> elementByName(String name){
		return (new UMLSearchFilterByName()).value(name);
	}

	public static UMLSearchFilter<Element, Element, String> elementByStereotype(String stereotypeName){
		return (new UMLSearchFilterByStereotype()).value(stereotypeName);
	}
	
	public static UMLSearchFilter<Association, Class, Class> associationByTarget(Class target){
		return (new UMLSearchFilterAssociationByTarget()).value(target);
	}

	public static UMLSearchFilter<Association, Class, Class> associationByEndType(Class endType){
		return (new UMLSearchFilterAssociationByEndType()).value(endType);
	}
	
	public static UMLSearchFilter<Dependency, Element, Element> dependencyByClient(Element client){
		return (new UMLSearchFilterDependencyByClient()).value(client);
	}
	
	public static UMLSearchCompositeFilter<Element, Element> or(){
		return new UMLSearchFilterLogicalOr();
	} 
	
	public static <ElementType extends Element, ContainerType extends Element> UMLSearchCompositeFilter<ElementType, ContainerType> not(){
		return new UMLSearchFilterLogicalNot<ElementType, ContainerType>();
	} 
	
}
