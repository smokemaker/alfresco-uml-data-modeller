package ru.neodoc.content.codegen.wizard;

import org.eclipse.jface.wizard.Wizard;

import ru.neodoc.content.codegen.CodegenSubject;

public class SubjectAwareWizard extends Wizard {

	protected CodegenSubject codegenSubject = null;

	public CodegenSubject getCodegenSubject() {
		return codegenSubject;
	}

	public void setCodegenSubject(CodegenSubject codegenSubject) {
		this.codegenSubject = codegenSubject;
	}

	public SubjectAwareWizard(CodegenSubject codegenSubject) {
		super();
		this.codegenSubject = codegenSubject;
	}
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
