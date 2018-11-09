package ru.neodoc.content.profile.meta.alfresco.forassociation;

import org.eclipse.uml2.uml.Association;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(AlfrescoProfile.ForAssociation.AssociationOptional.class)
public class AssociationOptional extends ImplementationMetaObjectClassified<Association>
		implements AlfrescoProfile.ForAssociation.AssociationOptional {

	public AssociationOptional(CompositeMetaObject composite) {
		super(composite);
		// TODO Auto-generated constructor stub
	}

}
