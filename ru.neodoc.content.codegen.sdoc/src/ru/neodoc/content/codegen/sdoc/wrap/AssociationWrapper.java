package ru.neodoc.content.codegen.sdoc.wrap;

import ru.neodoc.content.modeller.utils.uml.elements.BaseAssociationElement;
import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;

public abstract class AssociationWrapper extends NamedElementWrapper {

	protected BaseAssociationElement associationElement;
	
	AssociationWrapper(BaseAssociationElement wrappedElement) {
		super(wrappedElement);
		this.associationElement = wrappedElement;
	}
	
	@Override
	protected String getPrefix() {
		return "ASSOC_";
	}

	public ClassWrapper getTargetType(){
		if (this.associationElement == null)
			return null;
		
		BaseClassElement bce = associationElement.getTarget();
		if (bce == null)
			return null;
		BaseWrapper bw = WrapperFactory.get(bce); 
		return (ClassWrapper) bw;
	}
}
