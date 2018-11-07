package ru.neodoc.content.profile.meta.alfresco.forassociation;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(AlfrescoProfile.ForAssociation.Optional.class)
public class Optional extends AssociationOptional
		implements AlfrescoProfile.ForAssociation.Optional {

	public Optional(CompositeMetaObject composite) {
		super(composite);
		// TODO Auto-generated constructor stub
	}

}
