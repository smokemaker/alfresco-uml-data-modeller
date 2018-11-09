package ru.neodoc.content.profile.meta.alfresco.forproperty;

import org.eclipse.uml2.uml.Property;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Enforced;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Mandatory.class)
public class Mandatory extends PropertyOptionalAbstract
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Mandatory {

	Enforced ef;
	
	public Mandatory(CompositeMetaObject composite) {
		super(composite);
		ru.neodoc.content.profile.meta.alfresco.internal.Enforced imo = new ru.neodoc.content.profile.meta.alfresco.internal.Enforced(composite);
		addSubimplemetor(imo);
		ef = imo;
	}

	@Override
	public void beforeRemove() {
		super.beforeRemove();
		Property property = (Property)this.element;
		if (property==null)
			return;
		property.setLower(0);
	}
	
	@Override
	public boolean isEnforced() {
		return ef.isEnforced();
	}

	@Override
	public void setEnforced(boolean value) {
		ef.setEnforced(value);
	}
	@Override
	public boolean getMandatory() {
		Property property = (Property)this.element;
		if (property==null)
			return false;
		return property.getLower()>0;
	}
	
	@Override
	public void setMandatory(boolean value) {
		Property property = (Property)this.element;
		if (property==null)
			return;
		property.setLower(value?1:0);
	}
}
