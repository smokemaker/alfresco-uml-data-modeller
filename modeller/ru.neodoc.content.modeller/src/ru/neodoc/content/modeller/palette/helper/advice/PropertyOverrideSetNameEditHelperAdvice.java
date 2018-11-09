package ru.neodoc.content.modeller.palette.helper.advice;

public class PropertyOverrideSetNameEditHelperAdvice extends AssociationSetNameEditHelperAdvice {

	@Override
	protected String getPrefix() {
		return "po_";
	}
	
}
