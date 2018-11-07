package ru.neodoc.content.modeller.utils.uml.elements;

import org.eclipse.uml2.uml.Element;

@Deprecated
public interface BaseElement {

	boolean isValid();

	Element getElement();

	void setElement(Element element);

	String getUniqueId();
	
}