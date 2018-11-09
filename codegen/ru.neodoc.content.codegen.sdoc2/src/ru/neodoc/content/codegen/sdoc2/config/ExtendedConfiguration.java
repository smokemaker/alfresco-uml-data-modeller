package ru.neodoc.content.codegen.sdoc2.config;

public class ExtendedConfiguration extends SimpleConfiguration {

	protected abstract class ConfigurationValue {
		public abstract Object getValue();
		public abstract void setValue(Object value);
	}

	protected class LocalConfigurationValue extends ConfigurationValue {
		private Object value; 
		protected String name;
		
		public LocalConfigurationValue(String name, Object value) {
			this.name = name;
			this.value = value;
		}
		
		public Object getValue() {
			return value;
		}
		
		public void setValue(Object value) {
			this.value = value;
		}
	}
	
	protected class ParentConfigurationValue extends LocalConfigurationValue {

		public ParentConfigurationValue(String name, Object value) {
			super(name, value);
		}
		
		@Override
		public Object getValue() {
			if (ExtendedConfiguration.this.parentConfiguration!=null) {
				setValue(ExtendedConfiguration.this.parentConfiguration.getValue(name));
			}
			return super.getValue();
		}
		
		@Override
		public void setValue(Object value) {
			if (ExtendedConfiguration.this.parentConfiguration!=null) {
				ExtendedConfiguration.this.parentConfiguration.setValue(name, value);
			}
			super.setValue(value);
		}
	}
	
	public ExtendedConfiguration() {
	}
	
	@Override
	public Object getValue(String name) {
		if (!data.containsKey(name)) {
			findValue(name);
		}
		if (!data.containsKey(name))
			setLocalValue(name, null);
		return retrieveValue(name);
	}
	
	protected Object findValue(String name) {
		if (parentConfiguration!=null) {
			Object obj = parentConfiguration.getValue(name);
			if (obj!=null)
				data.put(name, new ParentConfigurationValue(name, obj));
			return obj; 
		}
		return null;
	}
	
	protected Object retrieveValue(String name) {
		Object obj = data.get(name);
		if (!ConfigurationValue.class.isAssignableFrom(obj.getClass())) {
			setLocalValue(name, obj);
			return obj;
		} else 
			return ((ConfigurationValue)obj).getValue();
	}
	
	@Override
	public void setValue(String name, Object value) {
		if (!data.containsKey(value)) {
			// try to receive it
			getValue(name);
		}
		if (!data.containsKey(value)) {
			// still don't have
			setLocalValue(name, value);
		} else
			updateValue(name, value);
	}
	
	protected void updateValue(String name, Object value) {
		Object obj = data.get(name);
		if (!ConfigurationValue.class.isAssignableFrom(obj.getClass())) {
			setLocalValue(name, value);
			return;
		}
		((ConfigurationValue)obj).setValue(value);
	}
	
	protected void setLocalValue(String name, Object value) {
		data.put(name, new LocalConfigurationValue(name, value));
	}
	
	
}
