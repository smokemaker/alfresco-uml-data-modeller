package ru.neodoc.content.profile.meta.alfresco.fordependency;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(AlfrescoProfile.ForDependency.ConstraintedInline.class)
public class ConstraintedInline extends Constrainted
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.ConstraintedInline {

	public ConstraintedInline(CompositeMetaObject composite) {
		super(composite);
	}

}
