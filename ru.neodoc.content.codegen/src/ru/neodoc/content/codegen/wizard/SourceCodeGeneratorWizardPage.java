package ru.neodoc.content.codegen.wizard;

import org.eclipse.swt.widgets.Composite;

public interface SourceCodeGeneratorWizardPage extends SubjectAwareWizardPage {

	void createControl(Composite parent);

	void backPressed();

	void nextPressed();

}