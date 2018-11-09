package ru.neodoc.content.utils.uml.profile.stereotype;

import org.eclipse.uml2.uml.Element;

public interface ProfileStereotypeClassified<T extends Element> extends ProfileStereotype {
	public T getElementClassified();
}
