package ru.neodoc.content.modeller.uml2xml.helper.classes.associations;

import java.util.List;

import org.alfresco.model.dictionary._1.ChildAssociation;
import org.alfresco.model.dictionary._1.Class.Associations;
import org.eclipse.uml2.uml.Association;

import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;

public class ChildAssociationHelper extends AbstractAssociationHelper<ChildAssociation, AlfrescoProfile.ForAssociation.ChildAssociation>{

	static {
		HELPER_REGISTRY.register(ChildAssociationHelper.class, AssociationsHelper.class).asContained();
	}
	
	@Override
	protected List<? extends AlfrescoProfile.ForAssociation.ChildAssociation> getAssociatonsFromClass(
			ClassMain classMain) {
		return classMain.getChildAssociations();
	}

	@Override
	protected List<ChildAssociation> addJAXBObjectsToContainer(Associations container,
			List<ChildAssociation> objectsToAdd) {
		container.getChildAssociation().addAll(objectsToAdd);
		return getObjects(container);
	}

	@Override
	public List<ChildAssociation> getObjects(Associations container) {
		return container.getChildAssociation();
	}

	@Override
	protected Class<AlfrescoProfile.ForAssociation.ChildAssociation> getStereotypeClass() {
		return AlfrescoProfile.ForAssociation.ChildAssociation.class;
	}

	@Override
	protected ChildAssociation doCreateObject(Association element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createChildAssociation();
	}

	@Override
	protected ChildAssociation doFillObjectProperties(ChildAssociation object, Association element,
			AlfrescoProfile.ForAssociation.ChildAssociation stereotyped) {
		
		ChildAssociation result = super.doFillObjectProperties(object, element, stereotyped);
		
		object.setChildName(stereotyped.getChildName());
		object.setDuplicate(stereotyped.isDuplicate());
		object.setPropagateTimestamps(stereotyped.isPropagateTimestamps());
		
		return result;
	}
	
	@Override
	protected void updateProperties(ChildAssociation objectToUpdate,
			ObjectContainer<ChildAssociation> newObjectContainer) {
		super.updateProperties(objectToUpdate, newObjectContainer);
		
		objectToUpdate.setChildName(newObjectContainer.getObject().getChildName());
		objectToUpdate.setDuplicate(newObjectContainer.getObject().isDuplicate());
		objectToUpdate.setPropagateTimestamps(newObjectContainer.getObject().isPropagateTimestamps());
		
	}
	
}
