package ru.neodoc.content.modeller.utils.uml.elements.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.elements.Aspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class AspectImpl extends BaseClassElementImpl implements Aspect {

	public AspectImpl() {
		super();
	}

	public AspectImpl(Element element) {
		super(element);
	}

	public AspectImpl(EObject eObject) {
		super(eObject);
	}

	@Override
	protected void init() {
		super.init();
		if (super.isValid())
			mainStereotype = AlfrescoUMLUtils.getStereotype(typeElement, AlfrescoProfile.ForClass.Aspect._NAME);
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && (this.mainStereotype != null);
	}

}
