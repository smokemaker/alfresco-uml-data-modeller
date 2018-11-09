package ru.neodoc.content.modeller.utils.uml.elements.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.elements.Type;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class TypeImpl extends BaseClassElementImpl implements Type {
	
	public TypeImpl() {
		super();
	}

	public TypeImpl(Element element) {
		super(element);
	}

	public TypeImpl(EObject eObject) {
		super(eObject);
	}
	
	@Override
	protected void init() {
		super.init();
		if (super.isValid())
			mainStereotype = AlfrescoUMLUtils.getStereotype(typeElement, AlfrescoProfile.ForClass.Type._NAME);
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && (this.mainStereotype != null);
	}
}
