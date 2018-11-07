package ru.neodoc.content.profile.meta.alfresco.forconstraint;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(AlfrescoProfile.ForConstraint.ConstraintLength.class)
public class ConstraintLength extends ConstraintMainAbstract
		implements AlfrescoProfile.ForConstraint.ConstraintLength {

	public ConstraintLength(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public int getMinLength() {
		return (Integer)getAttribute(
				AlfrescoProfile.ForConstraint.ConstraintLength.PROPERTIES.MIN_LENGTH);
	}

	@Override
	public void setMinLength(int minLength) {
		setAttribute(AlfrescoProfile.ForConstraint.ConstraintLength.PROPERTIES.MIN_LENGTH, minLength);
	}

	@Override
	public int getMaxLength() {
		return (Integer)getAttribute(
				AlfrescoProfile.ForConstraint.ConstraintLength.PROPERTIES.MAX_LENGTH);
	}

	@Override
	public void setMaxLength(int maxLength) {
		setAttribute(AlfrescoProfile.ForConstraint.ConstraintLength.PROPERTIES.MAX_LENGTH, maxLength);
	}

}
