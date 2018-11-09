package ru.neodoc.content.codegen;

import ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizard;

public interface SourceCodeGenerator {
	
	public String getGeneratorId();
	
	public void setSubject(CodegenSubject subject);
	public boolean isAccepatable();
	
	public SourceCodeGeneratorWizard getWizard();
	
}
