package ru.neodoc.content.modeller.utils.uml.elements.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;

import ru.neodoc.content.modeller.utils.uml.elements.BaseNamedElement;

public abstract class BaseNamedElementImpl extends BaseElementImpl implements BaseNamedElement {

	protected NamedElement namedElement;
	
	public BaseNamedElementImpl() {
		super();
	}

	public BaseNamedElementImpl(Element element) {
		super(element);
	}

	public BaseNamedElementImpl(EObject eObject) {
		super(eObject);
	}

	@Override
	protected void init() {
		super.init();
		if (super.isValid())
			if (this.element instanceof NamedElement)
				this.namedElement = (NamedElement)element;
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && (this.namedElement != null);
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.elements.NamedElement#getName()
	 */
	@Override
	public String getName(){
		return this.namedElement.getName();
	}
}
