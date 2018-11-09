package ru.neodoc.content.codegen.wizard;

import org.eclipse.swt.widgets.Composite;

public abstract class SourceCodeGeneratorWizardPageImpl extends SubjectAwareWizardPageImpl implements SourceCodeGeneratorWizardPage {

	protected SourceCodeGeneratorWizardPageImpl(String pageName) {
		super(pageName);
	}

	/* (non-Javadoc)
	 * @see ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizardPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizardPage#backPressed()
	 */
	@Override
	public void backPressed(){
		
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizardPage#nextPressed()
	 */
	@Override
	public void nextPressed(){
		
	}
	
}
