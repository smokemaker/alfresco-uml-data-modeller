package ru.neodoc.content.utils.uml.profile.dataconverter;

public abstract class AbstractToStringConverter<SourceClass> extends AbstractDataConverter<SourceClass, java.lang.String> {

	public AbstractToStringConverter(Class<SourceClass> sourceClass) {
		super(sourceClass, String.class);
	}

}
