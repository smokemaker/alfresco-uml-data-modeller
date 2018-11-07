package ru.neodoc.content.codegen.sdoc.wrap;

import ru.neodoc.content.modeller.utils.uml.elements.DataTypeElement;

public class DataTypeWrapper extends NamedElementWrapper {

	public static DataTypeWrapper newInstance(DataTypeElement wrappedElement) {
		return new DataTypeWrapper(wrappedElement);
	}
	
	private DataTypeWrapper(DataTypeElement wrappedElement) {
		super(wrappedElement);
		setWrappedElement(wrappedElement);
	}

	@Override
	protected String getPrefix() {
		// TODO Auto-generated method stub
		return "";
	}

}
