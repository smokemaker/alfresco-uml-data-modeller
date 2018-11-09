package ru.neodoc.content.modeller.xml2uml.helper.relation;

import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Association;
import org.alfresco.model.dictionary._1.Class;

import ru.neodoc.content.modeller.tasks.ExecutionCallback;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractRelationSubHelper;
import ru.neodoc.content.modeller.xml2uml.structure.AssociationInfo;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo.DependencyType;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.MandatoryAspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.TargetMandatory;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.AssociationComposer;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotypeClassified;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;
import ru.neodoc.content.utils.uml.search.UMLSearchUtils;

public abstract class AbstractAssociationHelper<AssociationClass extends Association> 
		extends AbstractRelationSubHelper<Class, AssociationClass, org.eclipse.uml2.uml.Association> {

	@Override
	protected ModelObject<AssociationClass> createNewModelObject(AssociationClass object) {
		return createNewTempModelObject(object);
	}
	
	@Override
	protected List<AssociationClass> getElementsFromContainer(Class container) {
		if (container.getAssociations()!=null)
			return getFinalElementsFromContainer(container);
		return Collections.emptyList();
	}
	
	protected abstract java.lang.Class<? extends AlfrescoProfile.ForAssociation.Association> getProfileStereotype(); 

	@Override
	protected org.eclipse.uml2.uml.Association getElement(
			ModelObject<AssociationClass> modelObject, AssociationClass object, RelationInfo relationInfo,
			ModelObject<?> sourceModelObject, ModelObject<?> targetModelObject) {
		if (!ModelObject.hasElement(sourceModelObject/*, targetModelObject*/))
			return null;
		
		org.eclipse.uml2.uml.Class sourceClass = (org.eclipse.uml2.uml.Class)sourceModelObject.getElement();
		org.eclipse.uml2.uml.Class targetClass = (org.eclipse.uml2.uml.Class)targetModelObject.getElement();
		if (targetClass == null) {
			targetClass = UMLSearchUtils.classByName(complexRegistry().getUmlRoot(), targetModelObject.getName());
		}
		if (targetClass==null)
			return null;
		for (org.eclipse.uml2.uml.Association as: UMLSearchUtils.associationsByEndType(sourceClass, targetClass)) {
			if (AbstractProfile.isType(as, getProfileStereotype()))
				if (CommonUtils.isValueable(object.getName()))
					if (object.getName().equalsIgnoreCase(as.getName()))
						return as;
					else
						return null;
				else
					return as;
		}
		return null;
	}

	protected abstract org.eclipse.uml2.uml.Association findAssociationByName(ClassMain owner, String associationName);
	
	@Override
	protected org.eclipse.uml2.uml.Association createElement(
			ModelObject<AssociationClass> modelObject, AssociationClass object, RelationInfo relationInfo,
			ModelObject<?> sourceModelObject, ModelObject<?> targetModelObject) {
		
		if (!ModelObject.hasElement(sourceModelObject, targetModelObject))
			return null;

		ClassMain cmSource = AbstractProfile.asUntyped(sourceModelObject.getElement()).get(ClassMain.class);
		ClassMain cmTarget = AbstractProfile.asUntyped(targetModelObject.getElement()).get(ClassMain.class);
		if ((cmSource==null) || (cmTarget==null))
			return null;
		
		return createAssociation(cmSource, cmTarget, object.getName());
	}
	
	protected abstract org.eclipse.uml2.uml.Association createAssociation(ClassMain source, ClassMain target, String associationName);
	
	protected abstract List<AssociationClass> getFinalElementsFromContainer(Class container);

	@Override
	protected ModelObject<?> getTargetModelObject(ModelObject<AssociationClass> modelObject) {
		String targetName = modelObject.source.getTarget().getClazz();
		return complexRegistry().getObjectSmartFactory().getObject(targetName);
	}
	
	@Override
	protected boolean processStereotypedElement(StereotypedElement stereotypedElement,
			ModelObject<AssociationClass> object) {
		super.processStereotypedElement(stereotypedElement, object);
		return processAssociationStereotypedElement(asAssociationStereotyped(stereotypedElement), object);
	}
	
	protected AlfrescoProfile.ForAssociation.Association asAssociationStereotyped(StereotypedElement stereotypedElement){
		return stereotypedElement.getOrCreate(getProfileStereotype());
	};
	
	protected boolean processAssociationStereotypedElement(AlfrescoProfile.ForAssociation.Association associationElement, 
			ModelObject<AssociationClass> object) {
		if (associationElement==null)
			return false;
		
		associationElement.setTitle(object.source.getTitle());
		associationElement.setDescription(object.source.getDescription());
		
		AssociationComposer ab = AssociationComposer.create(associationElement.getElementClassified());
		RelationInfo ri = complexRegistry().getRelationRegistry().getRelationByObject(object);
		AssociationInfo ai = ri.getAssociationInfo(); 
		
		ab.source()
			.lower(ai.sourceMin)
			.upper(ai.sourceMax)
			.roleName(ai.sourceRole)
			.builder()
		.target()
			.type((org.eclipse.uml2.uml.Class)ri.target.getElement())
			.lower(ai.targetMin)
			.upper(ai.targetMax)
			.roleName(ai.targetRole);

		if (ai.targetForce) {
			TargetMandatory tm = associationElement.getOrCreate(TargetMandatory.class);
			tm.setEnforced(true);
		} else
			associationElement.remove(TargetMandatory.class);
		
		return true;
	}
}
