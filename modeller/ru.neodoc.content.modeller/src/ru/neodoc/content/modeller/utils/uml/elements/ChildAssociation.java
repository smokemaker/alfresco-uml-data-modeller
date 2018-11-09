package ru.neodoc.content.modeller.utils.uml.elements;

public interface ChildAssociation extends PeerAssociation {

	String getChildName();
	boolean isDuplicate();
	boolean isPropogateTimestamps();
	
}
