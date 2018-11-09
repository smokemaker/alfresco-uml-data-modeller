package ru.neodoc.content.profile.alfresco.search.helper;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.uml.search.filter.SearchFilterFactory;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterByStereotype;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterLogicalOr;
import ru.neodoc.content.utils.uml.search.helper.SearchHelperFactory;
import ru.neodoc.content.utils.uml.search.helper.UMLSearchHelper;
import ru.neodoc.content.utils.uml.search.helper.UMLSearchHelperPackageInPackage;

//import ru.neodoc.content.modeller.utils.AlfrescoProfile;

public class AlfrescoSearchHelperFactory extends SearchHelperFactory {
	
	public static UMLSearchHelper<Package, Package> getCommonContainerSearcher(){
		return (new UMLSearchHelperPackageInPackage())
				.recoursive(true)
				.includeStartingPoint(true)
				.stopOnStereotype(AlfrescoProfile.ForModel.Alfresco._NAME)
				.stopOnStereotype(AlfrescoProfile.ForPackage.Model._NAME)
				.stopOnStereotype(AlfrescoProfile.ForPackage.Namespace._NAME);
	}
		
	public static UMLSearchHelper<Package, Package> getModelSearcher(){
		return getCommonContainerSearcher()
				.target(AlfrescoProfile.ForPackage.Model._NAME)
				.notStopOnStereotype(AlfrescoProfile.ForModel.Alfresco._NAME);
	}

	public static UMLSearchHelper<Package, Package> getNamespaceSearcher(){
		return getCommonContainerSearcher()
				.target(AlfrescoProfile.ForPackage.Namespace._NAME)
				.notStopOnStereotype(AlfrescoProfile.ForModel.Alfresco._NAME)
				.notStopOnStereotype(AlfrescoProfile.ForPackage.Model._NAME);
	}
	
	public static UMLSearchHelper<Package, Type> getDataTypeSearcher(){
		return SearchHelperFactory.getTypeSearcher()
				.target(DataType.class)
				.target(AlfrescoProfile.ForPrimitiveType.DataType._NAME);
	}
	
	public static UMLSearchHelper<org.eclipse.uml2.uml.Class, Property> getPropertySearcher(){
		return SearchHelperFactory.getAttributeSearcher().target(AlfrescoProfile.ForProperty.Property._NAME);
	}
	
	protected static UMLSearchHelper<Class, Association> getAlfrescoAssociationSearcher(String stereotypeName) {
		return SearchHelperFactory.getAssociationSearcher().target(stereotypeName);
	}
	
	protected static UMLSearchHelper<Class, Association> getAlfrescoAssociationSearcher(String stereotypeName, AlfrescoProfile.ForClass.ClassMain source) {
		return getAlfrescoAssociationSearcher(stereotypeName)
				.startWith(source.getElementClassified())
				.filter(
						SearchFilterFactory.<Association, Class>not().filter(
								SearchFilterFactory.associationByTarget(source.getElementClassified())
								)
						);
	}
	
	protected static UMLSearchHelper<Class, Association> getAlfrescoAssociationSearcher(UMLSearchHelper<Class, Association> searchHelper, AlfrescoProfile.ForClass.ClassMain source) {
		return searchHelper.startWith(source.getElementClassified())
				.filter(
						SearchFilterFactory.<Association, Class>not().filter(
								SearchFilterFactory.associationByTarget(source.getElementClassified())
								)
						);
	}
	
	public static UMLSearchHelper<Class, Association> getPeerAssociationSearcher() {
		return getAlfrescoAssociationSearcher(AlfrescoProfile.ForAssociation.Association._NAME);
	}

	public static UMLSearchHelper<Class, Association> getPeerAssociationSearcher(AlfrescoProfile.ForClass.ClassMain source) {
		return getAlfrescoAssociationSearcher(getPeerAssociationSearcher(), source);
	}
	
	public static UMLSearchHelper<Class, Association> getChildAssociationSearcher() {
		return getAlfrescoAssociationSearcher(AlfrescoProfile.ForAssociation.ChildAssociation._NAME);
	}

	public static UMLSearchHelper<Class, Association> getChildAssociationSearcher(AlfrescoProfile.ForClass.ClassMain source) {
		return getAlfrescoAssociationSearcher(getChildAssociationSearcher(), source);
	}
	
	public static UMLSearchHelper<Class, Association> getMandatoryAspectSearcher() {
		return getAlfrescoAssociationSearcher(AlfrescoProfile.ForAssociation.MandatoryAspect._NAME);
	}

	public static UMLSearchHelper<Class, Association> getMandatoryAspectSearcher(AlfrescoProfile.ForClass.ClassMain source) {
		return getAlfrescoAssociationSearcher(getMandatoryAspectSearcher(), source);
	}
	
	protected static UMLSearchHelper<NamedElement, Dependency> getAlfrescoDependencySearcher(String stereotypeName){
		return SearchHelperFactory.getDependencySearcher().target(stereotypeName);
	}
	
	protected static UMLSearchHelper<NamedElement, Dependency> getConstraintDependencySearcher(UMLSearchHelper<NamedElement, Dependency> searchHelper, AlfrescoProfile.ForNamedElement.ConstraintedObject<? extends NamedElement> source) {
		return searchHelper.startWith(source.getElementClassified());
	}
	
	public static UMLSearchHelper<NamedElement, Dependency> getConstraintedSearcher(AlfrescoProfile.ForNamedElement.ConstraintedObject<? extends NamedElement> source){
		return getConstraintDependencySearcher(getAlfrescoDependencySearcher(AlfrescoProfile.ForDependency.Constrainted._NAME), source);
	}

	public static UMLSearchHelper<NamedElement, Dependency> getInlineConstraintedSearcher(AlfrescoProfile.ForNamedElement.ConstraintedObject<? extends NamedElement> source){
		return getConstraintDependencySearcher(getAlfrescoDependencySearcher(AlfrescoProfile.ForDependency.ConstraintedInline._NAME), source);
	}

	public static UMLSearchHelper<NamedElement, Dependency> getAllConstraintedSearcher(AlfrescoProfile.ForNamedElement.ConstraintedObject<? extends NamedElement> source){
		return getConstraintDependencySearcher(getDependencySearcher(), source)
				.filter(
					(new UMLSearchFilterLogicalOr())
						.filter((new UMLSearchFilterByStereotype()).value(AlfrescoProfile.ForDependency.Constrainted._NAME))
						.filter((new UMLSearchFilterByStereotype()).value(AlfrescoProfile.ForDependency.ConstraintedInline._NAME))
				);
	}
	
	public static UMLSearchHelper<NamedElement, Dependency> getPropertyOverrideSearcher(AlfrescoProfile.ForClass.ClassMain classMain){
		return getAlfrescoDependencySearcher(AlfrescoProfile.ForDependency.PropertyOverride._NAME)
				.startWith(classMain.getElementClassified());
	}
}
