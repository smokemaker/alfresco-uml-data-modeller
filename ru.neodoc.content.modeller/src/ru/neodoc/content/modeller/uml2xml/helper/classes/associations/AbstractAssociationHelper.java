package ru.neodoc.content.modeller.uml2xml.helper.classes.associations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.MandatoryDef;
import org.alfresco.model.dictionary._1.Class.Associations;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.TargetMandatory;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ListComparator;
import ru.neodoc.content.utils.uml.AssociationComposer;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelper;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;

public abstract class AbstractAssociationHelper<
		AssociationJAXBClass extends org.alfresco.model.dictionary._1.Association,
		AssociationStereotypeClass extends AlfrescoProfile.ForAssociation.Association> 
	extends AbstractSubHelper<
		Class, 
		Association, 
		Associations, 
		AssociationJAXBClass, 
		AssociationStereotypeClass> {

	@Override
	public List<Association> getSubElements(Class container) {
		AlfrescoProfile.ForClass.ClassMain classMain = AbstractProfile.asUntyped(container).get(AlfrescoProfile.ForClass.ClassMain.class);
		if (classMain==null)
			return Collections.emptyList();
		List<? extends AssociationStereotypeClass> list = getAssociatonsFromClass(classMain);
		if ((list==null) || list.isEmpty())
			return Collections.emptyList();
		List<Association> result = new ArrayList<>();
		for (AssociationStereotypeClass stereotyped: list)
			result.add(stereotyped.getElementClassified());
		return result;
	}

	protected abstract List<? extends AssociationStereotypeClass> getAssociatonsFromClass(AlfrescoProfile.ForClass.ClassMain classMain);
	
	@Override
	protected List<ObjectContainer<AssociationJAXBClass>> addObjectsToContainer(
			ObjectContainer<Associations> container,
			List<ObjectContainer<AssociationJAXBClass>> objectsToAdd) {
		
		return ObjectContainer.FACTORY.createListTyped(
					addJAXBObjectsToContainer(
							container.getObject(),
							ObjectContainer.FACTORY.extractTyped(objectsToAdd)
					)
				);
	}

	protected abstract List<AssociationJAXBClass> addJAXBObjectsToContainer(
			Associations container, 
			List<AssociationJAXBClass> objectsToAdd);
	
	@Override
	public ListComparator<AssociationJAXBClass> getComparator() {
		return new CommonUtils.BaseListComparator<AssociationJAXBClass>() {

			@Override
			public boolean equals(AssociationJAXBClass item1, AssociationJAXBClass item2) {
				// always update
				return false;
			}
			
			@Override
			public String itemHash(AssociationJAXBClass item) {
				if (item==null)
					return "";
				return item.getName();
			}
		};
	}

	@Override
	protected List<AssociationJAXBClass> getOrCreateObjects(Associations container) {
		return getObjects(container);
	}

	@Override
	protected AssociationJAXBClass doFillObjectProperties(
			AssociationJAXBClass object, Association element,
			AssociationStereotypeClass stereotyped) {
		
		// fill main properties
		object.setName(stereotyped.getPrfixedName());
		object.setTitle(stereotyped.getTitle());
		object.setDescription(stereotyped.getDescription());
		
		AssociationComposer associationComposer = AssociationComposer.create(element);
		
		// fill source
		org.alfresco.model.dictionary._1.Association.Source source = object.getSource();
		if (source==null) {
			source = JaxbUtils.ALFRESCO_OBJECT_FACTORY.createAssociationSource();
			object.setSource(source);
		}
		Property property = associationComposer.source().getElement();
		source.setMandatory(associationComposer.source().isMandatory());
		source.setMany(associationComposer.source().isMultiple());
		source.setRole(
				CommonUtils.isValueable(property.getName())
				?AlfrescoUMLUtils.getFullName(property, element)
				:null);
		
		// fill target
		org.alfresco.model.dictionary._1.Association.Target target = object.getTarget();
		if (target==null) {
			target = JaxbUtils.ALFRESCO_OBJECT_FACTORY.createAssociationTarget();
			object.setTarget(target);
		}
		property = associationComposer.target().getElement();
		target.setClazz(stereotyped.getTarget().getPrfixedName());
		TargetMandatory tm = stereotyped.get(TargetMandatory.class);
		Boolean enforced = false;
		if (tm!=null)
			enforced = tm.isEnforced();
		
		if (associationComposer.target().isMandatory()){
			MandatoryDef mandatory = target.getMandatory();
			if (mandatory==null) {
				mandatory = JaxbUtils.ALFRESCO_OBJECT_FACTORY.createMandatoryDef();
				target.setMandatory(mandatory);
			};
			if (property.getUpper()>1)
				mandatory.setContent("true");
			else
				mandatory.setContent("false");
			if (enforced!=null) {
				mandatory.setEnforced(enforced);
			}
		} else {
			target.setMandatory(null);
		}
		
		target.setMany(associationComposer.target().isMultiple());
		target.setRole(
				CommonUtils.isValueable(property.getName())
				?AlfrescoUMLUtils.getFullName(property, element)
				:null);
		
		
		return object;
	}

	@Override
	protected void updateProperties(AssociationJAXBClass objectToUpdate,
			ObjectContainer<AssociationJAXBClass> newObjectContainer) {
		AssociationJAXBClass newObject = newObjectContainer.getObject();
		
		// update main properties
		objectToUpdate.setName(newObject.getName());
		objectToUpdate.setTitle(newObject.getTitle());
		objectToUpdate.setDescription(newObject.getDescription());
		
		org.alfresco.model.dictionary._1.Association.Source sourceToUpdate = objectToUpdate.getSource();
		org.alfresco.model.dictionary._1.Association.Source newSource = objectToUpdate.getSource();
		if (sourceToUpdate == null) {
			objectToUpdate.setSource(newSource);
		} else {
			sourceToUpdate.setMandatory(newSource.isMandatory());
			sourceToUpdate.setMany(newSource.isMany());
			sourceToUpdate.setRole(newSource.getRole());
		}
		
		org.alfresco.model.dictionary._1.Association.Target targetToUpdate = objectToUpdate.getTarget();
		org.alfresco.model.dictionary._1.Association.Target newTarget = objectToUpdate.getTarget();
		if (targetToUpdate == null) {
			objectToUpdate.setTarget(newTarget);
		} else {
			targetToUpdate.setClazz(newTarget.getClazz());
			targetToUpdate.setMandatory(newTarget.getMandatory());
			targetToUpdate.setMany(newTarget.isMany());
			targetToUpdate.setRole(newTarget.getRole());
		}
		
	}
	

}
