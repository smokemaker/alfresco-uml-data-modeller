package ru.neodoc.content.codegen.sdoc2.extension.java.preferences;

import org.eclipse.ui.IWorkbench;

import ru.neodoc.content.codegen.sdoc2.extension.java.JavaCodegenExtension;
import ru.neodoc.content.codegen.sdoc2.extension.preferences.ExtensionPreferencePage;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class JavaPreferencePage
	extends ExtensionPreferencePage {

	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
//	public void createFieldEditors() {
/*		addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH, 
				"&Directory preference:", getFieldEditorParent()));
*//*		addField(
			new BooleanFieldEditor(
				JavascriptPreferenceConstants.P_SELECT_ALL_NAMESPACES,
				"Select all namespaces to generate",
				getFieldEditorParent()));

		addField(
			new BooleanFieldEditor(
				JavascriptPreferenceConstants.P_SKIP_NAMESPACE_SELECTION,
				"Skip namespace selection page if possible",
				getFieldEditorParent()));
*/
		/*addField(
				new BooleanFieldEditor(
					PreferenceConstants.P_DEFAULT_GENERATE_JAVA,
					"Generate Java sources by default",
					getFieldEditorParent()));

		addField(
				new BooleanFieldEditor(
					PreferenceConstants.P_DEFAULT_GENERATE_JAVASCRIPT,
					"Skip namespace selection page if possible",
					getFieldEditorParent()));*/
		
/*		addField(new RadioGroupFieldEditor(
				PreferenceConstants.P_CHOICE,
			"An example of a multiple-choice preference",
			1,
			new String[][] { { "&Choice 1", "choice1" }, {
				"C&hoice 2", "choice2" }
		}, getFieldEditorParent()));
		addField(
			new StringFieldEditor(PreferenceConstants.P_STRING, "A &text preference:", getFieldEditorParent()));
*/	
//	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	@Override
	protected String getPreferenceStoreQualifier() {
		return JavaCodegenExtension.EXTENSION_ID;
	}

	@Override
	protected String getPreferenceDiscription() {
		return "Javscript generation preferences";
	}
	
}