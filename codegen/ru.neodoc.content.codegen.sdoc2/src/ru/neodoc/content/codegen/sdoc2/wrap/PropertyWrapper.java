package ru.neodoc.content.codegen.sdoc2.wrap;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property;

public class PropertyWrapper extends AbstractClassifiedWrapper<org.eclipse.uml2.uml.Property,Property> {

	public PropertyWrapper(Property wrappedElement) {
		super(wrappedElement);
		// TODO Auto-generated constructor stub
	}

	public DataTypeWrapper getDataTypeWrapper() {
		return WrapperFactory.get(getClassifiedWrappedElement().getDataType());
	}
	
}
