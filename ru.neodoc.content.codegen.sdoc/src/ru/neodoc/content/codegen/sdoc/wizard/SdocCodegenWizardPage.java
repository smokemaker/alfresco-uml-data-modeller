package ru.neodoc.content.codegen.sdoc.wizard;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import ru.neodoc.content.codegen.sdoc.SdocCodegenPlugin;
import ru.neodoc.content.codegen.sdoc.CodegenManager;
import ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizard;
import ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizardPageImpl;

public abstract class SdocCodegenWizardPage extends SourceCodeGeneratorWizardPageImpl {

	protected CodegenManager codegenManager = null;
	protected Composite container;
	
	protected PageDispatcher pageDispatcher = null;
	
	private IDialogSettings pageSettings = null;
	
	protected boolean skip = false;
	protected boolean skipAlways = false;
	
	public PageDispatcher getPageDispatcher() {
		return pageDispatcher;
	}

	public void setPageDispatcher(PageDispatcher pageDispatcher) {
		this.pageDispatcher = pageDispatcher;
	}

	public CodegenManager getCodegenManager() {
		return codegenManager;
	}

	public void setCodegenManager(CodegenManager codegenManager) {
		this.codegenManager = codegenManager;
	}

	protected SdocCodegenWizardPage(String pageName) {
		super(pageName);
	}
	
	public boolean isSkip() {
		return skip;
	}

	public boolean isSkipAlways() {
		return skipAlways;
	}

	public void skipOnce(){
		this.skip = true;
	}
	
	public void skipAlways(){
		this.skipAlways = true;
	}
	
	public void clearSkip(){
		this.skip = false;
		this.skipAlways = false;
	}
	
	public void skipped(){
		if (!this.skipAlways)
			this.skip = false;
	}
	
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		
		doCreateControl(parent);
		
        // required to avoid an error in the system
        setControl(container);
        setPageComplete(false);
        
        internaleRefresh();
	}
	
	protected boolean isControlCreated(){
		return (container != null);
	}
	
	
	protected void doCreateControl(Composite parent){
		// TO OVERRIDE
	}

	public void refresh(){
		if (isControlCreated())
			internaleRefresh();
	}
	
	protected void internaleRefresh(){
		// TO OVERRIDE
	}
	
	public boolean isLastPage(){
		return false;
	}
	
	@Override
	public IWizardPage getNextPage() {
		return pageDispatcher.getNextPage();
	}
	
	@Override
	public IWizardPage getPreviousPage() {
		IWizardPage page = pageDispatcher.getPrevPage();
		if (page!=null)
			return page;
		page = super.getPreviousPage();
		if (page!=null)
			return page;
		if (SourceCodeGeneratorWizard.class.isAssignableFrom(this.getWizard().getClass())){
			page = ((SourceCodeGeneratorWizard)this.getWizard()).getInitiatorPage();
		}
		return page;
	}
	
	protected IDialogSettings getPageSettings(){
		if (pageSettings == null) {
			IDialogSettings dialogSettings = SdocCodegenPlugin.getDefault().getDialogSettings();
			IDialogSettings section = dialogSettings.getSection(getPageSectionName());
			if (section == null)
				section = dialogSettings.addNewSection(getPageSectionName());
			pageSettings = section;
		}
		return pageSettings;
	}
	
	protected IDialogSettings getSubsection(String path) {
		return getSubsection(getPageSettings(), path);
	}
	
	protected IDialogSettings getSubsection(IDialogSettings parent, String path) {
		IDialogSettings result = parent;
		String[] splitted = path.split("/");
		for (int i=0; i<splitted.length; i++) {
			String sectionName = splitted[i];
			if ((result != null) && (sectionName != null) && (sectionName.trim().length() > 0)){
				sectionName = sectionName.trim();
				IDialogSettings temp = result.getSection(sectionName);
				if (temp == null)
					temp = result.addNewSection(sectionName);
				result = temp;
			}
		}
		return result;
	}
	
	public void setPageSettings(IDialogSettings pageSettings) {
		this.pageSettings = pageSettings;
	}

	@Override
	public void backPressed() {
		pageDispatcher.back();
		super.backPressed();
	}
	
	protected abstract String getPageSectionName();
}
