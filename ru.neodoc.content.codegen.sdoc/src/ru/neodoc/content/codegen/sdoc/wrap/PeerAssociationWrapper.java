package ru.neodoc.content.codegen.sdoc.wrap;

import ru.neodoc.content.modeller.utils.uml.elements.PeerAssociation;

public class PeerAssociationWrapper extends AssociationWrapper {

	protected PeerAssociation peerAssociation;
	
	public static PeerAssociationWrapper newInstance(PeerAssociation wrappedElement) {
		return new PeerAssociationWrapper(wrappedElement);
	}
	
	private PeerAssociationWrapper(PeerAssociation wrappedElement) {
		super(wrappedElement);
		peerAssociation = wrappedElement;
		
	}

}
