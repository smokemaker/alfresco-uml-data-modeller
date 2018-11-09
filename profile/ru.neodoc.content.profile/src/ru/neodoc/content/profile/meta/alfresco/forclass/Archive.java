package ru.neodoc.content.profile.meta.alfresco.forclass;

import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Archive.class)
public class Archive extends ImplementationMetaObjectClassified<org.eclipse.uml2.uml.Class>
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Archive {

	public Archive(CompositeMetaObject composite) {
		super(composite);
	}

}
