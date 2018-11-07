package ru.neodoc.content.profile.meta.alfresco.forconstraint;

import org.eclipse.uml2.uml.Constraint;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintOptional;
import ru.neodoc.content.utils.uml.profile.annotation.AWraps;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AWraps(ConstraintOptional.class)
public class ConstraintOptionalAbstract extends ImplementationMetaObjectClassified<Constraint>
		implements ConstraintOptional {

	protected ConstraintOptional _constraintOptional;
	
	public ConstraintOptionalAbstract(CompositeMetaObject composite) {
		super(composite);
		this._constraintOptional = createAndRegisterSubimplementor(ConstraintOptional.class);
	}

}
