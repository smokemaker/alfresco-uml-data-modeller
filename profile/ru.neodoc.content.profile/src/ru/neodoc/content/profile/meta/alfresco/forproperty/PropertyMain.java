package ru.neodoc.content.profile.meta.alfresco.forproperty;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.Constrainted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.ConstraintedInline;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPrimitiveType.DataType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.DdTextualDescription;
import ru.neodoc.content.profile.meta.alfresco.internal.TextualDescription;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.PropertyMain.class)
public class PropertyMain extends ImplementationMetaObjectClassified<Property>
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.PropertyMain {

	protected DdTextualDescription td;
	
	protected AlfrescoProfile.ForNamedElement.ConstraintedObject<Property> _constraintedObject;
	
	@SuppressWarnings("unchecked")
	public PropertyMain(CompositeMetaObject composite) {
		super(composite);
		TextualDescription impl = new TextualDescription(composite); 
		td = impl;
		addSubimplemetor(impl);
		_constraintedObject = createAndRegisterSubimplementor(AlfrescoProfile.ForNamedElement.ConstraintedObject.class);
	}

	@Override
	public String getTitle() {
		return td.getTitle();
	}

	@Override
	public void setTitle(String title) {
		td.setTitle(title);
	}

	@Override
	public String getDescription() {
		return td.getDescription();
	}

	@Override
	public void setDescription(String description) {
		td.setDescription(description);
	}

	@Override
	public DataType getDataType() {
		Property property = getElementClassified();
		if (property==null)
			return null;
		if (property.getType() instanceof PrimitiveType)
			return DataType._HELPER.getFor((PrimitiveType)property.getType());
		return null;
	}
	
	@Override
	public void setDataType(DataType dataType) {
		if (dataType==null)
			return;
		PrimitiveType pt = dataType.getElementClassified();
		if (pt==null)
			return;
		Property property = getElement();
		if (property==null)
			return;
		property.setType(pt);
	}

	@Override
	public List<Namespace> getRequiredNamespaces() {
		List<Namespace> result = new ArrayList<>();
		DataType dt = getDataType();
		if (dt!=null)
			result.add(Namespace._HELPER.findNearestFor(dt));
		return result;
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
	public Constrainted addConstraintRef(ConstraintMain constraint) {
		return _constraintedObject.addConstraintRef(constraint);
	}

	@Override
	public ConstraintedInline addInlineConstraint(ConstraintMain constraint) {
		return _constraintedObject.addInlineConstraint(constraint);
	}

	@Override
	public org.eclipse.uml2.uml.Namespace getConstraintContext() {
		// TODO Auto-generated method stub
		return _constraintedObject.getConstraintContext();
	}
}
