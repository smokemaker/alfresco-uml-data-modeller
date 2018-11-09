package ru.neodoc.content.codegen.sdoc2.extension.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import ru.neodoc.content.codegen.sdoc2.extension.SdocCodegenExtensionImpl;

public abstract class ExtensionPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public final void initializeDefaultPreferences() {
		String qualifier = getPreferenceStoreQualifier();
		IPreferenceStore store = SdocCodegenExtensionImpl.getPreferenceStore(qualifier);
		initializeDefaultPreferences(store);
	}

	protected void initializeDefaultPreferences(IPreferenceStore store) {
		store.setDefault(ExtensionPreferenceConstants.P_DEFAULT_ACTIVE, false);
	}
	
	protected abstract String getPreferenceStoreQualifier();
}
