package ru.neodoc.content.modeller.utils.uml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.modeller.model.AlfrescoModelUtils;
import ru.neodoc.content.modeller.utils.NamespaceElementsCreator.AssociationInfo;
import ru.neodoc.content.modeller.utils.NamespaceElementsCreator.DependencyInfo;
import ru.neodoc.content.modeller.utils.NamespaceElementsCreator.DependencyInfo.DependencyType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfileUtils;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.DecomposedAssociation;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterAssociationByTarget;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterByName;
import ru.neodoc.content.utils.uml.search.helper.UMLSearchHelper;

public class AlfrescoUMLUtils extends AlfrescoProfileUtils {
	
	protected static final String STEREOTYPE_NAME_FIELD = "_NAME";
	
	public static PackageType getPackageType(EObject eObject){
		if (eObject == null || !(eObject instanceof Package))
			return PackageType.PT_NOT_A_PACKAGE;
		else
			return getPackageType((Package)eObject);
	}

	@Deprecated
	public static PackageType getPackageType(Package pack){
		if (isAlfresco(pack))
			return PackageType.PT_ALFRESCO;
		if (isModel(pack))
			return PackageType.PT_MODEL;
		if (isNamespace(pack))
			return PackageType.PT_NAMESPACE;
		return PackageType.PT_NORMAL;
	}
	
	public static String getFullName(NamedElement t) {
		return getFullName(t, t);
	}
	
	public static String getFullName(NamedElement t, Element owner) {
		if (t==null)
			return null;
		if (!CommonUtils.isValueable(t.getName()))
			return "";
		String elementName = t.getName();
		if (elementName.indexOf(":")>0)
			return elementName;
		if (elementName.indexOf(":")==0)
			elementName = elementName.substring(1);
		Package ns = getNearestNamespace(owner);
		return (ns==null?"":ns.getName()+":") + elementName;
	}
	
	public static List<Package> getPackageList(EList<PackageImport> imports) {
		List<Package> result = new ArrayList<Package>();
		for (PackageImport pi: imports)
			result.add(pi.getImportedPackage());
		return result;
	}
	
	public static Profile getProfile(Element element){
		Model model = getUMLRoot(element);
		return model.getAppliedProfile(AlfrescoProfile._INSTANCE.getName());
	}
	
	public static Stereotype getStereotype(Element element, String name){
		try {
			return getProfile(element).getOwnedStereotype(name);
		} catch (NullPointerException npe) {
			return null;
		}
	}
	
	public static Stereotype getStereotypeByName(String stereotypeName, Element element){
		Stereotype stereotype = null;
		
		if (stereotypeName!=null && stereotypeName.length()>0){
			stereotype = getStereotype(element, stereotypeName);
			if (!element.isStereotypeApplied(stereotype)) {
				return null;
			}
		}
		
		return stereotype;
	}
	
	public static Stereotype getStereotypeByPropertyName(String propertyName, Element element){
		Stereotype stereotype = null;
		for (Stereotype s: element.getAppliedStereotypes()){
			for (Property p: s.getAllAttributes())
				if (p.getName().equals(propertyName)) {
					stereotype = s;
					break;
				}
			if (stereotype!=null)
				break;
		}		
		return stereotype;
	}
	
	public static Stereotype findStereotype(String stereotypeName, String propertyName, Element element) {
		Stereotype stereotype = getStereotypeByName(stereotypeName, element);
		if (stereotype == null)
			stereotype = getStereotypeByPropertyName(propertyName, element);
		return stereotype;
	}
	
	public static Object getStereotypeValue(String stereotypeName, Element element, String propertyName) {
		Stereotype stereotype = null;
		if (stereotypeName!=null && stereotypeName.length()>0){
			stereotype = getStereotype(element, stereotypeName);
			if (!element.isStereotypeApplied(stereotype)) {
				return null;
			}
		} else {
			for (Stereotype s: element.getAppliedStereotypes()){
				for (Property p: s.getAllAttributes())
					if (p.getName().equals(propertyName)) {
						stereotype = s;
						break;
					}
				if (stereotype!=null)
					break;
			}
		}
		if (stereotype!=null)
			return element.getValue(stereotype, propertyName);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getTypedStereotypeValue(String stereotypeName, Element element, String propertyName){
		Object value = getStereotypeValue(stereotypeName, element, propertyName);
		return (T)value;
	}
	
	public static Package getModel(Package pack){
		for (Package p: pack.allOwningPackages()){
			if (p.isStereotypeApplied(getStereotype(p, AlfrescoProfile.ForPackage.Model._NAME)))
				return p;
		}
		return null;
	}
	
	public static boolean isType(Element element) {
		if (!(element instanceof Class))
			return false;
		return hasStereotype(element, AlfrescoProfile.ForClass.Type._NAME);
	}

	public static boolean isAspect(Element element) {
		if (!(element instanceof Class))
			return false;
		return hasStereotype(element, AlfrescoProfile.ForClass.Aspect._NAME);
	}
	
	public static boolean isPeerAssociation(Element element){
		return isAssociation(element) && hasStereotype(element, AlfrescoProfile.ForAssociation.Association._NAME);
	}
	
	public static boolean isChildAssociation(Element element){
		return isAssociation(element) && hasStereotype(element, AlfrescoProfile.ForAssociation.ChildAssociation._NAME);
	}
	
	public static boolean isMandatoryAspect(Element element){
		return isAssociation(element) && hasStereotype(element, AlfrescoProfile.ForAssociation.MandatoryAspect._NAME);
	}
	
	public static boolean isParentOfChildAssociation(Association association, Type type){
		Property source = association.getMemberEnds().get(1);
		Property target = association.getMemberEnds().get(0);
		
		if (source.getType() == type)
			if (target.getAggregation().equals(AggregationKind.SHARED_LITERAL))
				return true;
		
		if (target.getType() == type)
			if (source.getAggregation().equals(AggregationKind.SHARED_LITERAL))
				return true;
		
		return false;
	}
	
	public static boolean isOwnerOfMandatoryAspect(Association association, Type type){
		Property source = association.getMemberEnds().get(1);
		Property target = association.getMemberEnds().get(0);
		
		if (source.getType() == type)
			if (target.getAggregation().equals(AggregationKind.SHARED_LITERAL) && isAspect(target.getType()))
				return true;
		
		if (target.getType() == type)
			if (source.getAggregation().equals(AggregationKind.SHARED_LITERAL) && isAspect(source.getType()))
				return true;
		
		return false;
	}
	
	public static Package getNearestNamespace(Element element){
		return getNearest(element, AlfrescoProfile.ForPackage.Namespace._NAME);
	}

	public static Package getNearestModel(Element element){
		return getNearest(element, AlfrescoProfile.ForPackage.Model._NAME);
	}
	
	public static void dependentModel(Element source, Element target){
		if (!isModel(source) || !isModel(target))
			return;
		dependentPackage(source, target);
	}

}
