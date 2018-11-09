package ru.neodoc.content.profile.meta.alfresco.forclass;

import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Aspect.class)
public class Aspect extends AbstractClass implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Aspect {

	public Aspect(CompositeMetaObject composite) {
		super(composite);
	}

}
