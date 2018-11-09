package ru.neodoc.content.modeller.utils.uml.elements.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtilsDeprecated;
import ru.neodoc.content.modeller.utils.uml.elements.BaseTitledElement;

public class BaseTitledElementImpl extends BaseNamedElementImpl implements BaseTitledElement {

	protected Stereotype mainStereotype;
	
	public BaseTitledElementImpl() {
		super();
	}

	public BaseTitledElementImpl(Element element) {
		super(element);
	}

	public BaseTitledElementImpl(EObject eObject) {
		super(eObject);
	}

	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.elements.BaseTitledElement#getTitle()
	 */
	@Override
	public String getTitle(){
		return AlfrescoUMLUtilsDeprecated.getTitle(namedElement);
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.elements.BaseTitledElement#getDescription()
	 */
	@Override
	public String getDescription(){
		return AlfrescoUMLUtilsDeprecated.getDescription(namedElement);
	}
}
