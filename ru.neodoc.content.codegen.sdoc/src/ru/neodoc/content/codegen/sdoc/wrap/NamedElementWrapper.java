package ru.neodoc.content.codegen.sdoc.wrap;

import ru.neodoc.content.modeller.utils.uml.elements.BaseNamedElement;

public abstract class NamedElementWrapper extends BaseConvertingWrapper {
	
	public NamedElementWrapper(BaseNamedElement wrappedElement){
		super();
		setNameConverter(new CommonSdocNameConverter(getPrefix()));
		setWrappedElement(wrappedElement);
	}
	
	protected abstract String getPrefix();
}
