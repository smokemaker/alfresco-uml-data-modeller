package ru.neodoc.content.modeller.utils.uml.elements.impl;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.elements.BaseAssociationElement;
import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;
import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;
import ru.neodoc.content.modeller.utils.uml.elements.ElementFactory;

public abstract class BaseAssociationElementImpl extends BaseTitledElementImpl implements BaseAssociationElement {

	protected Association associationElement;
	
	protected BaseClassElement source;
	protected BaseClassElement target;
	
	public BaseAssociationElementImpl(BaseClassElement source) {
		super();
		setSource(source);
	}

	public BaseAssociationElementImpl(Element element, BaseClassElement source) {
		super(element);
		setSource(source);
	}

	public BaseAssociationElementImpl(EObject eObject, BaseClassElement source) {
		super(eObject);
		setSource(source);
	}
	
	protected void setSource(BaseClassElement source){
		this.source = source;
		defineTarget();
	}
	
	@Override
	protected void init() {
		super.init();
		if (super.isValid())
			if (this.element instanceof Association){
				this.associationElement = (Association) element;
				mainStereotype = AlfrescoUMLUtils.getStereotype(associationElement, getMainStereotypeName());
				//defineTarget();
			};
	}
	
	protected void defineTarget(){
		List<Type> endTypes = this.associationElement.getEndTypes();
		this.target = source;
		for (Type t: endTypes) {
			if (t == source.getElement())
				continue;
			BaseElement be = ElementFactory.createElement(t);
			if (be instanceof BaseClassElement)
				this.target = (BaseClassElement)be;
		}
	}
	
	@Override
	public BaseClassElement getTarget() {
		return this.target;
	}
	
	protected abstract String getMainStereotypeName();
	
	@Override
	public boolean isValid() {
		return super.isValid() && (this.associationElement != null) && (mainStereotype != null);
	}
	
}
