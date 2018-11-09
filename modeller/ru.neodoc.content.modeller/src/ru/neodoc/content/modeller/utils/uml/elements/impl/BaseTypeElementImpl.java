package ru.neodoc.content.modeller.utils.uml.elements.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.modeller.utils.uml.elements.BaseTypeElement;

public class BaseTypeElementImpl extends BaseTitledElementImpl implements BaseTypeElement {
	
	protected Type typeElement;

	public BaseTypeElementImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BaseTypeElementImpl(Element element) {
		super(element);
		// TODO Auto-generated constructor stub
	}

	public BaseTypeElementImpl(EObject eObject) {
		super(eObject);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void init() {
		super.init();
		if (super.isValid())
			if (this.namedElement instanceof Type)
				this.typeElement = (Type)namedElement;
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && (this.typeElement != null);
	}
	
}
