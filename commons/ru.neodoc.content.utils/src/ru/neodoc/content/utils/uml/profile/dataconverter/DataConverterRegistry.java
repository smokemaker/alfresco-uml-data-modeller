package ru.neodoc.content.utils.uml.profile.dataconverter;

public interface DataConverterRegistry {

	public static final DataConverterRegistry INSTANCE = new DataConverterRegistryImpl(DataConverterEdge.class); 

	public static interface DataConverterRegistryListener {
		public void onClassAdded(final Class<?> clazz, final DataConverterRegistry registry);
		public void onConverterAdded(final DataConverter<?, ?> converter, final DataConverterRegistry registry); 
	}

	@SuppressWarnings("unchecked")
	public void register(Class<? extends DataConverter<?, ?>>...converterClasses);
//	public void registerConverter(Class<?Class<? extends DataConverter<?, ?>>...converterClasses);

	Object convert(Object source, Class<?> targetClass, Object...objects);

	void addConverter(Class<?> sourceClass, Class<?> targetClass, DataConverter<?, ?> dataConverter);

	void addConverter(DataConverter<?, ?> dataConverter);
}
