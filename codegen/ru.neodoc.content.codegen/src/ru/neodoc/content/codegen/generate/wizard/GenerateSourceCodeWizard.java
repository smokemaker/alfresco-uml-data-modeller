package ru.neodoc.content.codegen.generate.wizard;

import ru.neodoc.content.codegen.CodegenSubject;
import ru.neodoc.content.codegen.wizard.SubjectAwareWizard;

public class GenerateSourceCodeWizard extends SubjectAwareWizard {

	
	public GenerateSourceCodeWizard(CodegenSubject subject){
		super(subject);
	}

	@Override
	public boolean needsPreviousAndNextButtons() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void addPages() {
		// TODO Auto-generated method stub
		super.addPages();
		ChooseGeneratorPage cgp = new ChooseGeneratorPage();
		cgp.setCodegenSubject(codegenSubject);
		addPage(cgp);
		
		RedirectPage rp = new RedirectPage("REDIRECT");
		rp.setCodegenSubject(codegenSubject);
		addPage(rp);
	}
	
}
