package ru.neodoc.content.profile.meta.alfresco.forconstraint;

import org.eclipse.uml2.uml.Constraint;

import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

@AImplements(AlfrescoProfile.ForConstraint.ConstraintOptional.class)
public class ConstraintOptional extends ImplementationMetaObjectClassified<Constraint>
		implements AlfrescoProfile.ForConstraint.ConstraintOptional {

	public ConstraintOptional(CompositeMetaObject composite) {
		super(composite);
	}

}
