package ru.neodoc.content.codegen.sdoc2.extension.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ru.neodoc.content.codegen.sdoc2.extension.SdocCodegenExtensionImpl;
import ru.neodoc.content.codegen.sdoc2.extension.javascript.preferences.JavascriptPreferenceConstants;

public abstract class ExtensionPreferencePage 
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage  {

	public ExtensionPreferencePage() {
		super();
		doInitialize();
	}
	
	public ExtensionPreferencePage(int value) {
		super(value);
		doInitialize();
	}
	
	protected final void doInitialize() {
		setPreferenceStore(getPreferenceStoreQualifier());
		setDescription(getPreferenceDiscription());
		doAfterInitialize();
	}
	
	protected abstract String getPreferenceStoreQualifier();
	
	protected abstract String getPreferenceDiscription();
	
	protected void doAfterInitialize() {
		// TO OVERRIDE
	}
	
	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createFieldEditors() {
		addField(
				new BooleanFieldEditor(
					JavascriptPreferenceConstants.P_DEFAULT_ACTIVE,
					"Active by default",
					getFieldEditorParent()));		
	}

	public void setPreferenceStore(String qualifier) {
		super.setPreferenceStore(SdocCodegenExtensionImpl.getPreferenceStore(qualifier));
	}
}
