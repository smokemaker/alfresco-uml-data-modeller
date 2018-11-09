package ru.neodoc.content.profile.meta.alfresco.forconstraint;

import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.Inline.class)
public class Inline extends ConstraintOptionalAbstract
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.Inline {

	public Inline(CompositeMetaObject composite) {
		super(composite);
	}

}
