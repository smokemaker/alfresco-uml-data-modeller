package ru.neodoc.content.profile.meta.alfresco.internal;

import org.eclipse.uml2.uml.NamedElement;

import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObject;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Named.class)
public class Named extends ImplementationMetaObject
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Named {

	public Named(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public String getName() {
		try {
			return ((NamedElement)getElement()).getName();
		} catch (Exception e) {
			// NOOP
		}
		return null;
	}

	@Override
	public void setName(String value) {
		try {
			((NamedElement)getElement()).setName(value);
		} catch (Exception e) {
			// NOOP
		}
	}

}
