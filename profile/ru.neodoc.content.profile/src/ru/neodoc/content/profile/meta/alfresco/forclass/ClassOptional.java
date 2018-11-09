package ru.neodoc.content.profile.meta.alfresco.forclass;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(AlfrescoProfile.ForClass.ClassOptional.class)
public class ClassOptional extends ImplementationMetaObjectClassified<org.eclipse.uml2.uml.Class>
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassOptional {

	public ClassOptional(CompositeMetaObject composite) {
		super(composite);
		// TODO Auto-generated constructor stub
	}

}
