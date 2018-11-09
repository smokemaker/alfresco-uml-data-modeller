package ru.neodoc.content.profile.meta.alfresco.internal;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObject;

@AImplements(AlfrescoProfile.Internal.StoredElement.class)
public class StoredElement extends ImplementationMetaObject implements AlfrescoProfile.Internal.StoredElement {

	public StoredElement(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public boolean getPredefined() {
		return getBoolean(PREDEFINED);
	}

	@Override
	public boolean getDetached() {
		return getBoolean(DETACHED);
	}

	@Override
	public void setPredefined(boolean value) {
		setAttribute(PREDEFINED, value);
	}

	@Override
	public void setDetached(boolean value) {
		setAttribute(DETACHED, value);
	}
	
}
