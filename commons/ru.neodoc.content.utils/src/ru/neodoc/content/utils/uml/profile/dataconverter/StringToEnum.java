package ru.neodoc.content.utils.uml.profile.dataconverter;

import ru.neodoc.content.utils.uml.profile.dataconverter.DataConverterRegistry.DataConverterRegistryListener;

@SuppressWarnings("rawtypes")
public class StringToEnum extends AbstractFromStringConverter<Enum> implements DataConverterRegistryListener{

	public StringToEnum() {
		super(Enum.class);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Enum doConvert(String source, Class<? extends Enum> exactTargetClass, Object...objects) {
		return Enum.valueOf(exactTargetClass, source);
	}

	@Override
	public void onClassAdded(Class<?> clazz, DataConverterRegistry registry) {
		if (Enum.class.isAssignableFrom(clazz))
			registry.addConverter(sourceClass, clazz, this);
		
	}

	@Override
	public void onConverterAdded(DataConverter<?, ?> converter, DataConverterRegistry registry) {
		// NOOP
	}

}
