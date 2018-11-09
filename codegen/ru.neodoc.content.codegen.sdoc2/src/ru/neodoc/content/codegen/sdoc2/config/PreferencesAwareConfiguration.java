package ru.neodoc.content.codegen.sdoc2.config;

import org.eclipse.jface.preference.IPreferenceStore;

public class PreferencesAwareConfiguration extends ExtendedConfiguration {
	
	protected IPreferenceStore store;
	
	protected class PreferencesConfigurationValue extends LocalConfigurationValue {

		protected boolean localIsSet = false;
		
		public PreferencesConfigurationValue(String name, Object value) {
			super(name, value);
		}
		
		@Override
		public Object getValue() {
			if (!store.contains(name) || localIsSet)
				return super.getValue();
			try {
				return store.getBoolean(name);
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				return store.getInt(name);
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				return store.getLong(name);
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				return store.getFloat(name);
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				return store.getDouble(name);
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				return store.getString(name);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
		
		@Override
		public void setValue(Object value) {
			super.setValue(value);
			localIsSet = true;
		}
		
	}

	public PreferencesAwareConfiguration(IPreferenceStore store) {
		super();
		this.store = store;
	}
	
	protected boolean storeHasKey(String name) {
		return store.contains(name);
	}
	
	@Override
	public Object getValue(String name) {
		if (!data.containsKey(name))
			if (storeHasKey(name))
				data.put(name, new PreferencesConfigurationValue(name, null));
		return super.getValue(name);
	}
	
	@Override
	public void setValue(String name, Object value) {
		if (!data.containsKey(name))
			if (storeHasKey(name))
				data.put(name, new PreferencesConfigurationValue(name, value));
		super.setValue(name, value);
	}
}
