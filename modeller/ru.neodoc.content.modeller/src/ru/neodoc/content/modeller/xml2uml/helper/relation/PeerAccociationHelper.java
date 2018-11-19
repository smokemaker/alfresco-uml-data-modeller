package ru.neodoc.content.modeller.xml2uml.helper.relation;

import java.util.List;

import org.alfresco.model.dictionary._1.Association;
import org.alfresco.model.dictionary._1.Class;

import ru.neodoc.content.modeller.xml2uml.helper.AbstractHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.ClassHelper;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo.DependencyType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;

@SuppressWarnings("unchecked")
public class PeerAccociationHelper extends AbstractAssociationHelper<Association> {

	static {
		HELPER_REGISTRY.register(PeerAccociationHelper.class, (java.lang.Class<? extends AbstractHelper<?, ?>>) ClassHelper.class,
				org.eclipse.uml2.uml.Association.class, Association.class);
	}
	
	@Override
	protected List<Association> getFinalElementsFromContainer(Class container) {
		return container.getAssociations().getAssociation();
	}

	@Override
	protected org.eclipse.uml2.uml.Association findAssociationByName(ClassMain owner, String associationName) {
		ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.Association a
				= owner.getPeerAssociation(associationName);
		return a==null?null:a.getElementClassified();
	}

	@Override
	protected java.lang.Class<? extends AlfrescoProfile.ForAssociation.Association> getProfileStereotype() {
		return AlfrescoProfile.ForAssociation.Association.class;
	}
	
	@Override
	protected org.eclipse.uml2.uml.Association createAssociation(ClassMain source, ClassMain target,
			String associationName) {
		ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.Association a
				= source.addPeerAssociation(target, associationName);
		return a==null?null:a.getElementClassified();
	}

	@Override
	protected DependencyType getRelationType() {
		return DependencyType.PEER;
	}

/*	@Override
	protected ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.Association asAssociationStereotyped(
			StereotypedElement stereotypedElement) {
		return stereotypedElement.getOrCreate(AlfrescoProfile.ForAssociation.Association.class);
	}*/
	
}
