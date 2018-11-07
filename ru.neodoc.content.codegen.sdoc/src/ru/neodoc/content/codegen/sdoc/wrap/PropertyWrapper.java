package ru.neodoc.content.codegen.sdoc.wrap;

import ru.neodoc.content.modeller.utils.uml.elements.BaseNamedElement;
import ru.neodoc.content.modeller.utils.uml.elements.Property;

public class PropertyWrapper extends NamedElementWrapper {

	protected Property propertyElement;
	protected DataTypeWrapper dataTypeWrapper;
	
	public static PropertyWrapper newInstance(BaseNamedElement wrappedElement) {
		return new PropertyWrapper(wrappedElement);
	}
	
	private PropertyWrapper(BaseNamedElement wrappedElement) {
		super(wrappedElement);
		this.propertyElement = (Property) wrappedElement;
	}

	public DataTypeWrapper getDataTypeWrapper() {
		return dataTypeWrapper;
	}

	public void setDataTypeWrapper(DataTypeWrapper dataTypeWrapper) {
		this.dataTypeWrapper = dataTypeWrapper;
	}

	@Override
	protected String getPrefix() {
		return "PROP_";
	}

}
