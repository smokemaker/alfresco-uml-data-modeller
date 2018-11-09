package ru.neodoc.content.codegen.sdoc2.wrap;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotypeClassified;

public abstract class AbstractClassifiedWrapper<S extends Element, T extends ProfileStereotypeClassified<S>> extends AbstractWrapper {

	protected AbstractClassifiedWrapper(T wrappedElement) {
		super(wrappedElement);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public T getClassifiedWrappedElement() {
		return (T)wrappedElement;
	}
	
	public S getElement() {
		return getClassifiedWrappedElement().getElementClassified(); 
	}
	
}
