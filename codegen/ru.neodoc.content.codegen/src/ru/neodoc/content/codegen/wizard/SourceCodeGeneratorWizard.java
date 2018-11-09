package ru.neodoc.content.codegen.wizard;

import org.eclipse.jface.wizard.IWizardPage;

import ru.neodoc.content.codegen.CodegenSubject;

public abstract class SourceCodeGeneratorWizard extends SubjectAwareWizard {

	protected IWizardPage initiatorPage;
	
	public SourceCodeGeneratorWizard(CodegenSubject codegenSubject) {
		super(codegenSubject);
	}

	public IWizardPage getInitiatorPage() {
		return initiatorPage;
	}

	public void setInitiatorPage(IWizardPage initiatorPage) {
		this.initiatorPage = initiatorPage;
	}

	public abstract SourceCodeGeneratorWizardPage getStartPage();
	
}
