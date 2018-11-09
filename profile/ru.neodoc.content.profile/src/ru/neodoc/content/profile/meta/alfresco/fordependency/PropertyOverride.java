package ru.neodoc.content.profile.meta.alfresco.fordependency;

import java.util.List;

import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Namespace;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.Constrainted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.ConstraintedInline;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForNamedElement.ConstraintedObject;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(AlfrescoProfile.ForDependency.PropertyOverride.class)
public class PropertyOverride extends ImplementationMetaObjectClassified<Dependency>
		implements AlfrescoProfile.ForDependency.PropertyOverride {

	protected ConstraintedObject<Dependency> _constraintedObject;
	
	public PropertyOverride(CompositeMetaObject composite) {
		super(composite);
		_constraintedObject = createAndRegisterSubimplementor(ConstraintedObject.class);
	}

	@Override
	public List<Constrainted> getAllConstraints() {
		return _constraintedObject.getAllConstraints();
	}

	@Override
	public List<Constrainted> getConstraintRefs() {
		return _constraintedObject.getConstraintRefs();
	}

	@Override
	public List<ConstraintedInline> getInlineConstraints() {
		return _constraintedObject.getInlineConstraints();
	}

	@Override
	public Namespace getConstraintContext() {
		return _constraintedObject.getConstraintContext();
	}
	
	@Override
	public Constrainted addConstraintRef(ConstraintMain constraint) {
		return _constraintedObject.addConstraintRef(constraint);
	}

	@Override
	public ConstraintedInline addInlineConstraint(ConstraintMain constraint) {
		return _constraintedObject.addInlineConstraint(constraint);
	}

	@Override
	public Boolean getMandatory() {
		return getAttribute(PROPERTIES.MANDATORY);
	}

	@Override
	public void setMandatory(Boolean value) {
		setAttribute(PROPERTIES.MANDATORY, value);
	}

	@Override
	public String getDefaultValue() {
		return getAttribute(PROPERTIES.DEFAULT_VALUE);
	}

	@Override
	public void setDefaultValue(String value) {
		setAttribute(PROPERTIES.DEFAULT_VALUE, value);
	}

	@Override
	public Property getOverridenProperty() {
		if (getElementClassified().getSuppliers()==null)
			return null;
		if (getElementClassified().getSuppliers().isEmpty())
			return null;
		return AbstractProfile.asUntyped(getElementClassified().getSuppliers().get(0)).get(Property.class);
	}
	
	@Override
	public ClassMain getOverridingClass() {
		if (getElementClassified().getClients()==null)
			return null;
		if (getElementClassified().getClients().isEmpty())
			return null;
		return AbstractProfile.asUntyped(getElementClassified().getClients().get(0)).get(ClassMain.class);
	}
}
