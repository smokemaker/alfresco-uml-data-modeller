package ru.neodoc.content.profile.meta.alfresco.forconstraint;

import java.util.List;

import org.eclipse.uml2.uml.Constraint;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.AlfrescoProfileLibrary.SimpleParameter;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.ConstraintType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Named;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Namespaced;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(AlfrescoProfile.ForConstraint.ConstraintMain.class)
public class ConstraintMain extends ImplementationMetaObjectClassified<Constraint>
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain {

	protected Namespaced _namespaced;
	
	public ConstraintMain(CompositeMetaObject composite) {
		super(composite);
		this._namespaced = createAndRegisterSubimplementor(Namespaced.class);
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
	public ConstraintType getConstraintType() {
		return (ConstraintType)getAttribute(PROPERTIES.TYPE);
	}

	
	@Override
	public void setConstraintType(ConstraintType constraintType) {
		setAttribute(PROPERTIES.TYPE, constraintType);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleParameter> getParameters() {
		return (List<SimpleParameter>)getAttribute(AlfrescoProfile.ForConstraint.ConstraintMain.PROPERTIES.PARAMETERS);
	}

	@Override
	public void setParameters(List<SimpleParameter> parameters) {
		setAttribute(
				AlfrescoProfile.ForConstraint.ConstraintMain.PROPERTIES.PARAMETERS, 
				parameters);
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
