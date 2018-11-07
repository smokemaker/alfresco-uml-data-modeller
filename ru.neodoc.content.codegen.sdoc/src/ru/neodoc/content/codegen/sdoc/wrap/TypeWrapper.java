package ru.neodoc.content.codegen.sdoc.wrap;

import ru.neodoc.content.modeller.utils.uml.elements.Type;

public class TypeWrapper extends ClassWrapper {

	protected Type type;
	
	protected TypeWrapper parentWrapper = null;
	
	public static TypeWrapper newInstance(Type wrappedElement) {
		return new TypeWrapper(wrappedElement);
	}
	
	private TypeWrapper(Type wrappedElement) {
		super(wrappedElement);
		this.type = wrappedElement;
	}
	
	@Override
	protected String getPrefix() {
		return "TYPE_";
	}
	
	public TypeWrapper getParent(){
		if (this.parentWrapper == null) {
			if (this.classElement.getParent()!=null) {
				this.parentWrapper = WrapperFactory.get(this.classElement.getParent());
			}
		}
		return this.parentWrapper;
	}
	
}
