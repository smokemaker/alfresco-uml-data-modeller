package ru.neodoc.content.codegen.wizard;

import org.eclipse.jface.wizard.WizardPage;

import ru.neodoc.content.codegen.CodegenSubject;

public abstract class SubjectAwareWizardPageImpl extends WizardPage implements SubjectAwareWizardPage {

	protected CodegenSubject codegenSubject = null;
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.codegen.wizard.SubjectAwareWizardPage#getCodegenSubject()
	 */
	@Override
	public CodegenSubject getCodegenSubject() {
		return codegenSubject;
	}

	/* (non-Javadoc)
	 * @see ru.neodoc.content.codegen.wizard.SubjectAwareWizardPage#setCodegenSubject(ru.neodoc.content.codegen.CodegenSubject)
	 */
	@Override
	public void setCodegenSubject(CodegenSubject codegenSubject) {
		this.codegenSubject = codegenSubject;
	}

	protected SubjectAwareWizardPageImpl(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

}
