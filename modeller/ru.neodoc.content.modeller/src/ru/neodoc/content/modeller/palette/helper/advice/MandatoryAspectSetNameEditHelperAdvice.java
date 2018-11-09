package ru.neodoc.content.modeller.palette.helper.advice;

public class MandatoryAspectSetNameEditHelperAdvice extends AssociationSetNameEditHelperAdvice {

	@Override
	protected String getPrefix() {
		return "ma_";
	}
	
}
