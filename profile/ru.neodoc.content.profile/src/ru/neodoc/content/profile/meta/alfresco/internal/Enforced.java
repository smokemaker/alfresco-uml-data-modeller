package ru.neodoc.content.profile.meta.alfresco.internal;

import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObject;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Enforced.class)
public class Enforced extends ImplementationMetaObject
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Enforced {

	public Enforced(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public boolean isEnforced() {
		return getBoolean(ENFORCED);
	}

	@Override
	public void setEnforced(boolean value) {
		setAttribute(ENFORCED, value);
	}

}
