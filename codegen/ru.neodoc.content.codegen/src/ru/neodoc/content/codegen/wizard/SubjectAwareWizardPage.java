package ru.neodoc.content.codegen.wizard;

import org.eclipse.jface.wizard.IWizardPage;

import ru.neodoc.content.codegen.CodegenSubject;

public interface SubjectAwareWizardPage extends IWizardPage {

	CodegenSubject getCodegenSubject();

	void setCodegenSubject(CodegenSubject codegenSubject);

}