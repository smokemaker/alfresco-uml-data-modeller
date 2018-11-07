package ru.neodoc.content.profile.meta.alfresco.forliteralspecification;

import org.eclipse.uml2.uml.LiteralSpecification;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.AlfrescoProfileLibrary.SimpleParameter;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(AlfrescoProfile.ForLiteralSpecification.SimpleParameterWrapper.class)
public class SimpleParameterWrapper extends ImplementationMetaObjectClassified<LiteralSpecification> 
		implements AlfrescoProfile.ForLiteralSpecification.SimpleParameterWrapper{

	public SimpleParameterWrapper(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public SimpleParameter getWrappedValue() {
		return getAttribute(PROPERTIES.WRAPPED_VALUE);
	}

	@Override
	public void setWrappedValue(SimpleParameter value) {
		setAttribute(PROPERTIES.WRAPPED_VALUE, value);
	}

	
	
}
