package ru.neodoc.content.profile.meta.alfresco.forconstraint;

import java.util.List;

import org.eclipse.uml2.uml.Constraint;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.AlfrescoProfileLibrary.SimpleParameter;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.ConstraintType;
import ru.neodoc.content.utils.uml.profile.annotation.AWraps;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AWraps(ConstraintMain.class)
public abstract class ConstraintMainAbstract extends ImplementationMetaObjectClassified<Constraint>
		implements ConstraintMain {

	protected ConstraintMain _constraintMain;
	
	public ConstraintMainAbstract(CompositeMetaObject composite) {
		super(composite);
		this._constraintMain = createAndRegisterSubimplementor(ConstraintMain.class);
	}

	@Override
	public String getName() {
		return _constraintMain.getName();
	}

	@Override
	public void setName(String value) {
		_constraintMain.setName(value);
	}

	@Override
	public ConstraintType getConstraintType() {
		return _constraintMain.getConstraintType();
	}
	@Override
	public void setConstraintType(ConstraintType constraintType) {
		_constraintMain.setConstraintType(constraintType);
	}
	
	@Override
	public List<SimpleParameter> getParameters() {
		return _constraintMain.getParameters();
	}
	
	@Override
	public void setParameters(List<SimpleParameter> parameters) {
		_constraintMain.setParameters(parameters);
	}
	
	@Override
	public Namespace getNamespace() {
		return _constraintMain.getNamespace();
	}
	
	@Override
	public String getPrfixedName() {
		return _constraintMain.getPrfixedName();
	}
}
