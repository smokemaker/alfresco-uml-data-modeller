package ru.neodoc.content.profile.meta.alfresco.forconstraint;

import java.util.List;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.AlfrescoProfileLibrary.SimpleParameter;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(AlfrescoProfile.ForConstraint.ConstraintCustom.class)
public class ConstraintCustom extends ConstraintMainAbstract
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintCustom {

	public ConstraintCustom(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public String getClassName() {
		return getAttribute(AlfrescoProfile.ForConstraint.ConstraintCustom.PROPERTIES.CLASS_NAME);
	}

	@Override
	public void setClassName(String className) {
		setAttribute(
				AlfrescoProfile.ForConstraint.ConstraintCustom.PROPERTIES.CLASS_NAME,
				className);
	}

}
