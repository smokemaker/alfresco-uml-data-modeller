package ru.neodoc.content.utils.uml.profile.dataconverter;

import ru.neodoc.content.utils.CommonUtils;

public class StringToBoolean extends AbstractFromStringConverter<Boolean> {

	public StringToBoolean() {
		super(Boolean.class);
	}

	@Override
	protected Boolean getNullValue() {
		return false;
	}
	
	@Override
	protected Boolean doConvert(String source, Class<? extends Boolean> exactTargetClass, Object...objects) {
		try {
			return Boolean.valueOf(source);
		} catch (Exception e) {
			return false;
		}
	}

}
