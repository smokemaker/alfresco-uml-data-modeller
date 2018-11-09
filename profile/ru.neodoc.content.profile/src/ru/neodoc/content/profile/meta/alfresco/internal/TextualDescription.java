package ru.neodoc.content.profile.meta.alfresco.internal;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObject;

@AImplements(AlfrescoProfile.Internal.DdTextualDescription.class)
public class TextualDescription extends ImplementationMetaObject implements AlfrescoProfile.Internal.DdTextualDescription {

	public TextualDescription(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public String getTitle() {
		return (String)getAttribute(DD_TITLE);
	}

	@Override
	public void setTitle(String title) {
		setAttribute(DD_TITLE, title);
	}

	@Override
	public String getDescription() {
		return (String)getAttribute(DD_DESCRIPTION);
	}

	@Override
	public void setDescription(String description) {
		setAttribute(DD_DESCRIPTION, description);
	}

}
