package ru.neodoc.content.utils.uml.profile.dataconverter;

import org.eclipse.uml2.uml.EnumerationLiteral;

public class EnumerationLiteralToString extends AbstractToStringConverter<EnumerationLiteral> {

	public EnumerationLiteralToString() {
		super(EnumerationLiteral.class);
	}

	@Override
	protected String doConvert(EnumerationLiteral source, Class<? extends String> exactTargetClass, Object... objects) {
		return source.getName();
	}

}
