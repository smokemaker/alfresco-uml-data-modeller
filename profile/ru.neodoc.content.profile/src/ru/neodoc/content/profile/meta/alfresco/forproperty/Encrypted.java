package ru.neodoc.content.profile.meta.alfresco.forproperty;

import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Encrypted.class)
public class Encrypted extends PropertyOptionalAbstract
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Encrypted {

	public Encrypted(CompositeMetaObject composite) {
		super(composite);
	}

}
