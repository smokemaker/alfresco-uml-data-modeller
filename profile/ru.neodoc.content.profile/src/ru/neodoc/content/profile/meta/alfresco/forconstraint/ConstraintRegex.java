package ru.neodoc.content.profile.meta.alfresco.forconstraint;

import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

@AImplements(AlfrescoProfile.ForConstraint.ConstraintRegex.class)
public class ConstraintRegex extends ConstraintMainAbstract
		implements AlfrescoProfile.ForConstraint.ConstraintRegex {

	public ConstraintRegex(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public boolean getRequiresMatch() {
		return getBoolean(AlfrescoProfile.ForConstraint.ConstraintRegex.PROPERTIES.REQUIRES_MATCH);
	}

	@Override
	public void setRequiresMatch(boolean requiresMatch) {
		setAttribute(AlfrescoProfile.ForConstraint.ConstraintRegex.PROPERTIES.REQUIRES_MATCH, requiresMatch);
	}

	@Override
	public String getExpression() {
		return getAttribute(AlfrescoProfile.ForConstraint.ConstraintRegex.PROPERTIES.EXPRESSION);
	}

	@Override
	public void setExpression(String expression) {
		setAttribute(AlfrescoProfile.ForConstraint.ConstraintRegex.PROPERTIES.REQUIRES_MATCH, expression);
	}

}
