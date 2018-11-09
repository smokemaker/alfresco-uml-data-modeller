package ru.neodoc.content.modeller.uml2xml.helper.constraints;

import org.alfresco.model.dictionary._1.Constraint;

public class ConstraintParametersProxyObject {

	protected Constraint ownerConstraint;

	public ConstraintParametersProxyObject(Constraint ownerConstraint) {
		super();
		setOwnerConstraint(ownerConstraint);
	}
	
	public Constraint getOwnerConstraint() {
		return ownerConstraint;
	}

	public void setOwnerConstraint(Constraint ownerConstraint) {
		this.ownerConstraint = ownerConstraint;
	}
	
}