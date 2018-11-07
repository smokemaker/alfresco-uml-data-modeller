package ru.neodoc.content.profile.meta.alfresco.forconstraint;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(AlfrescoProfile.ForConstraint.ConstraintRegistered.class)
public class ConstraintRegistered extends ConstraintMainAbstract
		implements AlfrescoProfile.ForConstraint.ConstraintRegistered {

	
	
	public ConstraintRegistered(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public String getRegisteredName() {
		return (String)getAttribute(AlfrescoProfile.ForConstraint.ConstraintRegistered.PROPERTIES.REGISTERED_NAME);
	}

	@Override
	public void setRegisteredName(String registeredName) {
		setAttribute(AlfrescoProfile.ForConstraint.ConstraintRegistered.PROPERTIES.REGISTERED_NAME, registeredName);
	}

}
