package ru.neodoc.content.modeller.palette.helper.advice;

public class ConstraintedSetNameEditHelperAdvice extends AssociationSetNameEditHelperAdvice {

	@Override
	protected String getPrefix() {
		return "constraintRef_";
	}
	
}
