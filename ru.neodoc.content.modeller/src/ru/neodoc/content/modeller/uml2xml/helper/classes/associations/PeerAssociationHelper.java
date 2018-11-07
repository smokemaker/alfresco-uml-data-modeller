package ru.neodoc.content.modeller.uml2xml.helper.classes.associations;

import java.util.List;

import org.alfresco.model.dictionary._1.Association;
import org.alfresco.model.dictionary._1.Class.Associations;

import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;

public class PeerAssociationHelper extends AbstractAssociationHelper<Association, AlfrescoProfile.ForAssociation.Association>{

	static {
		HELPER_REGISTRY.register(PeerAssociationHelper.class, AssociationsHelper.class).asContained();
	}
	
	@Override
	protected List<? extends AlfrescoProfile.ForAssociation.Association> getAssociatonsFromClass(
			ClassMain classMain) {
		return classMain.getPeerAssociations();
	}

	@Override
	protected List<Association> addJAXBObjectsToContainer(Associations container, List<Association> objectsToAdd) {
		container.getAssociation().addAll(objectsToAdd);
		return getObjects(container);
	}

	@Override
	public List<Association> getObjects(Associations container) {
		return container.getAssociation();
	}

	@Override
	protected Class<ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.Association> getStereotypeClass() {
		return AlfrescoProfile.ForAssociation.Association.class;
	}

	@Override
	protected Association doCreateObject(org.eclipse.uml2.uml.Association element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createAssociation();
	}

}
