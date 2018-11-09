package ru.neodoc.content.modeller.utils.uml.elements.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.elements.DataTypeElement;
import ru.neodoc.content.modeller.utils.uml.elements.ElementFactory;
import ru.neodoc.content.modeller.utils.uml.elements.Namespace;

public class DataTypeElementImpl extends BaseNamedElementImpl implements DataTypeElement {

	protected DataType dataTypeElement;
	
	protected Namespace parentNamespace;
	
	public DataTypeElementImpl() {
		super();
	}

	public DataTypeElementImpl(Element element) {
		super(element);
	}

	public DataTypeElementImpl(EObject eObject) {
		super(eObject);
	}
	
	@Override
	protected void init() {
		super.init();
		if (super.isValid())
			if (element instanceof DataType)
				this.dataTypeElement = (DataType)element;
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && (this.dataTypeElement != null);
	}
	
	@Override
	public Namespace getNamespace() {
		if (parentNamespace == null) {
			Package p = AlfrescoUMLUtils.getNearestNamespace(dataTypeElement);
			if (p != null)
				parentNamespace = ElementFactory.createNamespaceElement(p);
		}
		return parentNamespace;
	}
	
}
