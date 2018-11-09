package ru.neodoc.content.codegen.sdoc2.wizard;

import org.eclipse.jface.wizard.IWizardPage;

import ru.neodoc.content.codegen.CodegenSubject;
import ru.neodoc.content.codegen.sdoc2.CodegenManager;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.DispatchedPage;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.PageDispatcher;
import ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizard;
import ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizardPage;

public class SdocCodegenWizard extends SourceCodeGeneratorWizard {

	protected CodegenManager codegenManager = null;
	protected PageDispatcher pageDispatcher;
	
	public SdocCodegenWizard(CodegenSubject codegenSubject) {
		super(codegenSubject);
		codegenManager = new CodegenManager(codegenSubject);
		pageDispatcher = new RootPageDispatcher(codegenManager);
	}

	@Override
	public void addPages() {
		// TODO Auto-generated method stub
//		super.addPages();
//		Page1SelectNamespaces page1 = new Page1SelectNamespaces();
//		page1.setCodegenManager(codegenManager);
//		Page2ListNamespacesToGenerate page2 = new Page2ListNamespacesToGenerate();
//		page2.setCodegenManager(codegenManager);
		
//		this.addPage(page1);
//		this.addPage(page2);
/*		StartPage sp = new StartPage("START");
		sp.setCodegenSubject(codegenSubject);
		this.addPage(sp);
*/	
		for (DispatchedPage page: pageDispatcher.getPages())
			addPage(page);
		
	}
	
	@Override
	public SourceCodeGeneratorWizardPage getStartPage() {
//		return pageDispatcher.getStartPage(); 
		return pageDispatcher.goNext(); 
	}
	
	@Override
	public boolean canFinish() {
		IWizardPage current = getContainer().getCurrentPage();
		if (SdocCodegenWizardPage.class.isAssignableFrom(current.getClass())){
			SdocCodegenWizardPage currentPage = (SdocCodegenWizardPage)current; 
			return (currentPage != null) && (currentPage.isLastPage());
		} else
			return false;
	}
	
/*	@Override
	public boolean needsProgressMonitor() {
		SdocCodegenWizardPage currentPage = (SdocCodegenWizardPage)getContainer().getCurrentPage(); 
		return currentPage.getClass().isAssignableFrom(Page5Generation.class);
	}
*/	
	
	@Override
	public boolean performFinish() {
		return true;
	}
}
