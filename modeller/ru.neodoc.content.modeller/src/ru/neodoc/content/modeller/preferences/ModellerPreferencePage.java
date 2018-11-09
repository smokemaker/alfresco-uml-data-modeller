package ru.neodoc.content.modeller.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ru.neodoc.content.modeller.ContentModellerPlugin;

public class ModellerPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public ModellerPreferencePage() {
		super(GRID);
		setPreferenceStore(ContentModellerPlugin.getDefault().getPreferenceStore());
		setDescription("Content modeller preference page");
	}
	
	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createFieldEditors() {
		addField(
				new BooleanFieldEditor(
					PreferenceConstants.P_UPDATE_BEFORE_EXPORT,
					"Update dependencies before export to XML",
					getFieldEditorParent()));	
		addField(
				new BooleanFieldEditor(
					PreferenceConstants.P_BUGFIX_521273,
					"Use bugfix for #521273",
					getFieldEditorParent()));	
	}

}
