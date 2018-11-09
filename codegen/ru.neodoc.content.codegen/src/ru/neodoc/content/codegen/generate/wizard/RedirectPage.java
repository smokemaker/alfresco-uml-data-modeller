package ru.neodoc.content.codegen.generate.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import ru.neodoc.content.codegen.wizard.SubjectAwareWizardPageImpl;

public class RedirectPage extends SubjectAwareWizardPageImpl {

	protected RedirectPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		setControl(container);
		setPageComplete(false);
	}

	
	
}
