package ru.neodoc.content.modeller.utils.uml;

import java.util.List;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.modeller.model.AlfrescoModelUtils;
import ru.neodoc.content.modeller.utils.NamespaceElementsCreator.AssociationInfo;
import ru.neodoc.content.modeller.utils.NamespaceElementsCreator.DependencyInfo;
import ru.neodoc.content.modeller.utils.NamespaceElementsCreator.DependencyInfo.DependencyType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.DecomposedAssociation;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterAssociationByTarget;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterByName;
import ru.neodoc.content.utils.uml.search.helper.UMLSearchHelper;

@Deprecated
public class AlfrescoUMLUtilsDeprecated extends AlfrescoUMLUtils {

	@Deprecated
	public static Stereotype apply(Element element, String stereotype) {
		Stereotype s = getStereotype(element, stereotype);
		if (s!=null && element.isStereotypeApplicable(s) && !element.isStereotypeApplied(s))
			element.applyStereotype(s);
		return s;
	}

	@Deprecated
	public static Stereotype unapply(Element element, String stereotype) {
		Stereotype s = getStereotype(element, stereotype);
		if (s!=null && element.isStereotypeApplied(s))
			element.unapplyStereotype(s);
		return s;
	}
	
	@Deprecated
	public static Stereotype makeModel(Package pack){
		return apply(pack, AlfrescoProfile.ForPackage.Model._NAME);
	}
	
	@Deprecated
	public static Stereotype makeNamespace(Package pack){
		return apply(pack, AlfrescoProfile.ForPackage.Namespace._NAME);
	}
	
	@Deprecated
	public static Stereotype makeDataType(org.eclipse.uml2.uml.PrimitiveType pt){
		return apply(pt, AlfrescoProfile.ForPrimitiveType.DataType._NAME);
	}
	
	@Deprecated
	public static Stereotype makeType(org.eclipse.uml2.uml.Class cl){
		unapply(cl, AlfrescoProfile.ForClass.Aspect._NAME);
		return apply(cl, AlfrescoProfile.ForClass.Type._NAME);
	}

	@Deprecated
	public static Stereotype makeAspect(org.eclipse.uml2.uml.Class cl){
		unapply(cl, AlfrescoProfile.ForClass.Type._NAME);
		return apply(cl, AlfrescoProfile.ForClass.Aspect._NAME);
	}
	
	@Deprecated
	public static Stereotype makeArchive(org.eclipse.uml2.uml.Class cl, boolean apply){
		return apply
				?makeArchive(cl)
				:unmakeArchive(cl);
	}
	
	@Deprecated
	public static Stereotype makeArchive(org.eclipse.uml2.uml.Class cl){
		return apply(cl, AlfrescoProfile.ForClass.Archive._NAME);
	}

	@Deprecated
	public static Stereotype unmakeArchive(org.eclipse.uml2.uml.Class cl){
		return unapply(cl, AlfrescoProfile.ForClass.Archive._NAME);
	}
	
	@Deprecated
	public static Stereotype makeProperty(org.eclipse.uml2.uml.Property prop){
		return apply(prop, AlfrescoProfile.ForProperty.Property._NAME);
	}
	
	@Deprecated
	public static Stereotype makeMandatory(org.eclipse.uml2.uml.Property prop){
		return apply(prop, AlfrescoProfile.ForProperty.Mandatory._NAME);
	}

	@Deprecated
	public static Stereotype unmakeMandatory(org.eclipse.uml2.uml.Property prop){
		return unapply(prop, AlfrescoProfile.ForProperty.Mandatory._NAME);
	}
	
	@Deprecated
	public static Stereotype makeEncrypted(org.eclipse.uml2.uml.Property prop, boolean apply){
		return apply
			?makeEncrypted(prop)
			:unmakeEncrypted(prop);
	}
	
	@Deprecated
	public static Stereotype makeEncrypted(org.eclipse.uml2.uml.Property prop){
		return apply(prop, AlfrescoProfile.ForProperty.Encrypted._NAME);
	}

	@Deprecated
	public static Stereotype unmakeEncrypted(org.eclipse.uml2.uml.Property prop){
		return unapply(prop, AlfrescoProfile.ForProperty.Encrypted._NAME);
	}
	
	@Deprecated
	public static Stereotype makeIndex(org.eclipse.uml2.uml.Property prop){
		return apply(prop, AlfrescoProfile.ForProperty.Index._NAME);
	}
	
	@Deprecated
	public static Stereotype unmakeIndex(org.eclipse.uml2.uml.Property prop){
		return unapply(prop, AlfrescoProfile.ForProperty.Index._NAME);
	}
	
	@Deprecated
	public static void makeModelPredefined(Package model, boolean value){
		if (!isModel(model))
			return;
		Stereotype stereotype = getStereotype(model, AlfrescoProfile.ForPackage.Model._NAME);
		if (stereotype == null)
			return;
	}
	
	@Deprecated
	public static Stereotype makeInherit(Generalization gen) {
		unapply(gen, AlfrescoProfile.ForGeneralization.Inherit._NAME);
		return apply(gen, AlfrescoProfile.ForGeneralization.Inherit._NAME);
	}
	
	@Deprecated
	public static void setTitle(Element element, String title){
		/*
		 * title is mandatory in Alfresco Profile 0.1+
		 * so we set empty string, but not null
		 */
		// String titleToSet = null==title?"":title;
		setDDTextDescValue(element, AlfrescoProfile.Internal.DdTextualDescription.DD_TITLE, title);
	}
	
	@Deprecated
	public static void setDescription(Element element, String description){
		setDDTextDescValue(element, AlfrescoProfile.Internal.DdTextualDescription.DD_DESCRIPTION, description);
	}
	
	@Deprecated
	public static void setDDTextDescValue(Element element, String propertyName, String value) {
		setStereotypeValue(/*AlfrescoProfile.ST_DDTEXTDESC*/null , element, propertyName, value);
	}
	
	@Deprecated
	public static void setStereotypeValue(String stereotypeName, Element element, String propertyName, Object value) {
		Stereotype stereotype = null;
		if (stereotypeName!=null && stereotypeName.length()>0){
			stereotype = getStereotype(element, stereotypeName);
			if (!element.isStereotypeApplied(stereotype)) {
				if (element.isStereotypeApplicable(stereotype))
					element.applyStereotype(stereotype);
				else
					return;
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
			try{
				if (value instanceof org.w3c.dom.Element)
					element.setValue(stereotype, propertyName, ((org.w3c.dom.Element) value).getTextContent());
				else
					element.setValue(stereotype, propertyName, value);
			} catch (Exception e) {
				System.out.println(e);
			}
	}

	@Deprecated
	public static void setModelValue(Package model, String propertyName, Object value){
		setStereotypeValue(AlfrescoProfile.ForPackage.Model._NAME, model, propertyName, value);
	}
	
	@Deprecated
	public static String getTitle(Element element){
		Object value = getStereotypeValue(null, element, AlfrescoProfile.Internal.DdTextualDescription.DD_TITLE); 
		return value==null?null:value.toString();
	}

	@Deprecated
	public static String getDescription(Element element){
		Object value = getStereotypeValue(null, element, AlfrescoProfile.Internal.DdTextualDescription.DD_DESCRIPTION); 
		return value==null?null:value.toString();
	}
	
	@Deprecated
	public static void dependentNamespace(Element source, Element target){
		if (!isNamespace(source) || !isNamespace(target))
			return;		
		dependentPackage(source, target);
	} 
	
	@Deprecated
	public static void importPackage(Element source, Element target){
		if (!(source instanceof Package) || !(target instanceof Package))
			return;
		Package sourcePackage = (Package)source;
		Package targetPackage = (Package)target;
		if ((!isNamespace(sourcePackage) && !isModel(sourcePackage) ) || !isNamespace(targetPackage))
			return;
		if (sourcePackage.getImportedPackages().contains(targetPackage))
			return;
		sourcePackage.createPackageImport(targetPackage);
	}
	
	@Deprecated
	public static void generalize(Element child, Element parent){
		if (!(child instanceof Class) || !(parent instanceof Class))
			return;
		
		Class childClass = (Class)child;
		Class parentClass = (Class)parent;
		
		Generalization gen = childClass.getGeneralization(parentClass); 
		if (gen==null)
			gen = childClass.createGeneralization(parentClass);
		
		makeInherit(gen);
		
	}

	@Deprecated
	public static Package createNamespace(Package parent, String name){
		Package newPackage = parent.createNestedPackage(name);
		makeNamespace(newPackage);
		
		AlfrescoModelUtils.createEmptyClassDiagram(parent, newPackage);
		AlfrescoModelUtils.createEmptyPackageDiagram(newPackage, name);
		
		return newPackage;
	} 
	
	@Deprecated
	public static Package createModel(Package parent, String name){
		Package newPackage = parent.createNestedPackage(name);
		makeModel(newPackage);
		
		AlfrescoModelUtils.createEmptyPackageDiagram(newPackage, name);
		
		return newPackage;
	}
	
	@Deprecated
	protected static Association findAssociation(Class source, Class target, String name) {
		for (Association a: source.getAssociations()) {
			for (Property p: a.getMemberEnds())
				if (p.getType().equals(target))
					if (name!=null)
						if (name.equals(a.getName()))
							return a;
		}
		
		return null;
	} 
	
	@Deprecated
	protected static Association createOrUpdateAssociation(DependencyInfo di) {
		return createOrUpdateAssociation(di, null, true);
	}
	
	@Deprecated
	protected static Association createOrUpdateAssociation(
			DependencyInfo di, 
			String stereotypeName,
			boolean serachByName) {
		Class source = (Class)di.source.element;
		Class target = (Class)di.target.element;
		
		if (source==null || target==null) return null;
		
		AssociationInfo ai = di.getAssociationInfo();

		String assocName = null;
		if (ai!=null)
			assocName = ai.name;
		if (!CommonUtils.isValueable(assocName))
			assocName = source.getName() + "_" + target.getName();
		
		UMLSearchHelper<Class, Association> sh = 
				ru.neodoc.content.utils.uml.search.helper.SearchHelperFactory.getAssociationSearcher()
					.startWith(source)
					.filter(
							(new UMLSearchFilterAssociationByTarget())
								.value(target)
							)
					.target(stereotypeName)
					.target(Association.class);
		if (serachByName)
			sh.filter(
					(new UMLSearchFilterByName())
					.value(assocName)
				);

		
		List<? extends Association> found = sh.search();
		
		/*
		 * We create association from SOURCE object,
		 * though it swaps first and second ends.
		 * BUT!!! the association becomes owned by SOURCE package
		 * */
		
		Association assoc = null;
		
		if (found.size()>0)
			assoc = found.get(0);

		if (assoc == null)
			assoc = createAssociation(di, stereotypeName);
		else 
			updateAssociation(di, stereotypeName, assoc);
		if (assoc!=null)
			assoc.setName(assocName);
		
		return assoc;
	}
	
	@Deprecated
	protected static Association createAssociation(DependencyInfo di, String stereotypeName){
		Association assoc = null;
		
		Class source = (Class)di.source.element;
		Class target = (Class)di.target.element;
		
		if (source==null || target==null) return null;
		
		AssociationInfo ai = di.getAssociationInfo();
		
		String assocName = null;
		if (ai!=null)
			assocName = ai.name;
		if (!CommonUtils.isValueable(assocName))
			assocName = source.getName() + "_" + target.getName();

		if (ai != null){
			assoc = source.createAssociation(
					false, AggregationKind.NONE_LITERAL, 
					ai.targetRole, 
					ai.targetMin, 
					ai.targetMax, 
					target,
					
					false, AggregationKind.NONE_LITERAL,
					ai.sourceRole, 
					ai.sourceMin, 
					ai.sourceMax
					);
		} else if (di.dependencyType.equals(DependencyType.MANDATORY_ASPECT)) {
			assoc = source.createAssociation(
					false, AggregationKind.NONE_LITERAL, 
					"", 
					1, 
					1, 
					target,
					
					false, AggregationKind.NONE_LITERAL,
					"", 
					0, 
					-1
					);
		} else {
			return null;
		}
		
		if (stereotypeName!=null){
			apply(assoc, stereotypeName);
			
			if (ai!=null){
				setTitle(assoc, ai.title);
				setDescription(assoc, ai.description);
				if (ai.targetForce){
					setStereotypeValue(
							stereotypeName, 
							assoc, 
							AlfrescoProfile.Internal.Enforced.ENFORCED, 
							Boolean.TRUE);
				}
			}
		}
		
		return assoc;
		
	}
	
	@Deprecated
	protected static void updateAssociation(DependencyInfo di, String stereotypeName, Association association){
		Class source = (Class)di.source.element;
		AssociationInfo ai = di.getAssociationInfo();
		DecomposedAssociation da = new DecomposedAssociation(association, source);

		da.target.setIsNavigable(false);
		da.target.setAggregation(AggregationKind.NONE_LITERAL);
		da.source.setIsNavigable(false);
		da.source.setAggregation(AggregationKind.NONE_LITERAL);
		
		
		if (ai != null){
			da.target.setName(ai.targetRole);
			da.target.setLower(ai.targetMin);
			da.target.setUpper(ai.targetMax);
			
			da.source.setName(ai.sourceRole);
			da.source.setLower(ai.sourceMin);
			da.source.setUpper(ai.sourceMax);
			
		} else if (di.dependencyType.equals(DependencyType.MANDATORY_ASPECT)) {
			da.target.setName("");
			da.target.setLower(1);
			da.target.setUpper(1);
			
			da.source.setName("");
			da.source.setLower(0);
			da.source.setUpper(-1);
		}
		
	}
	
	@Deprecated
	public static void childAssociation(DependencyInfo di){
		
		Association assoc = createOrUpdateAssociation(di, AlfrescoProfile.ForAssociation.ChildAssociation._NAME, true);
		if (assoc == null) return;
		
		AssociationInfo ai = di.getAssociationInfo();
		
		Property p = assoc.getMemberEnd(
			di.getAssociationInfo().targetRole, (Class)di.target.element);
		p.setAggregation(AggregationKind.SHARED_LITERAL);
		p.setIsNavigable(false);
		
//		apply(assoc, AlfrescoProfile.ST_CHILD_ASSOCIATION);
		
		if (ai.childName!=null)
			setStereotypeValue(AlfrescoProfile.ForAssociation.ChildAssociation._NAME, 
					assoc, 
					AlfrescoProfile.ForAssociation.ChildAssociation.CHILD_NAME, 
					ai.childName);

		if (ai.isDuplicate!=null)
			setStereotypeValue(AlfrescoProfile.ForAssociation.ChildAssociation._NAME, 
					assoc, 
					AlfrescoProfile.ForAssociation.ChildAssociation.DUPLICATE, 
					ai.isDuplicate);
		
		if (ai.isPropogate!=null)
			setStereotypeValue(AlfrescoProfile.ForAssociation.ChildAssociation._NAME, 
					assoc, 
					AlfrescoProfile.ForAssociation.ChildAssociation.PROPAGATE_TIMESTAMPS, 
					ai.isPropogate);
	}

	@Deprecated
	public static void peerAssociation(DependencyInfo di){
		Association assoc = createOrUpdateAssociation(di, AlfrescoProfile.ForAssociation.Association._NAME, true);
		if (assoc == null) return;
		assoc.getMemberEnd(di.getAssociationInfo().targetRole, (Class)di.target.element).setIsNavigable(false);
//		apply(assoc, AlfrescoProfile.ST_ASSOCIATION);
	}
	
	@Deprecated
	public static void mandatoryAspect(DependencyInfo di){
		Association assoc = createOrUpdateAssociation(di, AlfrescoProfile.ForAssociation.MandatoryAspect._NAME, false); 
/*				targetClass.createAssociation(
				false, 
				AggregationKind.NONE_LITERAL, 
				"", 
				1, 
				1, 
				sourceClass, 
				false, 
				AggregationKind.SHARED_LITERAL, 
				"ma_" + sourceClass.getName(), 
				1, 
				1);
*/
		Property p = assoc.getMemberEnd("", (Class)di.target.element);
		p.setAggregation(AggregationKind.SHARED_LITERAL);
		p.setIsNavigable(false);

		assoc.setName("ma_" + assoc.getName());
	}

}
