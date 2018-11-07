package ru.neodoc.content.codegen.sdoc.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import ru.neodoc.content.codegen.sdoc.SdocCodegenPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = SdocCodegenPlugin.getStore();
		store.setDefault(PreferenceConstants.P_SELECT_ALL_NAMESPACES, true);
		store.setDefault(PreferenceConstants.P_SKIP_NAMESPACE_SELECTION, true);
		store.setDefault(PreferenceConstants.P_DEFAULT_GENERATE_JAVA, true);
		store.setDefault(PreferenceConstants.P_DEFAULT_GENERATE_JAVASCRIPT, false);
		
/*		store.setDefault(PreferenceConstants.P_CHOICE, "choice2");
		store.setDefault(PreferenceConstants.P_STRING,
				"Default value");
*/	}

}
