package ru.neodoc.content.codegen.sdoc2.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class StartPage extends SdocCodegenWizardPage {

	private Composite container;

	protected StartPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getPageSectionName() {
		return "start";
	}
	
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 2;
        
        Label label1 = new Label(container, SWT.NONE);
        label1.setText("Found generators: ");

        Text text1 = new Text(container, SWT.BORDER | SWT.SINGLE);
        text1.setText("");
        
        setControl(container);
        setPageComplete(false);
	}
	
}
