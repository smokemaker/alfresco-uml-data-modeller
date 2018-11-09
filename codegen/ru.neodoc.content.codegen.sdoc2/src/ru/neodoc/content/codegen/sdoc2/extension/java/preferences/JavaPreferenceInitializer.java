package ru.neodoc.content.codegen.sdoc2.extension.java.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import ru.neodoc.content.codegen.sdoc2.extension.java.JavaCodegenExtension;
import ru.neodoc.content.codegen.sdoc2.extension.preferences.ExtensionPreferenceInitializer;

/**
 * Class used to initialize default preference values.
 */
public class JavaPreferenceInitializer extends ExtensionPreferenceInitializer {

	@Override
	protected String getPreferenceStoreQualifier() {
		return JavaCodegenExtension.EXTENSION_ID;
	}
	
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		super.initializeDefaultPreferences(store);
/*		store.setDefault(JavascriptPreferenceConstants.P_SELECT_ALL_NAMESPACES, true);
		store.setDefault(JavascriptPreferenceConstants.P_SKIP_NAMESPACE_SELECTION, true);
		store.setDefault(JavascriptPreferenceConstants.P_DEFAULT_GENERATE_JAVA, true);
		store.setDefault(JavascriptPreferenceConstants.P_DEFAULT_GENERATE_JAVASCRIPT, false);
*/	}
	
}
