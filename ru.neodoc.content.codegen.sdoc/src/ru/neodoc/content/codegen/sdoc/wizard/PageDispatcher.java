package ru.neodoc.content.codegen.sdoc.wizard;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import ru.neodoc.content.codegen.sdoc.CodegenManager;
import ru.neodoc.content.codegen.sdoc.SdocCodegenPlugin;
import ru.neodoc.content.codegen.sdoc.preferences.PreferenceConstants;
import ru.neodoc.content.codegen.sdoc.wizard.pages.Page0Empty;
import ru.neodoc.content.codegen.sdoc.wizard.pages.Page1SelectNamespaces;
import ru.neodoc.content.codegen.sdoc.wizard.pages.Page2ListNamespacesToGenerate;
import ru.neodoc.content.codegen.sdoc.wizard.pages.Page3SetJavaGenerationTarget;
import ru.neodoc.content.codegen.sdoc.wizard.pages.Page4SetJavaImports;
import ru.neodoc.content.codegen.sdoc.wizard.pages.Page5Generation;
import ru.neodoc.content.codegen.sdoc.wizard.pages.Page6SetJavascriptGenerationTarget;

public class PageDispatcher {
	
	protected CodegenManager codegenManager;
	protected SdocCodegenWizard wizard;
	
	protected HashMap<String, SdocCodegenWizardPage> pages = new LinkedHashMap<>();
	
	protected boolean alwaysChooseSources = true;
	protected boolean sourcesAreChosen = false;
	
	protected boolean skipNamespaceSelectionPage = SdocCodegenPlugin.getBoolean(PreferenceConstants.P_SKIP_NAMESPACE_SELECTION);
	
	protected SdocCodegenWizardPage currentPage;
	
	public PageDispatcher(CodegenManager codegenManager, SdocCodegenWizard wizard) {
		this.codegenManager = codegenManager;
		this.wizard = wizard;
		initializePages();
	}

	protected void initializePages(){
		
		createPage1();
		createPage2();
		createPage3Java();
		createPage4Java();
		createPage5();
		createPage6();
		
	}
	
	public List<SdocCodegenWizardPage> getPages(){
		
		return new LinkedList<>(pages.values());
		
	}
	
	
	
	protected void preparePage(SdocCodegenWizardPage page) {
		page.setCodegenManager(codegenManager);
		page.setWizard(wizard);
		page.setPageDispatcher(this);
		pages.put(page.getName(), page);
	}
	
	public SdocCodegenWizardPage getStartPage(){
/*		if (codegenManager.needToChooseNamespaces()) {
			Page1SelectNamespaces page1 = new Page1SelectNamespaces();
			preparePage(page1);
			return page1;
		} else {
			return get();
		}*/
		return getNextPage();
	}
	
	protected SdocCodegenWizardPage createPage1(){
		Page1SelectNamespaces page1 = new Page1SelectNamespaces();
		preparePage(page1);
		if (skipNamespaceSelectionPage)
			page1.skipOnce();
		return page1;
	}
	
	protected SdocCodegenWizardPage createPage2(){
		Page2ListNamespacesToGenerate page2 = new Page2ListNamespacesToGenerate();
		preparePage(page2);
		return page2;
	} 
	
	protected SdocCodegenWizardPage createPage3Java(){
		Page3SetJavaGenerationTarget page3 = new Page3SetJavaGenerationTarget();
		preparePage(page3);
		return page3;
	}
	
	protected SdocCodegenWizardPage createPage4Java(){
		Page4SetJavaImports page4 = new Page4SetJavaImports();
		preparePage(page4);
		return page4;
	}
	
	protected SdocCodegenWizardPage createPage5(){
		Page5Generation page5 = new Page5Generation();
		preparePage(page5);
		return page5;
	}
	
	protected SdocCodegenWizardPage createPage6(){
		Page6SetJavascriptGenerationTarget page6 = new Page6SetJavascriptGenerationTarget();
		preparePage(page6);
		return page6;
	}
	
	protected SdocCodegenWizardPage getNextPage(String name, boolean allowSkip){
		currentPage = pages.get(name); 
		currentPage.refresh();
		
		Step step = new Step();
		step.page = currentPage;
		step.prev = currentStep;
		if (currentStep != null)
			currentStep.next = step;
		currentStep = step;
		
		if (allowSkip && currentPage.isSkip()){
			currentPage.skipped();
			return getNextPage();
		}
		currentPage.skipped();
		return currentPage;
	}
	
	public void back(){
		if (currentStep!=null)
			currentStep = currentStep.prev;
		if (currentStep!=null)
			currentPage = currentStep.page;
		currentPage.refresh();
	}
	
	
	
	public SdocCodegenWizardPage getPrevPage(){
		if (currentStep == null)
			return null;
		if (currentStep.prev == null)
			return null;
		return currentStep.prev.page;
	}
	
	public SdocCodegenWizardPage getNextPage(){
		
		if (currentPage == null)
			return getNextPage(Page1SelectNamespaces.PAGE_NAME, codegenManager.hasNamespacesToGenerate());
		
		if (Page1SelectNamespaces.PAGE_NAME.equals(currentPage.getName())){
			return getNextPage(Page2ListNamespacesToGenerate.PAGE_NAME, codegenManager.hasSourceToGenerate());
		}
		
		if (codegenManager.isGenerateJava()) {
			
			if (Page2ListNamespacesToGenerate.PAGE_NAME.equals(currentPage.getName()))
				return getNextPage(Page3SetJavaGenerationTarget.PAGE_NAME, codegenManager.targetsDefined());
			
			if (Page3SetJavaGenerationTarget.PAGE_NAME.equals(currentPage.getName()))
				return getNextPage(Page4SetJavaImports.PAGE_NAME, codegenManager.importsDefined());
			
		}
		
		if (codegenManager.isGenerateJavaScript()) {
			if (Page2ListNamespacesToGenerate.PAGE_NAME.equals(currentPage.getName())
					|| Page4SetJavaImports.PAGE_NAME.equals(currentPage.getName()))
				return getNextPage(Page6SetJavascriptGenerationTarget.PAGE_NAME, codegenManager.targetsDefinedJS());
		}
		
		if (codegenManager.isGenerateJava() || codegenManager.isGenerateJavaScript())
			return getNextPage(Page5Generation.PAGE_NAME, false);
		
		return new Page0Empty("unknown");
	}
	
	public SdocCodegenWizardPage getNextPage_() {
		SdocCodegenWizardPage currentPage = (SdocCodegenWizardPage)wizard.getContainer().getCurrentPage();
		String name = currentPage.getName();
		
		if (Page1SelectNamespaces.PAGE_NAME.equals(name)) {
			return createPage2();
		}
		
		if (Page2ListNamespacesToGenerate.PAGE_NAME.equals(name)){
			if (codegenManager.isGenerateJava())
			return createPage3Java();
		}
		
		if (Page3SetJavaGenerationTarget.PAGE_NAME.equals(name)){
			return createPage4Java();
		}
		
		return null;
	}
	
	protected Step currentStep = null;
	
	protected class Step {
		public SdocCodegenWizardPage page;
		public Step prev;
		public Step next;
	}
	
}
