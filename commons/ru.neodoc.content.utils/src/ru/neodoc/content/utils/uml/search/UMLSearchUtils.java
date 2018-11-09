package ru.neodoc.content.utils.uml.search;

import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.search.filter.SearchFilterFactory;
import ru.neodoc.content.utils.uml.search.helper.SearchHelperFactory;
import ru.neodoc.content.utils.uml.search.helper.UMLSearchHelper;

public class UMLSearchUtils {

	public static Package packageByName(Package root, String name) {
		UMLSearchHelper<Package, Package> sh = SearchHelperFactory.getPackageSearcher()
				.startWith(root)
				.recoursive(true)
				.includeStartingPoint(true)
				.filter(SearchFilterFactory.elementByName(name));
		return sh.search().first();
	}
	
	public static Package getRootFor(Namespace root, String name) {
		Package startRoot = root instanceof Package
				?(Package)root
				:root.getNearestPackage();
				
		PrefixedName pn = new PrefixedName(name);
		if (!pn.isPrefixed())
			return startRoot;
		return packageByName(startRoot, pn.getPrefix());
	}
	
	public static Class classByName(Package root, String name) {
		
		Package start = getRootFor(root, name);
		
		if (start==null)
			return null;
		
		UMLSearchHelper<Package, Class> sh = SearchHelperFactory.getClassSearcher()
				.startWith(start)
				.recoursive(true)
				.filter(SearchFilterFactory.elementByName(PrefixedName.name(name)));
		
		return sh.search().first();
	}
	
	public static DataType dataTypeByName(Package root, String name) {
		
		Package start = getRootFor(root, name);
		
		if (start==null)
			return null;
		
		UMLSearchHelper<Package, Type> sh = SearchHelperFactory.getTypeSearcher()
				.target(DataType.class)
				.startWith(start)
				.recoursive(true)
				.filter(SearchFilterFactory.elementByName(PrefixedName.name(name)));
		
		return (DataType)sh.search().first();
	}

	
	public static PrimitiveType primitiveTypeByName(Package root, String name) {
		
		Package start = getRootFor(root, name);
		
		if (start==null)
			return null;
		
		UMLSearchHelper<Package, Type> sh = SearchHelperFactory.getTypeSearcher()
				.target(PrimitiveType.class)
				.startWith(start)
				.recoursive(true)
				.filter(SearchFilterFactory.elementByName(PrefixedName.name(name)));
		
		return (PrimitiveType)sh.search().first();
	}
	
	public static Constraint constraintByName(Package root, String name) {
		Package start = getRootFor(root, name);
		
		if (start==null)
			return null;

		UMLSearchHelper<Package, Constraint> sh = SearchHelperFactory.getConstraintSearcher()
				.target(Constraint.class)
				.startWith(start)
				.recoursive(true)
				.filter(SearchFilterFactory.elementByName(PrefixedName.name(name)));
		
		return (Constraint)sh.search().first();
	}
	
	// FIXME parametrize
	public static <ElementType extends NamedElement> ElementType getByName(Namespace root, String name, java.lang.Class<ElementType> elementClass){

		if (root instanceof Package) {
			Package start = getRootFor(root, name);
			
			if (start==null)
				return null;
			
			UMLSearchHelper<Package, ElementType> sh = SearchHelperFactory.getSearcher(Package.class, elementClass);
			if (sh==null)
				return null;
			
			sh.target(elementClass)
				.startWith(start)
				.recoursive(true)
				.filter(SearchFilterFactory.elementByName(PrefixedName.name(name)));
			
			return (ElementType) sh.search().first();
		}
		
		if (root instanceof Class) {
			UMLSearchHelper<Class, ElementType> sh = SearchHelperFactory.getSearcher(Class.class, elementClass);
			if (sh==null)
				return null;
			
			sh.target(elementClass)
				.startWith((Class)root)
				.recoursive(true)
				.filter(SearchFilterFactory.elementByName(PrefixedName.name(name)));
			
			return (ElementType) sh.search().first();			
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Association> associationsByEndType(Class root, Class endType){
		UMLSearchHelper<Class, Association> sh = SearchHelperFactory.getAssociationSearcher()
			.startWith(root)
			.filter(SearchFilterFactory.associationByEndType(endType));
		return (List<Association>)sh.search();
	}
}
