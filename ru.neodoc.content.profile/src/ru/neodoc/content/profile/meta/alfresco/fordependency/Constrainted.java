package ru.neodoc.content.profile.meta.alfresco.fordependency;

import org.eclipse.uml2.uml.Dependency;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForNamedElement.ConstraintedObject;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.PropertyMain;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

@AImplements(AlfrescoProfile.ForDependency.Constrainted.class)
public class Constrainted extends ImplementationMetaObjectClassified<Dependency>
		implements AlfrescoProfile.ForDependency.Constrainted {

	public Constrainted(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public ConstraintMain getConstraint() {
		if (getElementClassified().getSuppliers().isEmpty())
			return null;
		return AbstractProfile.asUntyped(getElementClassified().getSuppliers().get(0)).get(ConstraintMain.class);
	}

	@Override
	public ConstraintedObject<?> getConstraintedObject() {
		if (getElementClassified().getClients().isEmpty())
			return null;
		StereotypedElement se = AbstractProfile.asUntyped(getElementClassified().getClients().get(0));
		if (se.has(ConstraintedObject.class))
			return se.get(ConstraintedObject.class);
		return null;
	}

}
