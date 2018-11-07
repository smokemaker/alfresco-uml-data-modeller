package ru.neodoc.content.codegen.sdoc.wrap;

import ru.neodoc.content.modeller.utils.uml.elements.Aspect;

public class AspectWrapper extends ClassWrapper {

	protected Aspect aspect;
	
	public static AspectWrapper newInstance(Aspect wrappedElement){
		return new AspectWrapper(wrappedElement);
	}
	
	private AspectWrapper(Aspect wrappedElement) {
		super(wrappedElement);
		this.aspect = wrappedElement;
	}
	
	@Override
	protected String getPrefix() {
		return "ASPECT_";
	}
}
