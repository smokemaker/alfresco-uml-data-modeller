package ru.neodoc.content.profile.meta.alfresco.forconstraint;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(AlfrescoProfile.ForConstraint.ConstraintMinmax.class)
public class ConstraintMinmax extends ConstraintMainAbstract
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMinmax {

	public ConstraintMinmax(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public double getMinValue() {
		return getAttribute(AlfrescoProfile.ForConstraint.ConstraintMinmax.PROPERTIES.MIN_VALUE);
	}

	@Override
	public void setMinValue(double minValue) {
		setAttribute(AlfrescoProfile.ForConstraint.ConstraintMinmax.PROPERTIES.MIN_VALUE, minValue);
	}

	@Override
	public double getMaxValue() {
		return getAttribute(AlfrescoProfile.ForConstraint.ConstraintMinmax.PROPERTIES.MAX_VALUE);
	}

	@Override
	public void setMaxValue(double maxValue) {
		setAttribute(AlfrescoProfile.ForConstraint.ConstraintMinmax.PROPERTIES.MAX_VALUE, maxValue);
	}

}
