package ru.neodoc.content.profile.meta.alfresco.forproperty;

import java.util.List;

import org.eclipse.uml2.uml.VisibilityKind;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.Constrainted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.ConstraintedInline;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPrimitiveType.DataType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Namespaced;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property.class)
public class Property extends ImplementationMetaObjectClassified<org.eclipse.uml2.uml.Property>
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property {

	protected ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.PropertyMain pm;
	protected Namespaced _namespaced;
	
	public Property(CompositeMetaObject composite) {
		super(composite);
		PropertyMain imo = new PropertyMain(composite);
		pm = imo;
		addSubimplemetor(imo);
		_namespaced = createAndRegisterSubimplementor(Namespaced.class);
	}

	@Override
	public String getTitle() {
		return pm.getTitle();
	}

	@Override
	public void setTitle(String title) {
		pm.setTitle(title);
	}

	@Override
	public String getDescription() {
		return pm.getDescription();
	}

	@Override
	public void setDescription(String description) {
		pm.setDescription(description);
	}

	@Override
	public void setProtected(boolean value) {
		org.eclipse.uml2.uml.Property property = (org.eclipse.uml2.uml.Property)this.element;
		if (property==null)
			return;
		if (value)
			property.setVisibility(VisibilityKind.PROTECTED_LITERAL);
		else
			property.setVisibility(VisibilityKind.PUBLIC_LITERAL);
	}

	@Override
	public boolean isProtected() {
		org.eclipse.uml2.uml.Property property = (org.eclipse.uml2.uml.Property)this.element;
		if (property==null)
			return false;
		return VisibilityKind.PROTECTED_LITERAL.equals(property.getVisibility());
	}

	@Override
	public boolean isMultiple() {
		org.eclipse.uml2.uml.Property property = (org.eclipse.uml2.uml.Property)this.element;
		if (property==null)
			return false;
		return property.getUpper()==-1;
	}

	@Override
	public void setMultiple(boolean value) {
		org.eclipse.uml2.uml.Property property = (org.eclipse.uml2.uml.Property)this.element;
		if (property==null)
			return;
		property.setUpper(value?-1:1);
	}

	@Override
	public DataType getDataType() {
		return pm.getDataType();
	}

	@Override
	public void setDataType(DataType dataType) {
		pm.setDataType(dataType);
	}

	@Override
	public String getName() {
		return _namespaced.getName();
	}

	@Override
	public void setName(String value) {
		_namespaced.setName(value);
	}

	@Override
	public List<Namespace> getRequiredNamespaces() {
		return pm.getRequiredNamespaces();
	}

	@Override
	public List<Constrainted> getAllConstraints() {
		return pm.getAllConstraints();
	}

	@Override
	public List<Constrainted> getConstraintRefs() {
		return pm.getConstraintRefs();
	}

	@Override
	public List<ConstraintedInline> getInlineConstraints() {
		return pm.getInlineConstraints();
	}

	@Override
	public Constrainted addConstraintRef(ConstraintMain constraint) {
		return pm.addConstraintRef(constraint);
	}

	@Override
	public ConstraintedInline addInlineConstraint(ConstraintMain constraint) {
		return pm.addInlineConstraint(constraint);
	}

	@Override
	public org.eclipse.uml2.uml.Namespace getConstraintContext() {
		return pm.getConstraintContext();
	}
	
	@Override
	public Namespace getNamespace() {
		return _namespaced.getNamespace();
	}

	@Override
	public String getPrfixedName() {
		return _namespaced.getPrfixedName();
	}
}
