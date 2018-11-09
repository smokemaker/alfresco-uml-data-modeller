package ru.neodoc.content.codegen.sdoc2.extension.javascript.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import ru.neodoc.content.codegen.sdoc2.extension.javascript.JavaScriptCodegenExtension;
import ru.neodoc.content.codegen.sdoc2.extension.preferences.ExtensionPreferenceInitializer;

/**
 * Class used to initialize default preference values.
 */
public class JavascriptPreferenceInitializer extends ExtensionPreferenceInitializer {

	@Override
	protected String getPreferenceStoreQualifier() {
		return JavaScriptCodegenExtension.EXTENSION_ID;
	}
	
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		super.initializeDefaultPreferences(store);
/*		store.setDefault(JavascriptPreferenceConstants.P_SELECT_ALL_NAMESPACES, true);
		store.setDefault(JavascriptPreferenceConstants.P_SKIP_NAMESPACE_SELECTION, true);
		store.setDefault(JavascriptPreferenceConstants.P_DEFAULT_GENERATE_JAVA, true);
		store.setDefault(JavascriptPreferenceConstants.P_DEFAULT_GENERATE_JAVASCRIPT, false);
*/	}
	
}
