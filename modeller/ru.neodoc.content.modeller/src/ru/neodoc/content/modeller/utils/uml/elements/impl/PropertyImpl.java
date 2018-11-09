package ru.neodoc.content.modeller.utils.uml.elements.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.elements.DataTypeElement;
import ru.neodoc.content.modeller.utils.uml.elements.Property;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class PropertyImpl extends BaseTitledElementImpl implements Property {

	protected org.eclipse.uml2.uml.Property propertyElement;
	
	protected DataTypeElement dataTypeElement;
	
	public PropertyImpl() {
		super();
	}

	public PropertyImpl(Element element) {
		super(element);
	}

	public PropertyImpl(EObject eObject) {
		super(eObject);
	}

	@Override
	protected void init() {
		super.init();
		if (super.isValid())
			if (namedElement instanceof org.eclipse.uml2.uml.Property){
				this.propertyElement = (org.eclipse.uml2.uml.Property) namedElement;
				this.mainStereotype = AlfrescoUMLUtils.getStereotype(propertyElement, AlfrescoProfile.ForProperty.Property._NAME);
			}
	}

	@Override
	public boolean isValid() {
		return super.isValid() && (this.mainStereotype != null) && (this.propertyElement != null);
	}
	
	@Override
	public DataTypeElement getDataType() {
		if (!isValid())
			return null;
		if (this.dataTypeElement == null) {
			Type t = this.propertyElement.getType();
			DataTypeElement dte = new DataTypeElementImpl(t);
			if (dte.isValid()){
				this.dataTypeElement = dte; 
			}
		}
		return this.dataTypeElement;
	}
}
