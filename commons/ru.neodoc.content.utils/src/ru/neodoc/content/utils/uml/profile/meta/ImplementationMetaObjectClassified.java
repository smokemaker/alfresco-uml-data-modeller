package ru.neodoc.content.utils.uml.profile.meta;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotypeClassified;

public abstract class ImplementationMetaObjectClassified<T extends Element> extends ImplementationMetaObject
		implements ProfileStereotypeClassified<T> {

	public ImplementationMetaObjectClassified(CompositeMetaObject composite) {
		super(composite);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getElementClassified() {
		try {
			return (T)getElement();
		} catch (Exception e) {
			return (T)null;
		}
	}

	
	
}
