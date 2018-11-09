package ru.neodoc.content.modeller.utils.uml.elements;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.elements.impl.AlfrescoImpl;
import ru.neodoc.content.modeller.utils.uml.elements.impl.AspectImpl;
import ru.neodoc.content.modeller.utils.uml.elements.impl.BasePackageElementImpl;
import ru.neodoc.content.modeller.utils.uml.elements.impl.DataTypeElementImpl;
import ru.neodoc.content.modeller.utils.uml.elements.impl.ModelImpl;
import ru.neodoc.content.modeller.utils.uml.elements.impl.NamespaceImpl;
import ru.neodoc.content.modeller.utils.uml.elements.impl.TypeImpl;

public class ElementFactory {

	public static BaseElement createElement(EObject eObject){
		if (eObject instanceof Element)
			return createElement((Element)eObject);
		return null;
	}
	
	public static BaseElement createElement(Element element) {
		if (element instanceof Package){ 
			BasePackageElementImpl result = createPackageElement((Package)element);
			if ((result != null) && result.isValid())
				return result;
		} else
		if (element instanceof DataType) {
			DataTypeElement dte = new DataTypeElementImpl(element);
			if (dte.isValid())
				return dte;
		} else
		if (element instanceof org.eclipse.uml2.uml.Class) {
			BaseClassElement bce = null;
			if (AlfrescoUMLUtils.isAspect(element))
				bce = new AspectImpl(element);
			else if (AlfrescoUMLUtils.isType(element))
				bce = new TypeImpl(element);
			if (bce!=null && bce.isValid())
				return bce;
		}
		
		return null;
	}
	
	public static BasePackageElementImpl createPackageElement(Package pack){
		if (AlfrescoUMLUtils.isAlfresco(pack))
			return createAlfrescoElement(pack);
		if (AlfrescoUMLUtils.isModel(pack))
			return createModelElement(pack);
		if (AlfrescoUMLUtils.isNamespace(pack))
			return new NamespaceImpl(pack);
		return null;
	}
	
	public static AlfrescoImpl createAlfrescoElement(Package pack) {
		AlfrescoImpl alfresco = new AlfrescoImpl(pack);
		if ((alfresco!=null) && alfresco.isValid())
			return alfresco;
		return null;
	}

	public static ModelImpl createModelElement(Package pack) {
		ModelImpl model = new ModelImpl(pack);
		if ((model!=null) && model.isValid())
			return model;
		return null;
	}

	public static Namespace createNamespaceElement(Package pack) {
		NamespaceImpl namespace = new NamespaceImpl(pack);
		if ((namespace!=null) && namespace.isValid())
			return namespace;
		return null;
	}
}
