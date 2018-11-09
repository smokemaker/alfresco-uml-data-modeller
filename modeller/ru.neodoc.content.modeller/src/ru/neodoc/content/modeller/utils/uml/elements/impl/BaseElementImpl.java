package ru.neodoc.content.modeller.utils.uml.elements.impl;

import java.util.UUID;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;

public abstract class BaseElementImpl implements BaseElement {
	
	protected Element element = null;
	
	protected String uniqueId;
	
	public BaseElementImpl(){
		this.uniqueId = UUID.randomUUID().toString();
	}
	
	public BaseElementImpl(Element element) {
		setElement(element);
	}
	
	public BaseElementImpl(EObject eObject) {
		if (eObject instanceof Element) 
			setElement((Element)eObject);
		
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.elements.BaseElement#isValid()
	 */
	@Override
	public boolean isValid(){
		return element != null;
	}

	protected void init(){
		
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.elements.BaseElement#getElement()
	 */
	@Override
	public Element getElement() {
		return element;
	}

	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.elements.BaseElement#setElement(org.eclipse.uml2.uml.Element)
	 */
	@Override
	public void setElement(Element element) {
		this.element = element;
		if (element!=null){
			uniqueId = element.eResource().getURIFragment(element);
			init();
		}
	}

	@Override
	public String getUniqueId() {
		return this.uniqueId;
	}
}
