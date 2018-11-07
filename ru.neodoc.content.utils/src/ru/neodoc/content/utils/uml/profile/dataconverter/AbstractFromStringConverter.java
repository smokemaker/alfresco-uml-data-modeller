package ru.neodoc.content.utils.uml.profile.dataconverter;

public abstract class AbstractFromStringConverter<TargetClass> extends AbstractDataConverter<String, TargetClass> {

	public AbstractFromStringConverter(Class<TargetClass> targetClass) {
		super(String.class, targetClass);
	}

}
