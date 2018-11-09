package ru.neodoc.content.codegen.generate.wizard;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizardPage;

public class CodegenWizardDialog extends WizardDialog {

	public CodegenWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
	}

	@Override
	protected void backPressed() {
		IWizardPage page = getCurrentPage();
		super.backPressed();
		if ((page!=null) && (page instanceof SourceCodeGeneratorWizardPage)) {
			((SourceCodeGeneratorWizardPage)page).backPressed();
		}
	}
	
	@Override
	protected void nextPressed() {
		IWizardPage page = getCurrentPage();
		super.nextPressed();
		if ((page!=null) && (page instanceof SourceCodeGeneratorWizardPage)) {
			((SourceCodeGeneratorWizardPage)page).nextPressed();
		}
	}
}
