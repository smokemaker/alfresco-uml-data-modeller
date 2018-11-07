package ru.neodoc.content.profile.meta.alfresco.forassociation;

import org.eclipse.uml2.uml.Association;

import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.AssociationSolidOptional.class)
public class AssociationSolidOptional extends ImplementationMetaObjectClassified<Association>
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.AssociationSolidOptional {

	public AssociationSolidOptional(CompositeMetaObject composite) {
		super(composite);
		// TODO Auto-generated constructor stub
	}

}
