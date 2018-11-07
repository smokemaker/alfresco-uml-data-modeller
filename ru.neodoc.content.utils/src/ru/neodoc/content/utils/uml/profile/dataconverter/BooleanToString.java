package ru.neodoc.content.utils.uml.profile.dataconverter;

public class BooleanToString extends AbstractToStringConverter<Boolean> {

	public BooleanToString() {
		super(Boolean.class);
	}

	@Override
	protected String getNullValue() {
		return (new Boolean(false)).toString();
	}
	
	@Override
	protected String doConvert(Boolean source, Class<? extends String> exactTargetClass, Object...objects) {
		return source.toString();
	}

}
