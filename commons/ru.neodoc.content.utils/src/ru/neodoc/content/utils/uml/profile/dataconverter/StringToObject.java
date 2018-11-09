package ru.neodoc.content.utils.uml.profile.dataconverter;

public class StringToObject extends AbstractFromStringConverter<Object> {

	public StringToObject() {
		super(Object.class);
	}

	@Override
	protected Object doConvert(String source, Class<? extends Object> exactTargetClass, Object...objects) {
		return source;
	}

}
