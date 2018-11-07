package ru.neodoc.content.profile.meta.alfresco.forclass;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.meta.alfresco.forclass.AbstractClass;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(AlfrescoProfile.ForClass.Type.class)
public class Type extends AbstractClass implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Type {

	public Type(CompositeMetaObject composite) {
		super(composite);
	}

}
