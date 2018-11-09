package ru.neodoc.content.modeller.xml2uml.helper.relation;

import java.util.List;

import org.alfresco.model.dictionary._1.ChildAssociation;
import org.alfresco.model.dictionary._1.Class;
import org.eclipse.uml2.uml.Association;

import ru.neodoc.content.modeller.xml2uml.helper.AbstractHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.ClassHelper;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo.DependencyType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;

@SuppressWarnings("unchecked")
public class ChildAssociationHelper extends AbstractAssociationHelper<ChildAssociation> {

	static {
		HELPER_REGISTRY.register(ChildAssociationHelper.class, (java.lang.Class<? extends AbstractHelper<?, ?>>) ClassHelper.class,
				Association.class, ChildAssociation.class);
	}
	
	@Override
	protected List<ChildAssociation> getFinalElementsFromContainer(Class container) {
		return container.getAssociations().getChildAssociation();
	}

	@Override
	protected Association findAssociationByName(ClassMain owner, String associationName) {
		ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.ChildAssociation ca 
				= owner.getChildAssociation(associationName);
		return ca==null?null:ca.getElementClassified();
	}

	@Override
	protected java.lang.Class<? extends AlfrescoProfile.ForAssociation.Association> getProfileStereotype() {
		return AlfrescoProfile.ForAssociation.ChildAssociation.class;
	}
	
	@Override
	protected Association createAssociation(ClassMain source, ClassMain target, String associationName) {
		ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.ChildAssociation ca 
				= source.addChildAssociation(target, associationName);
		return ca==null?null:ca.getElementClassified();
	}

	@Override
	protected DependencyType getRelationType() {
		return DependencyType.CHILD;
	}
	
	@Override
	protected boolean processAssociationStereotypedElement(
			AlfrescoProfile.ForAssociation.Association associationElement,
			ModelObject<ChildAssociation> object) {
		// TODO Auto-generated method stub
		AlfrescoProfile.ForAssociation.ChildAssociation ca = associationElement.get(AlfrescoProfile.ForAssociation.ChildAssociation.class); 
		if (super.processAssociationStereotypedElement(associationElement, object)) {
			ca.setChildName(object.source.getChildName());
			ca.setDuplicate(object.source.isDuplicate());
			ca.setPropagateTimestamps(object.source.isPropagateTimestamps());
		}
		return true;
	}

/*	@Override
	protected ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.Association asAssociationStereotyped(
			StereotypedElement stereotypedElement) {
		return stereotypedElement.getOrCreate(AlfrescoProfile.ForAssociation.ChildAssociation.class);
	}*/
}
