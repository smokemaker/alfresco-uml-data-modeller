package ru.neodoc.content.profile.meta.alfresco.forassociation;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.DdTextualDescription;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(AlfrescoProfile.ForAssociation.AssociationSolid.class)
public class AssociationSolid extends AssociationMainAbstract 
		implements AlfrescoProfile.ForAssociation.AssociationSolid {
	
	protected DdTextualDescription td;

	public AssociationSolid(CompositeMetaObject composite) {
		super(composite);
		td = createAndRegisterSubimplementor(DdTextualDescription.class);
	}

	@Override
	public String getTitle() {
		return td.getTitle();
	}

	@Override
	public void setTitle(String title) {
		td.setTitle(title);
	}

	@Override
	public String getDescription() {
		return td.getDescription();
	}

	@Override
	public void setDescription(String description) {
		td.setDescription(description);
	}

}
