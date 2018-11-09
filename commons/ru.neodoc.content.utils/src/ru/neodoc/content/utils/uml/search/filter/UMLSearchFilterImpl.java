package ru.neodoc.content.utils.uml.search.filter;

import org.eclipse.uml2.uml.Element;

public abstract class UMLSearchFilterImpl<ElementType extends Element, ContainerType extends Element, ValueType> implements UMLSearchFilter<ElementType, ContainerType, ValueType> {
	
	protected ValueType valueToCompare;
	
	@Override
	public abstract boolean matches(ElementType element, ContainerType container);

	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.UMLSearchFilter#value(ValueType)
	 */
	@Override
	public UMLSearchFilter<ElementType, ContainerType, ValueType> value(ValueType val){
		setValueToCompare(val);
		return this;
	} 
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.UMLSearchFilter#getValueToCompare()
	 */
	@Override
	public ValueType getValueToCompare() {
		return valueToCompare;
	}

	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.UMLSearchFilter#setValueToCompare(ValueType)
	 */
	@Override
	public void setValueToCompare(ValueType valueToCompare) {
		this.valueToCompare = valueToCompare;
	}
	
}
