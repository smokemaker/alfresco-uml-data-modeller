package ru.neodoc.content.utils.uml.search.filter;

import org.eclipse.uml2.uml.Element;

public interface UMLSearchFilter<ElementType extends Element, ContainerType extends Element, ValueType> {

	public abstract UMLSearchFilter<ElementType, ContainerType, ValueType> value(
			ValueType val);

	public abstract ValueType getValueToCompare();

	public abstract void setValueToCompare(ValueType valueToCompare);

	public abstract boolean matches(ElementType element, ContainerType container);

}