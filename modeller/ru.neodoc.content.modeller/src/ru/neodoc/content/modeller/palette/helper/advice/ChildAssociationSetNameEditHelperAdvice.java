package ru.neodoc.content.modeller.palette.helper.advice;

public class ChildAssociationSetNameEditHelperAdvice extends AssociationSetNameEditHelperAdvice {

	@Override
	protected String getPrefix() {
		return "ca_";
	}
	
}
