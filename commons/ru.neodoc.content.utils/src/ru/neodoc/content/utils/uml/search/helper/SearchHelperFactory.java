package ru.neodoc.content.utils.uml.search.helper;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

public class SearchHelperFactory {

	
	@SuppressWarnings("unchecked")
	public static <ContainerType extends NamedElement, ElementType extends NamedElement> UMLSearchHelper<ContainerType, ElementType> 
			getSearcher(java.lang.Class<ContainerType> containerClass, java.lang.Class<ElementType> elementClass){
		
		UMLSearchHelper<?, ?> result = null;
		
		if (Class.class.isAssignableFrom(containerClass)) {
			if (Association.class.isAssignableFrom(elementClass))
				result = getAssociationSearcher();
			else if (Property.class.isAssignableFrom(elementClass))
				result = getAttributeSearcher();
			else if (Constraint.class.isAssignableFrom(elementClass))
				result = getInnerConstraintSearcher();
		}
		else if (Package.class.isAssignableFrom(containerClass)) {
			if (Package.class.isAssignableFrom(elementClass))
				result = getPackageSearcher();
			else if (Class.class.isAssignableFrom(elementClass))
				result = getClassSearcher();
			else if (Type.class.isAssignableFrom(elementClass))
				result = getTypeSearcher();
			else if (Constraint.class.isAssignableFrom(elementClass))
				result = getConstraintSearcher();
		} 
		else if (Association.class.isAssignableFrom(containerClass)) {
			if (Property.class.isAssignableFrom(elementClass))
				result = getMemberEndSearcher();
		} 
		
		// default
		else if (Dependency.class.isAssignableFrom(elementClass))
			result = getDependencySearcher(); 
		
		return (UMLSearchHelper<ContainerType, ElementType>)result;
	}
	
	
	public static UMLSearchHelper<Class, Association> getAssociationSearcher(){
		return (new UMLSearchHelperAssociationInClass())
				.target(Association.class);
	}

	public static UMLSearchHelper<NamedElement, Dependency> getDependencySearcher(){
		return (new UMLSearchHelperDependencyInNamedElement())
				.target(Dependency.class);
	}
	
	public static UMLSearchHelper<Class, Property> getAttributeSearcher(){
		return (new UMLSearchHelperAttributeInClass())
				.target(Property.class);
	}
	
	public static UMLSearchHelper<Package, Type> getTypeSearcher(){
		return (new UMLSearchHelperTypeInPackage())
				.recoursive(true)
				.target(Type.class);
	}

	public static UMLSearchHelper<Package, NamedElement> getNamedElementSearcher(){
		return getNamedElementSearcher(NamedElement.class);
	}
	
	public static <T extends NamedElement> UMLSearchHelper<Package, T> getNamedElementSearcher(java.lang.Class<T> elementClass){
		return (new UMLSearchHelperNamedElementInPackage<T>())
				.recoursive(true)
				.target(elementClass);
	}
	
	public static UMLSearchHelper<Package, Class> getClassSearcher(){
		return (new UMLSearchHelperClassInPackage())
				.recoursive(true)
				.target(Type.class);
	}

	public static UMLSearchHelper<Package, Constraint> getConstraintSearcher(){
		return (new UMLSearchHelperConstraintInPackage())
				.recoursive(true)
				.target(Constraint.class);
	}

	public static UMLSearchHelper<Class, Constraint> getInnerConstraintSearcher(){
		return (new UMLSearchHelperConstraintInClass())
				.recoursive(true)
				.target(Constraint.class);
	}
	
	public static UMLSearchHelper<Association, Property> getMemberEndSearcher(){
		return (new UMLSearchHelperMemberEndInAssociation())
				.target(Property.class);
	}
	
	public static UMLSearchHelper<Package, Package> getPackageSearcher(){
		return (new UMLSearchHelperPackageInPackage()
				.target(Package.class));
	}
}
