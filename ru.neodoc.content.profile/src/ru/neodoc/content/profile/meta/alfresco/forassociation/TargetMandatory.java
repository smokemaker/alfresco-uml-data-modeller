package ru.neodoc.content.profile.meta.alfresco.forassociation;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Enforced;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(AlfrescoProfile.ForAssociation.TargetMandatory.class)
public class TargetMandatory extends AssociationSolidOptional
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.TargetMandatory {

	protected Enforced ef;
	
	public TargetMandatory(CompositeMetaObject composite) {
		super(composite);
		createAndRegisterSubimplementor(Enforced.class);
	}

	@Override
	public boolean isEnforced() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setEnforced(boolean value) {
		// TODO Auto-generated method stub

	}

}
