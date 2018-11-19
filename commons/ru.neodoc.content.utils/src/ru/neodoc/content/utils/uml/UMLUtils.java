package ru.neodoc.content.utils.uml;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;

public class UMLUtils {

	public static Model getUMLRoot(EObject eObject){
		if (eObject==null)
			return null;
		EObject current = eObject;
		while (current!=null){
			if (current instanceof Model)
				return (Model)current;
			current = current.eContainer();
		}
		return null;
	}

	public static List<Profile> getProfiles(Element element){
		Model model = getUMLRoot(element);
		return model.getAllAppliedProfiles();
	}
	
	public static Profile getProfile(Element element, String name) {
		Model model = getUMLRoot(element);
		return model.getAppliedProfile(name);
	}
	
	public static Stereotype getStereotype(Element element, String name){
		try {
			for (Profile p: getProfiles(element)) {
				Stereotype s = p.getOwnedStereotype(name);
				if (s!=null)
					return s;
			}
		} catch (NullPointerException npe) {
			return null;
		}
		return null;
	}
	
	public static Stereotype getStereotype(Element element, String profileName, String name){
		Profile p = getUMLRoot(element).getAppliedProfile(profileName);
		if (p==null)
			return null;
		Stereotype s = p.getOwnedStereotype(name);
		if (s==null)
			return null;
		return element.getAppliedStereotype(s.getQualifiedName());
	}
	
	public static boolean hasStereotype(Element element, String stereotypeName){
		if (element == null)
			return false;
		Stereotype stereotype = getStereotype(element, stereotypeName);
		if (stereotype == null)
			return false;
		return element.isStereotypeApplied(stereotype);
	}

	public static boolean isPackage(Element... elements) {
		return isOfClass(Package.class, elements);
	}

	public static boolean isAssociation(Element... elements) {
		return isOfClass(Association.class, elements);
	}

	public static boolean isClass(Element... elements) {
		return isOfClass(org.eclipse.uml2.uml.Class.class, elements);
	}

	public static boolean isOfClass(Class<? extends Element> clazz, Element... elements) {
		if (elements == null)
			return false;
		boolean result = true;
		for (int i=0; i<elements.length && result; i++)
			if (elements[i]==null)
				result = false;
			else
				result = result && clazz.isAssignableFrom(elements[i].getClass());
		return result;
	}
	
	public static Package getNearest(Element element, String stereotypeName) {
		Package result = null;
		Element current = (Element)element.eContainer();
		while (current != null && result==null){
			if (isPackage(current) && hasStereotype(current, stereotypeName))
				result = (Package)current;
			current = (Element)current.eContainer();
		}
		
		return result;
	}

	public static Dependency dependentPackage(Element source, Element target) {
		if (!(source instanceof Package) || !(target instanceof Package))
			return null;
		Package sourcePackage = (Package)source;
		Package targetPackage = (Package)target;
		
		for (Dependency d: sourcePackage.getClientDependencies()){
			if (d.getSuppliers().contains(targetPackage)) {
				d.setName("dependency_" + sourcePackage.getName() + "_" + targetPackage.getName());
				return d;
			}
		}
		
		Dependency d = sourcePackage.createDependency(targetPackage);
		d.setName("dependency_" + sourcePackage.getName() + "_" + targetPackage.getName());
		return d;
	}

}
