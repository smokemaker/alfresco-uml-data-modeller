package ru.neodoc.content.codegen.sdoc2;

import ru.neodoc.content.codegen.CodegenSubject;
import ru.neodoc.content.codegen.sdoc2.wizard.SdocCodegenWizard;
import ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizard;

public class SourceCodeGenerator implements ru.neodoc.content.codegen.SourceCodeGenerator {

	protected CodegenSubject subject;
	
	protected SourceCodeGeneratorWizard wizard = null;
	
	public SourceCodeGenerator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setSubject(CodegenSubject subject) {
		this.subject = subject;
	}
	
	@Override
	public boolean isAccepatable() {
		return (subject!=null) && (subject.isModel() || subject.isNamespace());
	}
	
	@Override
	public String getGeneratorId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SourceCodeGeneratorWizard getWizard() {
		if (wizard == null)
			wizard = new SdocCodegenWizard(subject); 
		return wizard;
	}
	
}
