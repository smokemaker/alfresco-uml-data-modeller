package ru.neodoc.content.codegen.sdoc.wrap;

import ru.neodoc.content.modeller.utils.uml.elements.ChildAssociation;

public class ChildAssociationWrapper extends AssociationWrapper {

	protected ChildAssociation childAssociation;
	
	public static ChildAssociationWrapper newInstance(ChildAssociation wrappedElement) {
		return new ChildAssociationWrapper(wrappedElement);
	}
	
	private ChildAssociationWrapper(ChildAssociation wrappedElement) {
		super(wrappedElement);
		this.childAssociation = wrappedElement;
	}

}
