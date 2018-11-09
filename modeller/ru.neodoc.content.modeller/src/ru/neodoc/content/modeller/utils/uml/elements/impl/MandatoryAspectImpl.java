package ru.neodoc.content.modeller.utils.uml.elements.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;
import ru.neodoc.content.modeller.utils.uml.elements.MandatoryAspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class MandatoryAspectImpl extends BaseAssociationElementImpl implements MandatoryAspect {

	public MandatoryAspectImpl(BaseClassElement source) {
		super(source);
	}

	public MandatoryAspectImpl(Element element, BaseClassElement source) {
		super(element, source);
	}

	public MandatoryAspectImpl(EObject eObject, BaseClassElement source) {
		super(eObject, source);
	}

	@Override
	protected String getMainStereotypeName() {
		return AlfrescoProfile.ForAssociation.MandatoryAspect._NAME;
	}

}
