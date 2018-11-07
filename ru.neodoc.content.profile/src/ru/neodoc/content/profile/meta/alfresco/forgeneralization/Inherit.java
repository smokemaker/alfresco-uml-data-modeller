package ru.neodoc.content.profile.meta.alfresco.forgeneralization;

import org.eclipse.uml2.uml.Generalization;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForGeneralization.Inherit.class)
public class Inherit extends ImplementationMetaObjectClassified<Generalization>
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForGeneralization.Inherit {

	public Inherit(CompositeMetaObject composite) {
		super(composite);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ClassMain getGeneral() {
		Generalization g = getElementClassified();
		if (g==null)
			return null;
		return AbstractProfile.asUntyped(g.getGeneral()).get(ClassMain.class);
	}

	@Override
	public ClassMain getSpecific() {
		Generalization g = getElementClassified();
		if (g==null)
			return null;
		return AbstractProfile.asUntyped(g.getSpecific()).get(ClassMain.class);
	}
	
}
