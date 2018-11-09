package ru.neodoc.content.codegen.sdoc2.config;

public interface Configuration {
	
	public static final String PROP_IS_ACTIVE = "_IS_ACTIVE";
	
	// void loadRootObjects(List<BaseWrapper> rootObjects);
	boolean isActive();
	void setActive(boolean value);
	
	void setParentConfiguration(Configuration configuration);

	Object getValue(String name);
	
	<T> T getTypedValue(String name);
	String getString(String name);
	boolean getBoolean(String name);
	
	void setValue(String name, Object value);
}
