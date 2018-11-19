package ru.neodoc.content.codegen.sdoc2.config;

import java.util.HashMap;
import java.util.Map;

import ru.neodoc.content.utils.CommonUtils;

public class SimpleConfiguration implements Configuration {

	protected Map<String, Object> data = new HashMap<>();
	
	protected Configuration parentConfiguration = null;
	
	public SimpleConfiguration() {
		setActive(true);
	}
	
	@Override
	public boolean isActive() {
		return CommonUtils.isTrue((Boolean)data.get(PROP_IS_ACTIVE));
	}

	@Override
	public void setActive(boolean value) {
		data.put(PROP_IS_ACTIVE, value);
	}
	
	@Override
	public void setParentConfiguration(Configuration configuration) {
		this.parentConfiguration = configuration;
	}

	@Override
	public Object getValue(String name) {
		return data.get(name);
	}

	@Override
	public void setValue(String name, Object value) {
		data.put(name, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getTypedValue(String name) {
		Object obj = getValue(name);
		return (T)obj;
	}

	@Override
	public String getString(String name) {
		return getTypedValue(name);
	}

	@Override
	public boolean getBoolean(String name) {
		Boolean b = getTypedValue(name);
		return CommonUtils.isTrue(b);
	}

}
