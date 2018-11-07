package ru.neodoc.content.codegen.sdoc.wrap;

import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;

public class BaseConvertingWrapper extends BaseWrapper {
	
	protected NameConverter nameConverter;

	public NameConverter getNameConverter() {
		return nameConverter;
	}

	public void setNameConverter(NameConverter nameConverter) {
		this.nameConverter = nameConverter;
	}
	
	@Override
	public void setWrappedElement(BaseElement wrappedElement) {
		super.setWrappedElement(wrappedElement);
		setInitialTargetJavaName();
		setInitialTargetJavaScriptName();
	}
	
	protected void setInitialTargetJavaName(){
		setTargetJavaName(nameConverter.convert(getName()));
	}

	protected void setInitialTargetJavaScriptName(){
		setTargetJavaScriptName(nameConverter.convert(getName()));
	}
}
