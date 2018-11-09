package ru.neodoc.content.modeller.utils.uml.elements.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;
import ru.neodoc.content.modeller.utils.uml.elements.ChildAssociation;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class ChildAssociationImpl extends PeerAssociationImpl implements ChildAssociation {

	public ChildAssociationImpl(BaseClassElement source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	public ChildAssociationImpl(Element element, BaseClassElement source) {
		super(element, source);
		// TODO Auto-generated constructor stub
	}

	public ChildAssociationImpl(EObject eObject, BaseClassElement source) {
		super(eObject, source);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getMainStereotypeName() {
		return AlfrescoProfile.ForAssociation.ChildAssociation._NAME;
	}
	
	@Override
	public String getChildName() {
		return (String)AlfrescoUMLUtils.getTypedStereotypeValue(
				getMainStereotypeName(), 
				associationElement, 
				AlfrescoProfile.ForAssociation.ChildAssociation.CHILD_NAME);
	}

	@Override
	public boolean isDuplicate() {
		return ((Boolean)
				AlfrescoUMLUtils.getTypedStereotypeValue(
						getMainStereotypeName(), 
						associationElement, 
						AlfrescoProfile.ForAssociation.ChildAssociation.DUPLICATE)).booleanValue();
	}

	@Override
	public boolean isPropogateTimestamps() {
		return ((Boolean)
				AlfrescoUMLUtils.getTypedStereotypeValue(
						getMainStereotypeName(), 
						associationElement, 
						AlfrescoProfile.ForAssociation.ChildAssociation.PROPAGATE_TIMESTAMPS)).booleanValue();
	}

}
