package ru.neodoc.content.codegen.sdoc2.wrap;

import java.util.List;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.Association;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.ChildAssociation;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.MandatoryAspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property;

public abstract class AbstractAlfrescoClassWrapper<T extends ClassMain> extends AbstractClassWrapper<T> {

	protected AbstractAlfrescoClassWrapper(T wrappedElement) {
		super(wrappedElement);
		// TODO Auto-generated constructor stub
	}

	public void fill() {
	
		List<Property> properties = getClassifiedWrappedElement().getAllProperties();
		for (Property property: properties) {
			PropertyWrapper pw = WrapperFactory.get(property);
			if (!hasChild(pw)) {
				pw.setOwner(this);
				addChild(pw);
			}
		}
		
		List<MandatoryAspect> aspects = getClassifiedWrappedElement().getMandatoryAspects();
		for (MandatoryAspect ma: aspects) {
			MandatoryAspectWrapper maw = WrapperFactory.get(ma);
			if (!hasChild(maw)) {
				maw.setOwner(this);
				addChild(maw);
			}
		}
		
		List<Association> peers = getClassifiedWrappedElement().getPeerAssociations();
		for (Association peer: peers) {
			PeerAssociationWrapper paw = WrapperFactory.get(peer);
			if (!hasChild(paw)) {
				paw.setOwner(this);
				addChild(paw);
			}
		}
		
		List<ChildAssociation> childs = getClassifiedWrappedElement().getChildAssociations();
		for (ChildAssociation child: childs) {
			ChildAssociationWrapper caw = WrapperFactory.get(child);
			if (!hasChild(caw)) {
				caw.setOwner(this);
				addChild(caw);
			}
		}
	}
	
}
