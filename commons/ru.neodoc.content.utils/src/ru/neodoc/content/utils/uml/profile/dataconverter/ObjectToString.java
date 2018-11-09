package ru.neodoc.content.utils.uml.profile.dataconverter;

public class ObjectToString extends AbstractToStringConverter<Object> {

	public ObjectToString() {
		super(Object.class);
	}

	@Override
	protected String doConvert(Object source, Class<? extends String> exactTargetClass, Object...objects) {
		return source.toString();
	}

}
