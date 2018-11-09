package ru.neodoc.content.modeller.utils.uml.elements.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;
import ru.neodoc.content.modeller.utils.uml.elements.PeerAssociation;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class PeerAssociationImpl extends BaseAssociationElementImpl implements PeerAssociation {

	public PeerAssociationImpl(BaseClassElement source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	public PeerAssociationImpl(Element element, BaseClassElement source) {
		super(element, source);
		// TODO Auto-generated constructor stub
	}

	public PeerAssociationImpl(EObject eObject, BaseClassElement source) {
		super(eObject, source);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getMainStereotypeName() {
		return AlfrescoProfile.ForAssociation.Association._NAME;
	} 
	
	@Override
	public boolean isEnforced() {
		return ((Boolean)AlfrescoUMLUtils.getStereotypeValue(
				AlfrescoProfile.ForAssociation.TargetMandatory._NAME, 
				associationElement, 
				AlfrescoProfile.ForAssociation.TargetMandatory.ENFORCED)).booleanValue();
	}

}
