package ru.neodoc.content.codegen.sdoc2.wizard;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import ru.neodoc.content.codegen.sdoc2.CodegenManager;
import ru.neodoc.content.codegen.sdoc2.SdocCodegenPlugin;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.ConfigurableDispatchedPageImpl;
import ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizard;

public abstract class SdocCodegenWizardPage extends ConfigurableDispatchedPageImpl {

	protected Composite container;
	
	private IDialogSettings pageSettings = null;
	
	public CodegenManager getCodegenManager() {
		return (CodegenManager)configuration.getValue(CodegenManager.PROP_NAME);
	}

/*	public void setCodegenManager(CodegenManagerOld codegenManager) {
		this.codegenManager = codegenManager;
	}
*/
	protected SdocCodegenWizardPage(String pageName) {
		super(pageName);
	}
	
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		
		doCreateControl(parent);
		
        // required to avoid an error in the system
        setControl(container);
        setPageComplete(false);
        
        internalRefresh();
	}
	
	public boolean isControlCreated(){
		return (container != null);
	}
	
	
	protected void doCreateControl(Composite parent){
		// TO OVERRIDE
	}

	protected void internalRefresh(){
		// TO OVERRIDE
	}
	
	public boolean isLastPage(){
		return false;
	}
	
/*	@Override
	public IWizardPage getNextPage() {
		return pageDispatcher.getNext();
	}
*/	
	@Override
	public IWizardPage getPreviousPage() {
		IWizardPage page = super.getPreviousPage();
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

	protected abstract String getPageSectionName();
}
